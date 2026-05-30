package mth.dto;

public class TokenResponseDTO {

    private String accessToken;
    private String tokenType;
    private UserResponseDTO user;

    public TokenResponseDTO() {
    }

    // ── Getters & Setters ──────────────────────────────────

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public UserResponseDTO getUser() {
        return user;
    }

    public void setUser(UserResponseDTO user) {
        this.user = user;
    }
}
