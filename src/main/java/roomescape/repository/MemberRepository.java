package roomescape.repository;

import roomescape.entity.Member;

import java.util.List;

public interface MemberRepository {

    List<Member> findAll();

    Member findById(long memberId);
}
