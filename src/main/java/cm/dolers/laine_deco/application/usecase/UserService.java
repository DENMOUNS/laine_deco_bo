package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.*;

/**
 * Interface Service pour User (SOLID - Dependency Inversion)
 * Contiens la logique métier liée à la gestion des utilisateurs
 */
public interface UserService {
    /**
     * Créer un nouvel utilisateur
     */
    UserResponse createUser(CreateUserRequest request);
    
    /**
     * Récupérer un utilisateur par ID
     */
    UserResponse getUserById(Long userId);
    
    /**
     * Récupérer un utilisateur par email
     */
    UserResponse getUserByEmail(String email);
    
    /**
     * Mettre à jour le profil utilisateur
     */
    UserResponse updateUser(Long userId, CreateUserRequest request);
    
    /**
     * Supprimer un utilisateur
     */
    void deleteUser(Long userId);
    
    /**
     * Ajouter des points de loyauté
     */
    void addLoyaltyPoints(Long userId, Integer points);
}
