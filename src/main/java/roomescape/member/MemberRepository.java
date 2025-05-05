package roomescape.member;

public interface MemberRepository {
    void saveMember(Member member);

    Boolean existsByEmail(String email);
}
