package roomescape.member.domain;

public interface MemberRepository {
    Long save(Member member);

    Member findById(Long id);

    boolean existByEmail(String email);
}
