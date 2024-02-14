package checkoutapi.security.payload;

public class AuthenticationSuccess {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresInMsec;

    public AuthenticationSuccess(String accessToken, String refreshToken, Long expiresInMsec) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresInMsec = expiresInMsec;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

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

    public Long getExpiresInMsec() {
        return expiresInMsec;
    }

    public void setExpiresInMsec(Long expiresInMsec) {
        this.expiresInMsec = expiresInMsec;
    }
}
