package roomescape.member.service;

import roomescape.member.domain.Member;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;

public interface MemberService {
    MemberResponse create(MemberRequest request);

    Member findById(Long id);
}
