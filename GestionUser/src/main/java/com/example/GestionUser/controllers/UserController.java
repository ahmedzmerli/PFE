package com.example.GestionUser.controllers;

import com.example.GestionUser.auth.AuthenticationService;
import com.example.GestionUser.auth.RegistrationRequest;
import com.example.GestionUser.entities.Role;
import com.example.GestionUser.entities.User;
import com.example.GestionUser.handler.ApiException;
import com.example.GestionUser.handler.BusinessErrorCodes;
import com.example.GestionUser.handler.ResourceNotFoundException;
import com.example.GestionUser.repositories.RoleRepository;
import com.example.GestionUser.repositories.UserRepository;
import javax.mail.MessagingException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
// @PreAuthorize("hasAuthority('admin.user')")
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationService authService;

    @PostMapping("/users")
    @PreAuthorize("hasAuthority('users.create')")
    public ResponseEntity<?> createUser(@Valid @RequestBody RegistrationRequest request) throws MessagingException {
        authService.register(request);
//        return ResponseEntity.accepted()
//                .body(Collections.singletonMap("message",
//                        "Utilisateur créé, un email d'activation a été envoyé."));
        return ResponseEntity.accepted()
                .body(Collections.singletonMap("message", "Utilisateur créé et activé avec succès."));

    }





    @GetMapping("/users")
    @PreAuthorize("hasAuthority('users.read')")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userRepository.findAll();
        Map<String,Object> resp = new HashMap<>();
        resp.put("count", users.size());
        resp.put("users", users);
        return ResponseEntity.ok(resp);
    }




    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority('users.delete')")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiException(BusinessErrorCodes.USER_NOT_FOUND));
        user.getRoles().clear();
        userRepository.save(user);
        userRepository.deleteById(id);
        return ResponseEntity.ok(
                Collections.singletonMap("message", "Utilisateur supprimé avec succès.")
        );
    }



    @PutMapping("/users/{id}")
    @PreAuthorize("hasAuthority('users.update')")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiException(BusinessErrorCodes.USER_NOT_FOUND));
        try {
            user.setFirstname(updatedUser.getFirstname());
            user.setLastname(updatedUser.getLastname());
            user.setEmail(updatedUser.getEmail());
            user.setAccount_locked(updatedUser.isAccount_locked());
            // ... gestion des rôles ...
            userRepository.save(user);
            Map<String,Object> resp = new HashMap<>();
            resp.put("message", "Utilisateur mis à jour.");
            resp.put("user", user);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            throw new ApiException(BusinessErrorCodes.ACCOUNT_NOT_FOUND);
        }
    }




    // @PutMapping("/users/{userId}/roles")
    // // @PreAuthorize("hasAuthority('admin.user')")
    // public ResponseEntity<?> assignRolesToUser(@PathVariable Integer userId, @RequestBody List<Integer> roleIds) {
    //     return userRepository.findById(userId).map(user -> {
    //         var roles = roleRepository.findAllById(roleIds);
    //         user.setRoles(roles);
    //         return ResponseEntity.ok(userRepository.save(user));
    //     }).orElse(ResponseEntity.notFound().build());
    // }

    @GetMapping("/users/{id}")
// @PreAuthorize("hasAuthority('admin.user')")
public ResponseEntity<User> getUserById(@PathVariable Integer id) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new ApiException(BusinessErrorCodes.USER_NOT_FOUND));
    return ResponseEntity.ok(user);
}


    @PostMapping("/users/{userId}/roles/{roleId}")
    @PreAuthorize("hasAuthority('users.roles.create')")
    public ResponseEntity<?> addRoleToUser(@PathVariable Integer userId, @PathVariable Integer roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(BusinessErrorCodes.USER_NOT_FOUND));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ApiException(BusinessErrorCodes.ROLE_NOT_FOUND));
        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRepository.save(user);
        }
        Map<String,Object> resp = new HashMap<>();
        resp.put("message", "Rôle ajouté à l'utilisateur.");
        resp.put("user", user);
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/users/{userId}/roles/{roleId}")
    @PreAuthorize("hasAuthority('users.roles.delete')")

    public ResponseEntity<?> removeRoleFromUser(@PathVariable Integer userId, @PathVariable Integer roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(BusinessErrorCodes.USER_NOT_FOUND));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ApiException(BusinessErrorCodes.ROLE_NOT_FOUND));

        user.getRoles().remove(role);
        userRepository.save(user);

        return ResponseEntity.ok(user);
    }


@GetMapping("/users/{id}/roles")
@PreAuthorize("hasAuthority('users.roles.read')")
public ResponseEntity<Set<Role>> getUserRoles(@PathVariable Integer id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", id));
    return ResponseEntity.ok(user.getRoles());
}




}
