package roomescape.repository;

import roomescape.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    List<Member> findAll();

    Optional<Member> findByEmail(String email); //TODO Long 다 long으로 변경하기

    Member fetchByEmail(String email);

    Member save(Member member);

    void delete(String email);

}
