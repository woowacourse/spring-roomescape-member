package roomescape.domain.member;

import java.util.Optional;

public interface RoleRepository {
    MemberRole save(MemberRole role);

    Optional<MemberRole> findByMemberId(long memberId);
}
