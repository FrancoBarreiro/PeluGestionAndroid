package com.sandrapeinados.pelugestion.services;

import com.sandrapeinados.pelugestion.models.JwtAuthenticationResponse;
import com.sandrapeinados.pelugestion.models.SignInRequest;
import com.sandrapeinados.pelugestion.models.SignUpRequest;

public interface IAuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SignInRequest request);
}
