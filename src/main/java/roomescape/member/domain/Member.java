package roomescape.member.domain;

import java.util.Objects;

public class Member {
    private static final String NULL_VALUE_EXCEPTION_MESSAGE = "널 값은 저장될 수 없습니다.";

    private final Long id;
    private final String name;
    private final String email;
    private final String password;

    public Member(String name, String email, String password) {
        this.id = null;
        this.name = Objects.requireNonNull(name, NULL_VALUE_EXCEPTION_MESSAGE);
        this.email = Objects.requireNonNull(email, NULL_VALUE_EXCEPTION_MESSAGE);
        this.password = Objects.requireNonNull(password, NULL_VALUE_EXCEPTION_MESSAGE);
    }

    public Member(Long id, String name, String email, String password) {
        this.id = Objects.requireNonNull(id, NULL_VALUE_EXCEPTION_MESSAGE);
        this.name = Objects.requireNonNull(name, NULL_VALUE_EXCEPTION_MESSAGE);
        this.email = Objects.requireNonNull(email, NULL_VALUE_EXCEPTION_MESSAGE);
        this.password = Objects.requireNonNull(password, NULL_VALUE_EXCEPTION_MESSAGE);
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
    public boolean equals(Object other) {
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Member member = (Member) other;
        return Objects.equals(id, member.id)
                && Objects.equals(name, member.name)
                && Objects.equals(email, member.email)
                && Objects.equals(password, member.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, password);
    }
}
