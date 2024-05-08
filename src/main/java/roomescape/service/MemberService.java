package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Email;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.repository.member.MemberRepository;

import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public boolean validateMember(LoginRequest loginRequest) {
        Optional<Member> memberOptional = memberRepository.findByEmail(new Email(loginRequest.email()));
        return memberOptional
                .map(member -> member.isMatchPassword(loginRequest.password()))
                .orElse(false);
    }
}
