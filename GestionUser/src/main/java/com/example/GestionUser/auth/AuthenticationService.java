package com.example.GestionUser.auth;

import com.example.GestionUser.entities.Token;
import com.example.GestionUser.entities.User;
import com.example.GestionUser.handler.BusinessErrorCodes;
import com.example.GestionUser.handler.BusinessException;
import com.example.GestionUser.repositories.RoleRepository;
import com.example.GestionUser.repositories.TokenRepository;
import com.example.GestionUser.repositories.UserRepository;
import com.example.GestionUser.security.JwtService;
import com.example.GestionUser.services.EmailService;
import com.example.GestionUser.services.EmailTemplateName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;

    @Value("${mailing.frontend.activation-url}")
    private String activationUrl;

//    public void register(RegistrationRequest request) throws MessagingException {
//        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
//            throw new BusinessException(BusinessErrorCodes.EMAIL_ALREADY_EXISTS);
//        }
//
//        User user = new User();
//        user.setFirstname(request.getFirstname());
//        user.setLastname(request.getLastname());
//        user.setEmail(request.getEmail());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setAccount_locked(false);
//        user.setEnabled(false);
//
//        userRepository.save(user);
//        sendValidationEmail(user);
//    }

    public void register(RegistrationRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException(BusinessErrorCodes.EMAIL_ALREADY_EXISTS);
        }

        User user = new User();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAccount_locked(false);
        user.setEnabled(true); // <-- ACTIVE DIRECTEMENT !

        userRepository.save(user);

        // On ne fait plus d'envoi de mail ni de génération de token
    }


    private void sendValidationEmail(User user) throws MessagingException {
        String newToken = generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationCode(6);

        Token token = new Token();
        token.setToken(generatedToken);
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        token.setUser(user);

        tokenRepository.save(token);

        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // authentification
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );
        Authentication authResult = authenticationManager.authenticate(authToken);

        // rechargement de l’utilisateur avec ses rôles et permissions
        User user = userRepository.findByEmail(request.getEmail())
                .map(u -> {
                    // forcer le chargement des permissions
                    u.getRoles().forEach(role -> {
                        role.getPermissionLists().forEach(pl -> {
                            pl.getPermissions().size();
                        });
                    });
                    return u;
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // extraction des permissions
        Set<String> allPermissions = user.getRoles().stream()
                .flatMap(role -> role.getPermissionLists().stream())
                .flatMap(pl -> pl.getPermissions().stream())
                .map(p -> p.getFeature() + "." + p.getAction())
                .collect(Collectors.toSet());

        // préparation des claims
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("fullName", user.getFullName());
        claims.put("authorities", allPermissions);

        String jwtToken = jwtService.generateToken(claims, user);

        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(jwtToken);
        return response;

    }

    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new BusinessException(BusinessErrorCodes.TOKEN_EXPIRED);
        }

        User user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }

    public void resendActivationToken(String email) throws MessagingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(
                        BusinessErrorCodes.ACCOUNT_NOT_FOUND,
                        "Aucun compte avec cet email"));

        if (user.isEnabled()) {
            throw new BusinessException(
                    BusinessErrorCodes.ACCOUNT_ALREADY_ACTIVATED,
                    "Le compte est déjà activé");
        }

        // Récupère le dernier token (Optional<Token>)
        Optional<Token> lastTokenOpt = tokenRepository
                .findTopByUserOrderByCreatedAtDesc(user);

        if (lastTokenOpt.isPresent()) {
            Token lastToken = lastTokenOpt.get();
            // si créé il y a moins de 30 secondes, on bloque la nouvelle demande
            if (lastToken.getCreatedAt()
                    .isAfter(LocalDateTime.now().minusSeconds(30))) {
                throw new BusinessException(
                        BusinessErrorCodes.TOKEN_ALREADY_SENT,
                        "Veuillez patienter avant de redemander un code.");
            }
        }

        // Sinon on génère et envoie un nouveau token
        sendValidationEmail(user);
    }


    public void resendToken(String email) throws MessagingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(BusinessErrorCodes.USER_NOT_FOUND,
                        "Aucun utilisateur associé à cet email"));

        if (user.isEnabled()) {
            throw new BusinessException(BusinessErrorCodes.ACCOUNT_ALREADY_ACTIVATED,
                    "Ce compte est déjà activé.");
        }

        log.info("Resending activation email to {}", user.getEmail());
        sendValidationEmail(user);
    }
}
