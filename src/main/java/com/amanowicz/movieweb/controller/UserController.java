package com.amanowicz.movieweb.controller;

import com.amanowicz.movieweb.exception.UserAlreadyExistsException;
import com.amanowicz.movieweb.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

import static com.amanowicz.movieweb.utils.JwtUtils.createJWTToken;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Validated
public class UserController {

    private static final String JWT_TOKEN = "token";
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestParam @NotBlank String username, @RequestParam @NotBlank String password) {
        userService.register(username, password);
        String token = createJWTToken(username);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(JWT_TOKEN, token)
                .build();
    }

    @PostMapping
    public ResponseEntity login(@RequestParam String username, @RequestParam String password) {
        userService.login(username, password);
        String token = createJWTToken(username);

        return ResponseEntity.status(HttpStatus.OK)
                .header(JWT_TOKEN, token)
                .build();
    }

    @ExceptionHandler({AuthenticationServiceException.class})
    public ResponseEntity<String> handleNotFound(AuthenticationServiceException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler({UserAlreadyExistsException.class})
    public ResponseEntity<String> handleNotFound(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
