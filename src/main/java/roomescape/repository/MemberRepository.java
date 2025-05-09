package roomescape.repository;

import java.util.List;
import roomescape.entity.Member;

public interface MemberRepository {

    Member findById(Long id);

    List<Member> findAll();

    Member save(Member member);

    void deleteById(Long id);

    boolean existsByName(String name);
}
