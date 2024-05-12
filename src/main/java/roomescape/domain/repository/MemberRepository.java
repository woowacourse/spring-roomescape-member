package roomescape.domain.repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import roomescape.domain.Member;

public interface MemberRepository {

    List<Member> findAll();

    Optional<Member> findById(Long id);

    default Member getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 id의 사용자가 존재하지 않습니다."));
    }

    Member save(Member member);

    void deleteById(Long id);

    boolean existsById(Long id);

    boolean existsByEmail(String email);

    Optional<Member> findByEmailAndPassword(String email, String password);
}
