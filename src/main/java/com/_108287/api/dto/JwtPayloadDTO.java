package com._108287.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtPayloadDTO {

  @JsonProperty("at_hash") String atHash;
  String sub;
  @JsonProperty("email_verified") String emailVerified;
  String iss;
  @JsonProperty("cognito:username") String cognitoUsername;
  @JsonProperty("origin_jti") String originJti;
  String aud;
  @JsonProperty("event_id") String eventId;
  @JsonProperty("token_use") String tokenUse;
  @JsonProperty("auth_time") String authTime;
  String exp;
  String iat;
  String jti;
  String email;
}
