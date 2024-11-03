package com._108287.api.dto;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class PublicKeyResponseDTO {
  List<PublicKeyContentDTO> keys;
}

