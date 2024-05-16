package roomescape.domain.member;

public class AuthenticatedMember {
    private final long id;
    private final String name;
    private final String email;

    private AuthenticatedMember(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static AuthenticatedMember from(Member member) {
        return new AuthenticatedMember(
                member.getId(),
                member.getName(),
                member.getEmail()
        );
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
