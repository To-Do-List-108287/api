package com._108287.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

public interface JwtService {
  Optional<Claims> validateJwtAndExtractClaims(String token) throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeySpecException;

}
