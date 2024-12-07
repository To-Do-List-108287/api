package com._108287.api.service.impl;

import com._108287.api.dto.JwtHeaderDTO;
import com._108287.api.dto.JwtPayloadDTO;
import com._108287.api.dto.PublicKeyContentDTO;
import com._108287.api.dto.PublicKeyResponseDTO;
import com._108287.api.service.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Optional;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

@Service
@NoArgsConstructor
public class IJwtService implements JwtService {
  static final Logger logger = getLogger(lookup().lookupClass());
  RestTemplate restTemplate;

  @Autowired
  public IJwtService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Value("${TOKEN_SIGNING_KEY_URL}")
  private String jwksUrl;

  private Optional<Claims> verifyToken(String token) throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeySpecException {
    String[] tokenParts = token.split("\\.");
    if (tokenParts.length != 3) {
      return Optional.empty();
    }
    Base64.Decoder decoder = Base64.getUrlDecoder();
    JwtHeaderDTO header = new ObjectMapper().readValue(
      new String(decoder.decode(tokenParts[0])),
      JwtHeaderDTO.class
    );
    JwtPayloadDTO payload = new ObjectMapper().readValue(
      new String(decoder.decode(tokenParts[1])),
      JwtPayloadDTO.class
    );
    try {
      Optional<RSAPublicKey> publicKey = getPublicKey(header.getKid());
      return publicKey
        .map(rsaPublicKey -> Jwts.parser().verifyWith(rsaPublicKey).build().parseSignedClaims(token).getPayload());
    } catch (ExpiredJwtException e) {
      logger.error(e.getMessage());
      return Optional.empty();
    }

  }

  private Optional<RSAPublicKey> getPublicKey(String kid) throws NoSuchAlgorithmException, InvalidKeySpecException {
    PublicKeyResponseDTO jwks = restTemplate.getForObject(
      jwksUrl,
      PublicKeyResponseDTO.class
    );

    if (jwks != null){
      for (PublicKeyContentDTO publicKeyContentDTO : jwks.getKeys()) {
        if (publicKeyContentDTO.getKid().equals(kid)){
          byte[] decodedModulus = Base64.getUrlDecoder().decode(publicKeyContentDTO.getN());
          byte[] decodedExponent = Base64.getUrlDecoder().decode(publicKeyContentDTO.getE());
          BigInteger modBigInt = new BigInteger(1, decodedModulus);
          BigInteger expBigInt = new BigInteger(1, decodedExponent);

          RSAPublicKeySpec spec = new RSAPublicKeySpec(modBigInt, expBigInt);
          KeyFactory keyFactory = KeyFactory.getInstance("RSA");

          return Optional.of((RSAPublicKey) keyFactory.generatePublic(spec));
        }
      }
    }
    return Optional.empty();
  }

  @Override
  public Optional<Claims> validateJwtAndExtractClaims(String token) throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeySpecException {
    return verifyToken(token);
  }
}
