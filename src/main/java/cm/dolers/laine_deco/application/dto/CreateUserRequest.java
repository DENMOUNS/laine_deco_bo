package cm.dolers.laine_deco.application.dto;

import jakarta.annotation.Nullable;

/**
 * Record DTO pour créer/modifier un utilisateur
 */
public record CreateUserRequest(
    @Nullable String email,
    @Nullable String password,
    @Nullable String name,
    @Nullable String phone
) {}

