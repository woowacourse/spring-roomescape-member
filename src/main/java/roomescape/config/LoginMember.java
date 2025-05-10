package roomescape.config;

import roomescape.business.domain.member.MemberRole;
import roomescape.exception.UnAuthorizedException;

public record LoginMember(
        Long id,
        String name,
        MemberRole role
) {

    public static final LoginMember ANONYMOUS = new LoginMember(null, null, null);

    public boolean isAdmin() {
        return role == MemberRole.ADMIN;
    }

    @Override
    public Long id() {
        if (id == null) {
            throw new UnAuthorizedException("로그인 정보가 없습니다.");
        }
        return id;
    }

    @Override
    public String name() {
        if (name == null || name.isBlank()) {
            throw new UnAuthorizedException("로그인 정보가 없습니다.");
        }
        return name;
    }

    @Override
    public MemberRole role() {
        if (role == null) {
            throw new UnAuthorizedException("로그인 정보가 없습니다.");
        }
        return role;
    }
}
