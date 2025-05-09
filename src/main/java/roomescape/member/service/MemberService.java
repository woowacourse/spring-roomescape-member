package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;
import roomescape.member.dto.SignupRequest;
import roomescape.member.dto.SignupResponse;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public SignupResponse createMember(SignupRequest request) {
        if (memberRepository.existByEmail(request.email())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다");
        }

        Member member = Member.createWithoutId(request.name(), request.email(), request.password());
        Long id = memberRepository.save(member);
        return SignupResponse.from(member.assignId(id));
    }
}
