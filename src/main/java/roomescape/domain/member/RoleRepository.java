package roomescape.domain.member;

import java.util.Optional;

public interface RoleRepository {
    public MemberRole save(MemberRole role);

    public Optional<MemberRole> findByMemberId(long memberId);
}
