package roomescape.repository.dao;

import roomescape.model.Member;

import java.util.Optional;

public interface MemberDao {

    Optional<Member> findByEmail(String email);
}
