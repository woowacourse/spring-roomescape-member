package roomescape.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import roomescape.domain.Member;
import roomescape.domain.repository.MemberRepository;
import roomescape.exception.member.AuthenticationFailureException;
import roomescape.service.security.JwtUtils;
import roomescape.web.dto.request.LoginRequest;
import roomescape.web.dto.response.MemberResponse;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public String login(LoginRequest loginRequest) {
        Member findMember = memberRepository.findByEmailAndPassword(loginRequest.email(), loginRequest.password())
                .orElseThrow(AuthenticationFailureException::new);

        return "token=" + JwtUtils.encode(findMember);
    }

    public MemberResponse findMemberByToken(String token) {
        Long decodedId = JwtUtils.decodeId(token);
        Member findMember = memberRepository.findById(decodedId)
                .orElseThrow(AuthenticationFailureException::new);
        return new MemberResponse(findMember.getName());
    }
}
