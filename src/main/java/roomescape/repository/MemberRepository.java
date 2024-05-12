package roomescape.repository;

import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

import java.util.List;

@Repository
public interface MemberRepository {

    List<Member> findAll();

    Member findByEmail(String email);

    Member findById(Long id);

    Member save(Member member);

    int deleteById(Long id);
}
