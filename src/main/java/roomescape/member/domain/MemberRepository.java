package roomescape.member.domain;

public interface MemberRepository {

    Member findById(Long id);

    Member findByEmailAndPassword(String email, String password);
}
