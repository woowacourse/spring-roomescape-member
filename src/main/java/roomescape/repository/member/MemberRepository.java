package roomescape.repository.member;

import java.util.List;
import java.util.Optional;
import roomescape.domain.member.Email;
import roomescape.domain.member.Member;
import roomescape.domain.member.Password;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findById(Long id);

    Optional<Member> findByEmailAndPassword(Email email, Password password);

    List<Member> findAll();

    void delete(Long id);

    boolean doesEmailExist(Email email);
}
