package roomescape.member;

public interface MemberRepository {
    void saveMember(Member member);

    Member findByEmail(String email);

    Boolean existsByEmail(String email);
}
