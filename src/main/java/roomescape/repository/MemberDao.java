package roomescape.repository;

import roomescape.entity.LoginMember;

import java.util.List;
import java.util.Optional;

public interface MemberDao {

    Optional<LoginMember> findByEmailAndPassword(String email, String password);

    Optional<LoginMember> findById(Long memberId);

    List<LoginMember> findAll();
}
