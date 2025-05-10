package roomescape.domain;

import java.util.Objects;
import roomescape.domain.regex.BaseFormat;
import roomescape.exception.InternalServerException;

public class Member {

    private static final long DEFAULT_ID = 0L;
    private static final int MAX_NAME_LENGTH = 255;

    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final MemberRole role;

    public Member(Long id, String name, String email, String password, MemberRole role) {
        validate(id, name, email, password, role);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static Member createWithoutId(String name, String email, String password, MemberRole role) {
        return new Member(DEFAULT_ID, name, email, password, role);
    }

    public boolean comparePassword(String password) {
        return this.password.equals(password);
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

    public MemberRole getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    private void validate(Long id, String name, String email, String password, MemberRole role) {
        validateId(id);
        validateName(name);
        validateEmail(email);
        validatePassword(password);
        validateRole(role);
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new InternalServerException("[ERROR] 비어있는 ID로 멤버를 생성할 수 없습니다.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InternalServerException("[ERROR] 비어있는 이름로 멤버를 생성할 수 없습니다.");
        }
        if (name.length() >= MAX_NAME_LENGTH) {
            throw new InternalServerException("[ERROR] 최대길이를 초과한 이름으로는 멤버를 생성할 수 없습니다.");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new InternalServerException("[ERROR] 비어있는 이메일로 멤버를 생성할 수 없습니다.");
        }
        if (email.length() >= MAX_NAME_LENGTH) {
            throw new InternalServerException("[ERROR] 최대길이를 초과한 이메일로는 멤버를 생성할 수 없습니다.");
        }
        if (!email.matches(BaseFormat.EMAIL)) {
            throw new InternalServerException("[ERROR] 올바르지 않은 형식의 이메일로는 멤버를 생성할 수 없습니다.");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new InternalServerException("[ERROR] 비어있는 비밀번호로 멤버를 생성할 수 없습니다.");
        }
        if (!password.matches(BaseFormat.PASSWORD)) {
            throw new InternalServerException("[ERROR] 올바르지 않은 형식의 비밀번호로는 멤버를 생성할 수 없습니다.");
        }
    }

    private void validateRole(MemberRole role) {
        if (role == null) {
            throw new InternalServerException("[ERROR] 비어있는 권한으로 멤버를 생성할 수 없습니다.");
        }
    }
}
