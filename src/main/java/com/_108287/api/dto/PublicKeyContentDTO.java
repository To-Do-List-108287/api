package com._108287.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicKeyContentDTO {
  String kid;
  String kty;
  String alg;
  String n;
  String e;
}
