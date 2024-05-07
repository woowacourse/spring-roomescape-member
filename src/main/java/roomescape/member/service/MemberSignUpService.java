package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.dto.MemberSignUpRequest;
import roomescape.member.repository.MemberRepository;

@Service
public class MemberSignUpService {

    private final MemberRepository memberRepository;

    public MemberSignUpService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long save(MemberSignUpRequest memberSignUpRequest) {
        Member member = memberSignUpRequest.toMember();
        if (memberRepository.existNameOrEmail(member)) {
            throw new IllegalArgumentException("중복된 이름 또는 이메일 입니다.");
        }

        return memberRepository.save(member);
    }
}
