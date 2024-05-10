package roomescape.domain;

import java.util.Objects;

public class Member {

    private final Long id;
    private final String email;
    private final String password;
    private final String name;
    private final Role role;

    public Member(String email, String password, String name, Role role) {
        this(null, email, password, name, role);
    }

    public Member(Long id, String email, String password, String name, Role role) {
        validate(email, name);

        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    private void validate(String email, String name) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email는 필수 값입니다.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name은 필수 값입니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
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

    public Role getRole() {
        return role;
    }
}
