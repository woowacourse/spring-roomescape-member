package roomescape.controller.response;

public class LoginResponse {

    private String name;

    public LoginResponse(String name) {
        this.name = name;
    }

    private LoginResponse() {
    }

    public String getName() {
        return name;
    }
}
