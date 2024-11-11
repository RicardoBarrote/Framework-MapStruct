package com.artigo.mapstruct.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserResponseDto(@NotBlank String nome,
                              @Email String email) {
}
