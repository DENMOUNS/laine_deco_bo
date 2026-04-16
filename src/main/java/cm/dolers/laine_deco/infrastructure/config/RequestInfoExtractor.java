package cm.dolers.laine_deco.infrastructure.config;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Utilitaire pour extraire les informations du client (IP, User-Agent)
 */
public class RequestInfoExtractor {

    /**
     * Extrait l'adresse IP du client, en tenant compte des proxies
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        if (request == null) {
            return "UNKNOWN";
        }

        // Vérifier les headers des proxies
        String[] ipHeaders = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_FORWARDED_HOST",
            "HTTP_CF_CONNECTING_IP"
        };

        for (String header : ipHeaders) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // X-Forwarded-For peut contenir plusieurs IPs
                if (ip.contains(",")) {
                    ip = ip.split(",")[0].trim();
                }
                return ip;
            }
        }

        // Fallback sur l'IP directe
        String ip = request.getRemoteAddr();
        return ip != null ? ip : "UNKNOWN";
    }

    /**
     * Extrait le User-Agent du navigateur
     */
    public static String getUserAgent(HttpServletRequest request) {
        if (request == null) {
            return "UNKNOWN";
        }
        
        String userAgent = request.getHeader("User-Agent");
        return userAgent != null ? userAgent : "UNKNOWN";
    }

    /**
     * Extrait la méthode HTTP (GET, POST, PUT, DELETE, etc.)
     */
    public static String getHttpMethod(HttpServletRequest request) {
        if (request == null) {
            return "UNKNOWN";
        }
        
        String method = request.getMethod();
        return method != null ? method : "UNKNOWN";
    }

    /**
     * Extrait le chemin de la requête (endpoint)
     * Ex: /api/products/search
     */
    public static String getRequestPath(HttpServletRequest request) {
        if (request == null) {
            return "/";
        }
        
        String path = request.getRequestURI();
        return path != null ? path : "/";
    }

    /**
     * Extrait la query string complète
     * Ex: ?keyword=lampe&page=0
     */
    public static String getQueryString(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        
        String queryString = request.getQueryString();
        return queryString; // Peut être null si pas de params
    }

    /**
     * Récupère l'URL complète sans la query string
     */
    public static String getFullUrl(HttpServletRequest request) {
        if (request == null) {
            return "UNKNOWN";
        }
        
        StringBuilder url = new StringBuilder(request.getScheme())
            .append("://")
            .append(request.getServerName());
            
        if ((request.getScheme().equals("http") && request.getServerPort() != 80) ||
            (request.getScheme().equals("https") && request.getServerPort() != 443)) {
            url.append(":").append(request.getServerPort());
        }
        
        url.append(request.getRequestURI());
        
        return url.toString();
    }

    /**
     * Extrait le User-Agent court (browser/version)
     */
    public static String getUserAgentShort(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        
        if (userAgent.contains("Chrome")) {
            return "Chrome";
        } else if (userAgent.contains("Safari")) {
            return "Safari";
        } else if (userAgent.contains("Firefox")) {
            return "Firefox";
        } else if (userAgent.contains("Edge")) {
            return "Edge";
        } else if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            return "Internet Explorer";
        } else if (userAgent.contains("Mobile")) {
            return "Mobile";
        }
        
        return "Unknown Browser";
    }

    private RequestInfoExtractor() {
        // Utilitaire - pas d'instanciation
    }
}
