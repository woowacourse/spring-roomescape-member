package roomescape.domain;

public record Member(Long id, Name name, Email email, Password password, Role role) {

    public static Member of(final Long id,
                            final String name,
                            final String email,
                            final String password,
                            final String role) {
        return new Member(id, new Name(name), new Email(email), new Password(password), Role.valueOf(role));
    }

    public static Member of(final long id, final Member member) {
        return new Member(id, member.name, member.email, member.password, member.role);
    }

    public static Member of(final Long id, final String name, final String role) {
        return Member.of(id, name, null, null, role);
    }

    public String nameAsString() {
        return name.value();
    }
}
