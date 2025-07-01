package com.example.GestionUser.handler;

import com.example.GestionUser.handler.BusinessErrorCodes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.ObjectError;

import javax.mail.MessagingException;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleLockedException(LockedException exp) {
        return ResponseEntity.status(UNAUTHORIZED).body(
                ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.ACCOUNT_LOCKED.getCode())
                        .businessErrorDescription(BusinessErrorCodes.ACCOUNT_LOCKED.getDescription())
                        .error(exp.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleDisabledException(DisabledException exp) {
        return ResponseEntity.status(UNAUTHORIZED).body(
                ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.ACCOUNT_DISABLED.getCode())
                        .businessErrorDescription(BusinessErrorCodes.ACCOUNT_DISABLED.getDescription())
                        .error(exp.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException exp) {
        return ResponseEntity.status(UNAUTHORIZED).body(
                ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.BAD_CREDENTIALS.getCode())
                        .businessErrorDescription(BusinessErrorCodes.BAD_CREDENTIALS.getDescription())
                        .error("Login and/or password is incorrect")
                        .build()
        );
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleMessagingException(MessagingException exp) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                ExceptionResponse.builder()
                        .error(exp.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(MethodArgumentNotValidException exp) {
        Set<String> errors = new HashSet<String>();
        for (ObjectError error : exp.getBindingResult().getAllErrors()) {
            String message = error.getDefaultMessage();
            errors.add(message);
        }

        return ResponseEntity.status(BAD_REQUEST).body(
                ExceptionResponse.builder()
                        .validationErrors(errors)
                        .build()
        );
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionResponse> handleBusinessException(BusinessException exp) {
        BusinessErrorCodes code = exp.getErrorCode();
        return ResponseEntity.status(code.getHttpStatus()).body(
                ExceptionResponse.builder()
                        .businessErrorCode(code.getCode())
                        .businessErrorDescription(code.getDescription())
                        .error(exp.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleForbidden(AccessDeniedException exp) {
        return ResponseEntity.status(FORBIDDEN).body(
                ExceptionResponse.builder()
                        .businessErrorDescription("Accès refusé")
                        .error(exp.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGeneralException(Exception exp) {
        // Log ici si besoin
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                ExceptionResponse.builder()
                        .businessErrorDescription("Internal error, please contact the admin")
                        .error("Une erreur technique est survenue.")
                        .build()
        );
    }
}
