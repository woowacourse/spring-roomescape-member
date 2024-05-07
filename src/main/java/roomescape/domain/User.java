package roomescape.domain;

import io.micrometer.common.util.StringUtils;

public class User {

    private final String name;
    private final UserEmail email;
    private final UserPassword password;


    public User(String name, UserEmail email, UserPassword password) {
        validate(name, email, password);
        this.name = name;
        this.email = email;
        this.password = password;
    }

    private void validate(String name, UserEmail email, UserPassword password) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("사용자 이름은 비어 있을 수 없습니다.");
        }
        if (email == null || password == null) {
            throw new IllegalArgumentException("사용자는 이메일, 비밀 번호가 필수입니다.");
        }
    }
}
