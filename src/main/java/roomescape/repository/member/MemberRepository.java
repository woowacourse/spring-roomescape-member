package roomescape.repository.member;

import roomescape.entity.MemberEntity;

public interface MemberRepository {

    MemberEntity findMember(MemberEntity memberRequest);

    MemberEntity findMemberById(Long id);
}
