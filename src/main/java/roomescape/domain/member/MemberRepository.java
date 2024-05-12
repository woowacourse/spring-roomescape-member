package roomescape.domain.member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);

    default Member getByEmail(String email) {
        return findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않은 멤버입니다. email: %s", email)));
    }

    default Member getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않은 멤버입니다. id: %d", id)));
    }

    List<Member> findAll();

    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);
}
