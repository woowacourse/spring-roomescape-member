package roomescape.member.repository;

import java.util.List;
import java.util.Optional;
import roomescape.member.domain.Email;
import roomescape.member.domain.Member;
import roomescape.member.domain.Password;

public interface MemberRepository {

    Optional<Member> findById(Long id);

    Optional<Member> findByEmailAndPassword(Email email, Password password);

    List<Member> findAll();

    void delete(Long id);

    boolean doesEmailExist(Email email);
}
