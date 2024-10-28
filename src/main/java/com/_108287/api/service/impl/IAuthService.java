package com._108287.api.service.impl;

import com._108287.api.dto.AuthenticationResponseDTO;
import com._108287.api.dto.TokenResponseDTO;
import com._108287.api.service.AuthService;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Optional;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

@Service
@NoArgsConstructor
public class IAuthService implements AuthService {

  static final Logger logger = getLogger(lookup().lookupClass());
  RestTemplate restTemplate;

  @Value("${CLIENT_ID}")
  private String clientId;

  @Value("${CLIENT_SECRET}")
  private String clientSecret;

  @Value("${REDIRECT_URI}")
  private String redirectUri;

  @Value("${TOKEN_URL}")
  private String tokenUrl;

  @Autowired
  public IAuthService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public Optional<AuthenticationResponseDTO> signIn(String code) {
    String clientCredentials = Base64.getEncoder().encodeToString(String.format(
      "%s:%s", clientId, clientSecret
    ).getBytes());
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.set("Authorization", "Basic " + clientCredentials);

    MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
    formParams.add("grant_type", "authorization_code");
    formParams.add("client_id", clientId);
    formParams.add("code", code); // Replace with actual authorization code
    formParams.add("redirect_uri", redirectUri);

    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formParams, headers);

    try {
      ResponseEntity<TokenResponseDTO> response = restTemplate.exchange(
        tokenUrl,
        HttpMethod.POST,
        requestEntity,
        TokenResponseDTO.class
      );
      if (response.getStatusCode() != HttpStatus.OK) {
        return Optional.empty();
      }
      TokenResponseDTO tokenResponseDTO = response.getBody();
      if (tokenResponseDTO == null) {
        return Optional.empty();
      }
      return Optional.of(new AuthenticationResponseDTO(
        tokenResponseDTO.getId_token(),
        tokenResponseDTO.getExpires_in()
      ));
    } catch (HttpClientErrorException e) {
      // restTemplate.exchange throws exceptions for 4xx and 5xx responses
      // 400 status code may happen in case of invalid_grant for instance
      logger.error("Error while exchanging code for token", e);
      return Optional.empty();
    }
  }
}
