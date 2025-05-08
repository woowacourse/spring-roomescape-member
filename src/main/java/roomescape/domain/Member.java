package roomescape.domain;

import roomescape.common.exception.MemberValidationException;

import java.util.Objects;

public class Member {
    private final Long id;
    private final String name;
    private final String email;
    private final String password;

    public Member(Long id, String name, String email, String password) {
        validate(name, email, password);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static Member generateWithPrimaryKey(Member member, Long newPrimaryKey) {
        return new Member(newPrimaryKey, member.name, member.email, member.password);
    }

    private void validate(String name, String email, String password) {
        if (name == null || name.isBlank()) {
            throw new MemberValidationException("이름은 비어있을 수 없습니다.");
        }
        if (email == null || email.isBlank()) {
            throw new MemberValidationException("이메일은 비어있을 수 없습니다.");
        }
        if (password == null || password.isBlank()) {
            throw new MemberValidationException("패스워드는 비어있을 수 없습니다.");
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
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        if (member.getId() == null || id == null) {
            return false;
        }
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
