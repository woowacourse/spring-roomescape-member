package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.member.dto.SignUpRequest;
import roomescape.member.dto.SignUpResponse;
import roomescape.member.entity.Member;
import roomescape.member.entity.Role;
import roomescape.member.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public SignUpResponse registerUser(SignUpRequest request) {
        Member member = Member.withoutId(request.getEmail(), request.getPassword(), request.getName(), Role.USER);
        Member registeredUser = memberRepository.save(member);
        return SignUpResponse.from(registeredUser);
    }
}
