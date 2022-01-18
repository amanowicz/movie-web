package com.amanowicz.movieweb.controller;

import com.amanowicz.movieweb.model.UserDto;
import com.amanowicz.movieweb.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public UserDto register(@RequestParam @NotBlank String username, @RequestParam @NotBlank String password) {
        userService.register(username, password);
        String token = getJWTToken(username);

        return new UserDto(username, token);
    }

    @PostMapping()
    public UserDto login(@RequestParam String username, @RequestParam String password) {
        userService.login(username, password);
        String token = getJWTToken(username);

        return new UserDto(username, token);
    }

    @ExceptionHandler({AuthenticationServiceException.class})
    public ResponseEntity<String> handleNotFound(AuthenticationServiceException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    private String getJWTToken(String username){
        String secretKey = "movieweb123";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts.builder()
                .setId("moviewebJWT")
                .setSubject(username)
                .claim("authorities", grantedAuthorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60*60*1000))
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes()).compact();

        return "Bearer " + token;
    }

}
