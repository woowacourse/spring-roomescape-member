package roomescape.domain.member;

public class MemberCredential {

    private final Long id;
    private final String email;
    private final String password;
    private final Member member;

    public MemberCredential(Long id, String email, String password, Member member) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.member = member;
    }
}
