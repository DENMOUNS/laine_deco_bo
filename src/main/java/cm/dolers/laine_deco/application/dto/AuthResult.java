package cm.dolers.laine_deco.application.dto;


public class AuthResult {
    private final String token;
    private final long expiresAtEpochSeconds;
    private final String email;
    private final String name;

    public AuthResult(String token, long expiresAtEpochSeconds, String email, String name) {
        this.token = token;
        this.expiresAtEpochSeconds = expiresAtEpochSeconds;
        this.email = email;
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public long getExpiresAtEpochSeconds() {
        return expiresAtEpochSeconds;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}

