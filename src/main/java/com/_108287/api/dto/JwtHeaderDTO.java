package com._108287.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtHeaderDTO {
  String kid;
  String alg;
}
