package roomescape.auth.dto;

public record CreateTokenResponse(String accessToken) {

    public CreateTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
