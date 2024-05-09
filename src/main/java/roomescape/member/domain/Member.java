package roomescape.member.domain;

import io.micrometer.common.util.StringUtils;
import roomescape.global.exception.error.ErrorType;
import roomescape.global.exception.model.ValidateException;

public class Member {
    private Long id;
    private String name;
    private String email;
    private String password;

    public Member(final String name, final String email, final String password) {
        this(null, name, email, password);
    }

    public Member(final Long id, final Member member) {
        this(id, member.name, member.email, member.password);
    }

    public Member(final Long id, final String name, final String email, final String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;

        // TODO: email regex validate 추가
        validateBlank(name, email, password);
        validateName(name);
        validatePassword(password);
    }

    private void validateBlank(final String name, final String email, final String password) {
        if (StringUtils.isBlank(name) || StringUtils.isBlank(email) || StringUtils.isBlank(password)) {
            throw new ValidateException(ErrorType.MEMBER_REQUEST_DATA_BLANK,
                    String.format("회원(Member) 생성에 유효하지 않은 값(null OR 공백)이 입력되었습니다. [values: %s]", this));
        }
    }

    private void validateName(final String name) {
        if (name.length() < 1 || name.length() > 10) {
            throw new ValidateException(ErrorType.MEMBER_REQUEST_DATA_BLANK,
                    String.format("회원(Member)의 이름은 1자 이상 10자 이하여야 합니다. [name: %s]", name));
        }
    }

    private void validatePassword(final String password) {
        if (password.length() < 8 || password.length() > 16) {
            throw new ValidateException(ErrorType.MEMBER_REQUEST_DATA_BLANK,
                    String.format("회원(Member)의 패스워드는은 8자 이상 16자 이하여야 합니다. [password: %s]", password));
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

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
