package cm.dolers.laine_deco.application.dto;


/**
 * Record DTO pour créer/modifier un utilisateur
 */
public record CreateUserRequest(
    String email,
    String password,
    String name,
    String phone
) {}

