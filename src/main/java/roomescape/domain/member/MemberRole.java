package roomescape.domain.member;

import java.util.Arrays;
import roomescape.common.exception.member.MemberException;

public enum MemberRole {

    ADMIN("ADMIN"),
    USER("USER");

    private final String description;

    MemberRole(final String description) {
        this.description = description;
    }

    public static MemberRole from(final String role) {
        return Arrays.stream(values())
                .filter(memberRole -> memberRole.description.equals(role))
                .findFirst()
                .orElseThrow(() -> new MemberException("존재하지 않는 권한 입니다."));
    }

    public boolean isAdmin() {
        return this.equals(ADMIN);
    }
}
