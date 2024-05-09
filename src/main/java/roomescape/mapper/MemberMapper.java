package roomescape.mapper;

import roomescape.dto.MemberResponse;

public class MemberMapper {

    public MemberResponse mapToResponse(String name) {
        return new MemberResponse(name);
    }
}
