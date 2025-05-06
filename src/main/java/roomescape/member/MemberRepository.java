package roomescape.member;

public interface MemberRepository {
    void saveMember(Member member);

    Member findByEmail(String email);

    Boolean existsById(Long id);
    Boolean existsByEmail(String email);
}
