package cm.dolers.laine_deco.interfaces.rest.response;

public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private long expiresAtEpochSeconds;
    private String email;
    private String name;

    public AuthResponse(String token, long expiresAtEpochSeconds, String email, String name) {
        this.token = token;
        this.expiresAtEpochSeconds = expiresAtEpochSeconds;
        this.email = email;
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
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
