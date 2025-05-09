package roomescape.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.member.controller.request.LoginRequest;
import roomescape.member.controller.request.SignUpRequest;
import roomescape.member.controller.response.MemberNameResponse;
import roomescape.member.controller.response.MemberResponse;
import roomescape.member.domain.Member;
import roomescape.member.infrastructure.jwt.JwtHandler;
import roomescape.member.infrastructure.repository.MemberRepository;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtHandler jwtHandler;

    public String login(LoginRequest request) {
        Member member = memberRepository.findByEmailAndPassword(request.email(), request.password())
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 가입되지 않은 회원입니다."));

        return jwtHandler.createToken(member.getId());
    }

    public MemberNameResponse check(String token) {
        Long memberId = jwtHandler.getMemberId(token);

        Member member = memberRepository.findById(memberId)
                .orElseThrow();

        return new MemberNameResponse(member.getName());
    }

    public MemberResponse signUp(SignUpRequest request) {
        Member member = Member.create(request.name(), request.email(), request.password());
        boolean exists = memberRepository.existsByEmail(member.getEmail());
        if (exists) {
            throw new IllegalArgumentException("[ERROR] 이미 가입된 이메일입니다.");
        }
        Member signed = memberRepository.save(member);
        return MemberResponse.from(signed);
    }
}
