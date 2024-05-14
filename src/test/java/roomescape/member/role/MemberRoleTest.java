package roomescape.member.role;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.global.exception.model.RoomEscapeException;
import roomescape.member.exception.MemberExceptionCode;

class MemberRoleTest {

    @Test
    @DisplayName("일치하는 권한이 없는 경우 에러를 던진다.")
    void findMemberRole() {
        Throwable noExistRole = assertThrows(RoomEscapeException.class, () -> MemberRole.findMemberRole("polla"));

        assertEquals(noExistRole.getMessage(), MemberExceptionCode.MEMBER_ROLE_NOT_EXIST_EXCEPTION.getMessage());
    }

    @Test
    @DisplayName("같은 권한인지 확인한다.")
    void isSameRole() {
        MemberRole[] memberRoles = new MemberRole[]{MemberRole.MEMBER};

        assertAll(
                () -> assertTrue(MemberRole.MEMBER.hasSameRoleFrom(memberRoles)),
                () -> assertFalse(MemberRole.ADMIN.hasSameRoleFrom(memberRoles))
        );
    }

    @Test
    @DisplayName("여러개의 권한 중 하나라도 포함이 되면 true를 반환한다.")
    void hasSameRule() {
        MemberRole[] memberRoles = new MemberRole[]{MemberRole.MEMBER, MemberRole.ADMIN};

        assertAll(
                () -> assertTrue(MemberRole.MEMBER.hasSameRoleFrom(memberRoles)),
                () -> assertTrue(MemberRole.ADMIN.hasSameRoleFrom(memberRoles)),
                () -> assertFalse(MemberRole.NON_MEMBER.hasSameRoleFrom(memberRoles))
        );
    }
}
