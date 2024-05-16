package roomescape.dto.response;

public class LoginResponse {

    private final String name;

    public LoginResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
