package roomescape.member.presentation.dto;

public class SignUpRequest {
    private String email;
    private String password;
    private String name;

    public SignUpRequest() {
    }

    public SignUpRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
}
