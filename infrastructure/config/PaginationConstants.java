package cm.dolers.laine_deco.infrastructure.config;

import java.util.Arrays;
import java.util.List;

/**
 * Constantes et utilitaires pour la pagination à travers l'application
 * Tailles de page prédéfinies: 5, 10, 25, 50, 100, 250
 */
public class PaginationConstants {
    
    // Tailles de page disponibles
    public static final int PAGE_SIZE_5 = 5;
    public static final int PAGE_SIZE_10 = 10;
    public static final int PAGE_SIZE_25 = 25;
    public static final int PAGE_SIZE_50 = 50;
    public static final int PAGE_SIZE_100 = 100;
    public static final int PAGE_SIZE_250 = 250;
    
    // Taille par défaut
    public static final int DEFAULT_PAGE_SIZE = PAGE_SIZE_10;
    
    // Taille maximale
    public static final int MAX_PAGE_SIZE = PAGE_SIZE_250;
    
    // Liste des tailles acceptées
    public static final List<Integer> ALLOWED_PAGE_SIZES = Arrays.asList(
        PAGE_SIZE_5,
        PAGE_SIZE_10,
        PAGE_SIZE_25,
        PAGE_SIZE_50,
        PAGE_SIZE_100,
        PAGE_SIZE_250
    );
    
    /**
     * Valide et normalise la taille de page
     * Si la taille n'est pas dans les valeurs acceptées, cherche la plus proche
     * @param requestedSize la taille demandée
     * @return une taille valide
     */
    public static int normalizePageSize(Integer requestedSize) {
        if (requestedSize == null) {
            return DEFAULT_PAGE_SIZE;
        }
        
        // Si la taille est dans les valeurs acceptées
        if (ALLOWED_PAGE_SIZES.contains(requestedSize)) {
            return requestedSize;
        }
        
        // Trouver la plus proche taille acceptée (inférieure)
        return ALLOWED_PAGE_SIZES.stream()
            .filter(size -> size <= requestedSize)
            .max(Integer::compareTo)
            .orElse(DEFAULT_PAGE_SIZE);
    }
    
    /**
     * Valide la taille de page (true si valide)
     */
    public static boolean isValidPageSize(int pageSize) {
        return ALLOWED_PAGE_SIZES.contains(pageSize);
    }
    
    private PaginationConstants() {
        // Utilitaire - pas d'instanciation
    }
}
