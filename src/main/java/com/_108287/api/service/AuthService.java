package com._108287.api.service;

import com._108287.api.dto.AuthenticationResponseDTO;

import java.util.Optional;

public interface AuthService {

  Optional<AuthenticationResponseDTO> signIn(String code);

}
