package roomescape.domain;

public record Member(Long id, Name name, Email email, Password password) {

    public static Member of(final Long id, final String name, final String email, final String password) {
        return new Member(id, new Name(name), new Email(email), new Password(password));
    }

    public static Member of(final long id, final Member member) {
        return new Member(id, member.name, member.email, member.password);
    }

    public static Member of(final Long id, final String name) {
        return Member.of(id, name, null, null);
    }

    public String nameAsString() {
        return name.asString();
    }
}
