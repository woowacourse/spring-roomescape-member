package roomescape.repository;

import org.springframework.stereotype.Component;
import roomescape.domain.member.Member;

import java.util.List;
import java.util.Optional;

@Component
public interface MemberDao {
    Optional<Member> findByEmailAndPassword(final String email, final String password);

    Optional<Member> findById(long memberId);

    List<Member> findAll();
}
