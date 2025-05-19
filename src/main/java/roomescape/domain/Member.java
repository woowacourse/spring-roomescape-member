package roomescape.domain;

import java.util.Objects;
import lombok.Getter;

@Getter
public class Member {

    private static final String KOREAN_WORDS_REGEX = "^[가-힣]+$";
    private static final String EMAIL_FORMAT_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final int NAME_MAX_SIZE = 5;

    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final MemberRole role;

    public Member(final Long id, final String name, final String email, final String password, final MemberRole role) {
        validateNotNulls(name, email, password, role);
        validateNameKoreanWords(name);
        validateNameSize(name);
        validateEmailFormat(email);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Member(final String name, final String email, final String password, final MemberRole role) {
        this(null, name, email, password, role);
    }

    public Member assignId(final Long id) {
        return new Member(id, name, email, password, role);
    }

    public boolean checkPassword(final String password) {
        return this.password.equals(password);
    }

    private void validateNotNulls(String name, String email, String password, MemberRole role) {
        Objects.requireNonNull(name, "멤버 이름을 입력 해 주세요.");
        Objects.requireNonNull(email, "이메일을 입력 해 주세요.");
        Objects.requireNonNull(password, "비밀번호를 입력 해 주세요.");
        Objects.requireNonNull(role, "역할을 입력 해 주세요.");
    }

    private void validateNameKoreanWords(String name) {
        if (!name.matches(KOREAN_WORDS_REGEX)) {
            throw new IllegalArgumentException("[ERROR] 이름은 한국어로만 입력해 주세요.");
        }
    }

    private void validateNameSize(String name) {
        if (name.length() > NAME_MAX_SIZE) {
            throw new IllegalArgumentException(
                    "[ERROR] 이름은 " + NAME_MAX_SIZE + "글자 이내로 입력해 주세요. 현재 길이는 " + name.length() + "글자 입니다.");
        }
    }

    private void validateEmailFormat(String email) {
        if (email == null || !email.matches(EMAIL_FORMAT_REGEX)) {
            throw new IllegalArgumentException("잘못된 이메일 형식입니다: " + email);
        }
    }
}
