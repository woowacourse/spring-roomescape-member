package roomescape.member.domain;

import roomescape.handler.exception.CustomBadRequest;
import roomescape.handler.exception.CustomException;
import java.util.regex.Pattern;

public class Member {

    private final Pattern emailPattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    private final Long id;
    private final String name;
    private final String email;
    private final String password;

    public Member(Long id, String name, String email, String password) {
        validateEmail(email);

        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    private void validateEmail(String email) {
        if (!emailPattern.matcher(email).matches()) {
            throw new CustomException(CustomBadRequest.INVALID_EMAIL);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
