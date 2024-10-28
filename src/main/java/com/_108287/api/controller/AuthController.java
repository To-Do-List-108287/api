package com._108287.api.controller;

import com._108287.api.dto.AuthenticationResponseDTO;
import com._108287.api.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
  private AuthService authService;


  @GetMapping("/sign-in")
  public ResponseEntity<AuthenticationResponseDTO> getCodeToken(@RequestParam String code) {
    return authService.signIn(code)
      .map(ResponseEntity::ok)
      .orElseGet(() -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
  }

}
