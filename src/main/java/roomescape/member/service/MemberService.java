package roomescape.member.service;

import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;

public interface MemberService {
    MemberResponse create(MemberRequest request);
}
