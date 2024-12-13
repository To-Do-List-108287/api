package com._108287.api.controller;

import com._108287.api.dto.AuthenticationResponseDTO;
import com._108287.api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
  @Operation(summary = "Exchange the authorization code for the access token.", description = "This endpoint exchanges an authorization code received during the OAuth flow for an access token.")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved the access token.", content = {
    @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponseDTO.class))})
  @ApiResponse(responseCode = "400", description = "Bad Request. The authorization code was not provided.", content = {@Content(mediaType = "application/json")})
  @ApiResponse(responseCode = "401", description = "Unauthorized. The provided authorization code is invalid or expired.", content = {@Content(mediaType = "application/json")})
  public ResponseEntity<AuthenticationResponseDTO> getCodeToken(@RequestParam String code) {
    return authService.signIn(code)
      .map(ResponseEntity::ok)
      .orElseGet(() -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
  }

}
