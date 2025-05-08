package roomescape.business.domain.member;

public enum MemberRole {

    MEMBER("ROLE_MEMBER"),
    ADMIN("ROLE_ADMIN");

    private final String value;

    MemberRole(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
