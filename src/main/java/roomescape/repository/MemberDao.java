package roomescape.repository;

import org.springframework.stereotype.Component;
import roomescape.domain.member.Member;

import java.util.Optional;

@Component
public interface MemberDao {
    Optional<Member> findByEmail(final String email);

    Optional<Member> findById(long memberId);
}
