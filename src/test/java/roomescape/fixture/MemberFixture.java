package roomescape.fixture;

import roomescape.auth.domain.AuthRole;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;

public class MemberFixture {

    public static final Member NOT_SAVED_MEMBER_1 = new Member("헤일러", "he@iler.com", "비밀번호", AuthRole.MEMBER);
    public static final Member NOT_SAVED_MEMBER_2 = new Member("머피", "mu@ffy.com", "비밀번호", AuthRole.MEMBER);
    public static final Member NOT_SAVED_MEMBER_3 = new Member("매트", "ma@t.com", "비밀번호", AuthRole.MEMBER);

    public static Member getSavedMember1(final MemberRepository memberRepository) {
        final Long id = memberRepository.save(NOT_SAVED_MEMBER_1);
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원 조회에 실패했습니다."));
    }

    public static Member getSavedMember2(final MemberRepository memberRepository) {
        final Long id = memberRepository.save(NOT_SAVED_MEMBER_2);
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원 조회에 실패했습니다."));
    }

    public static Member getSavedMember3(final MemberRepository memberRepository) {
        final Long id = memberRepository.save(NOT_SAVED_MEMBER_3);
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원 조회에 실패했습니다."));
    }
}
