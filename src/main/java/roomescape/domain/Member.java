package roomescape.domain;

import java.util.Objects;

public class Member {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;

    public Member(String name, String email, String password) {
        this(null, name, email, password);
    }

    public Member(Long id, String name, String email, String password) {
        validate(name, email, password);

        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    private void validate(String name, String email, String password) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name은 필수 값입니다.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email는 필수 값입니다.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("password은 필수 값입니다.");
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
