package roomescape.service.dto;

public class LoginMemberRequestDto {

    private final String email;
    private final String password;

    public LoginMemberRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
