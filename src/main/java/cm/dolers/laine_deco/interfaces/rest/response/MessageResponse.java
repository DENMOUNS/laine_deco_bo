package cm.dolers.laine_deco.interfaces.rest.response;

public class MessageResponse {
    private final String message;
    private final String debugToken;

    public MessageResponse(String message) {
        this(message, null);
    }

    public MessageResponse(String message, String debugToken) {
        this.message = message;
        this.debugToken = debugToken;
    }

    public String getMessage() {
        return message;
    }

    public String getDebugToken() {
        return debugToken;
    }
}
