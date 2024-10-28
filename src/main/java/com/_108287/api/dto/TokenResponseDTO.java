package com._108287.api.dto;

import lombok.Data;

@Data
public class TokenResponseDTO {
  private String id_token;
  private int expires_in;
  private String token_type;
}
