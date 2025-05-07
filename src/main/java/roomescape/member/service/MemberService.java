package roomescape.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.member.controller.request.LoginRequest;
import roomescape.member.controller.response.MemberNameResponse;
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
                .orElseGet(() -> {
                    Member created = Member.create("name", request.email(), request.password());
                    return memberRepository.save(created);
                });

        return jwtHandler.createToken(member.getId());
    }

    public MemberNameResponse check(String token) {
        Long memberId = jwtHandler.getMemberId(token);

        Member member = memberRepository.findById(memberId)
                .orElseThrow();

        return new MemberNameResponse(member.getName());
    }
}
