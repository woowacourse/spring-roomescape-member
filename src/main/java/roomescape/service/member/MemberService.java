package roomescape.service.member;

import roomescape.dto.member.MemberRequest;
import roomescape.dto.member.MemberResponse;

public interface MemberService {
    MemberResponse create(MemberRequest request);
}
