package roomescape.fixture;

import roomescape.domain.member.Member;
import roomescape.domain.member.MemberEmail;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.MemberPassword;
import roomescape.domain.member.MemberRole;
import roomescape.dto.member.MemberNameResponse;
import roomescape.dto.member.MemberResponse;

public class MemberFixtures {

    private MemberFixtures() {
    }

    public static Member createAdminMember(String name, String email) {
        return new Member(
                null,
                new MemberName(name),
                new MemberEmail(email),
                new MemberPassword("default"),
                MemberRole.ADMIN
        );
    }

    public static Member createUserMember(long id, String name, String email, String password) {
        return new Member(
                id,
                new MemberName(name),
                new MemberEmail(email),
                new MemberPassword(password),
                MemberRole.USER
        );
    }

    public static Member createUserMember(String name) {
        return new Member(
                null,
                new MemberName(name),
                new MemberEmail("test@test.com"),
                new MemberPassword("default"),
                MemberRole.USER
        );
    }

    public static Member createUserMember(String name, String email) {
        return new Member(
                null,
                new MemberName(name),
                new MemberEmail(email),
                new MemberPassword("default"),
                MemberRole.USER
        );
    }

    public static Member createUserMember(String name, String email, String password) {
        return new Member(
                null,
                new MemberName(name),
                new MemberEmail(email),
                new MemberPassword(password),
                MemberRole.USER
        );
    }

    public static MemberResponse createMemberResponse(long id, String name, String email) {
        return new MemberResponse(createUserMember(id, name, email, "1234"));
    }

    public static MemberNameResponse createNameResponse(String name) {
        return new MemberNameResponse(createUserMember(1L, name, "test@test.com", "1234"));
    }
}
