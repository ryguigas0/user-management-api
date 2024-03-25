package dev.guiga.proj1.user_management.transfer;

import jakarta.validation.constraints.NotBlank;

public record UserInTO(@NotBlank String username, @NotBlank String password) {

}
