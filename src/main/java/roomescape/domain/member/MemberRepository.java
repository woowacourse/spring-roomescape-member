package roomescape.domain.member;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findById(long id);

    default Member getById(long id) {
        return findById(id).orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));
    }

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    default Member getByEmail(String email) {
        return findByEmail(email).orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));
    }
}
