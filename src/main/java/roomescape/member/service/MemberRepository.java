package roomescape.member.service;

import roomescape.member.domain.Member;

public interface MemberRepository {
    Member save(String name, String email, String password);
    boolean isExistUser(String email, String password);
    Member findUserByEmail(String payload);
    Member findMemberById(Long id);
    Member findMemberByName(String name);
}
