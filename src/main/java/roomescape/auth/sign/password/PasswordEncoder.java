package roomescape.auth.sign.password;

public interface PasswordEncoder {

    String execute(String rawPassword);
}
