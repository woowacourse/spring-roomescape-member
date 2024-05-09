package roomescape.domain.member.mapper;

import roomescape.domain.member.dto.MemberResponse;

public class MemberMapper {

    public MemberResponse mapToResponse(String name) {
        return new MemberResponse(name);
    }
}
