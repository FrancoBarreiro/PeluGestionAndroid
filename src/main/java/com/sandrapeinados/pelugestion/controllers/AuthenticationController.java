package com.sandrapeinados.pelugestion.controllers;

import com.sandrapeinados.pelugestion.models.JwtAuthenticationResponse;
import com.sandrapeinados.pelugestion.models.SignInRequest;
import com.sandrapeinados.pelugestion.models.SignUpRequest;
import com.sandrapeinados.pelugestion.services.IAuthenticationService;
import com.sandrapeinados.pelugestion.services.IJwtService;
import com.sandrapeinados.pelugestion.services.IUserService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final IAuthenticationService authenticationService;

    @Autowired
    private IJwtService jwtService;

    @Autowired
    private IUserService userService;

    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signup(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }

    @PostMapping("/validate-token")
    @ResponseBody
    public ResponseEntity<?> validateToken(@RequestBody String token) {
        boolean isValid = false;
        try {
        String username = jwtService.extractUserName(token);
        UserDetails user = userService.userDetailsService().loadUserByUsername(username);

            isValid = jwtService.isTokenValid(token, user);
            return ResponseEntity.ok(isValid);
        } catch (ExpiredJwtException e) {
            return ResponseEntity.ok(isValid);
        }
    }
}
