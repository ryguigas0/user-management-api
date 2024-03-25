package dev.guiga.proj1.user_management.transfer;

import jakarta.validation.constraints.NotBlank;

public record UserChangePasswordInTO(
        @NotBlank String username,
        @NotBlank String oldPassword,
        @NotBlank String newPassword) {
}