package roomescape.member.role;

import java.util.Arrays;
import roomescape.global.exception.model.RoomEscapeException;
import roomescape.member.exception.MemberExceptionCode;

public enum MemberRole {

    MEMBER("member"),
    ADMIN("admin");

    private final String roleName;

    MemberRole(String roleName) {
        this.roleName = roleName;
    }

    public static MemberRole findMemberRole(String role) {
        return Arrays.stream(MemberRole.values())
                .filter(memberRole -> memberRole.roleName.equals(role))
                .findAny()
                .orElseThrow(() -> new RoomEscapeException(MemberExceptionCode.MEMBER_ROLE_NOT_EXIST_EXCEPTION));
    }

    public boolean isSameRole(MemberRole role) {
        return roleName.equals(role.roleName);
    }
}
