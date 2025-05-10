package roomescape.reservation.fixture;

import org.springframework.stereotype.Component;
import roomescape.member.domain.Member;
import roomescape.member.infrastructure.repository.MemberRepository;

@Component
public class MemberDbFixture {

    private final MemberRepository memberRepository;

    public MemberDbFixture(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member 유저1_생성() {
        Member member = Member.signUpUser("유저1", "user1@email.com", "1234");
        return memberRepository.save(member);
    }
}
