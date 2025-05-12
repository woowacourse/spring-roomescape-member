package roomescape.member.domain;

import java.util.Objects;
import roomescape.member.domain.enums.Role;

public class Member {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final Role role;

    public Member(Long id, String name, String email, String password, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        validate();
    }

    private void validate() {
        Objects.requireNonNull(name, "이름은 null일 수 없습니다.");
        Objects.requireNonNull(email, "이메일은 null일 수 없습니다.");
        Objects.requireNonNull(password, "비밀번호는 null일 수 없습니다.");
        Objects.requireNonNull(role, "권한은 null일 수 없습니다.");
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

    public Role getRole() {
        return role;
    }

    public boolean isSameId(Long memberId) {
        return id.equals(memberId);
    }

}
