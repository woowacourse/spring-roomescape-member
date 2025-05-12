package roomescape.repository;

import roomescape.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberDao {

    Optional<Member> findByEmailAndPassword(String email, String password);

    Optional<Member> findById(Long memberId);

    List<Member> findAll();
}
