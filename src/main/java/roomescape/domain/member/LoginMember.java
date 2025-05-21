package roomescape.domain.member;

public record LoginMember(Long id, String name, String email, MemberRole role) {

    public boolean isAdmin() {
        return role.isAdmin();
    }
}
