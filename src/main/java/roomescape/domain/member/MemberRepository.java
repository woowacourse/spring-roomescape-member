package roomescape.domain.member;

public interface MemberRepository {

    Member save(Member member);

    boolean existsByEmail(String email);
}
