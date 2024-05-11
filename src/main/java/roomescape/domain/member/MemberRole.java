package roomescape.domain.member;

public class MemberRole {
    private final long memberId;
    private final Role role;

    public MemberRole(long memberId, Role role) {
        this.memberId = memberId;
        this.role = role;
    }

    public MemberRole(long memberId) {
        this(memberId, Role.MEMBER);
    }

    public long getMemberId() {
        return memberId;
    }

    public Role getRole() {
        return role;
    }

    public boolean isAdmin() {
        return role.isAdmin();
    }
}

