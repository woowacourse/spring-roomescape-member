package roomescape.repository.role;

import java.util.Optional;
import roomescape.domain.MemberRole;

public interface RoleRepository {
    public MemberRole save(MemberRole role);

    public Optional<MemberRole> findByMemberId(long memberId);
}
