package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.exception.DuplicatedException;
import roomescape.repository.member.MemberRepository;
import roomescape.service.dto.member.MemberResponse;
import roomescape.service.dto.member.MemberSignUpRequest;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse signUp(MemberSignUpRequest request) {
        if (memberRepository.existsByEmail(request.email())) {
            throw new DuplicatedException("이미 가입되어 있는 이메일 주소입니다.");
        }
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.from(member);
    }
}
