package roomescape.member.application.service;

import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.member.application.TokenProvider;
import roomescape.member.application.repository.MemberRepository;
import roomescape.member.domain.Member;
import roomescape.member.presentation.dto.MemberResponse;
import roomescape.member.presentation.dto.SignUpRequest;
import roomescape.member.presentation.dto.SignUpResponse;
import roomescape.member.presentation.dto.TokenRequest;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public MemberService(MemberRepository memberRepository, TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }

    public String createToken(TokenRequest tokenRequest) {
        Member member = memberRepository.findByEmail(tokenRequest)
                .orElseThrow(() -> new NoSuchElementException("유저 정보를 찾을 수 없습니다."));

        return tokenProvider.createToken(member.getName());
    }

    public MemberResponse findByToken(String token) {
        String payload = tokenProvider.getPayload(token);
        return findMember(payload);
    }

    private MemberResponse findMember(String payload) {
        return new MemberResponse(payload);
    }

    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        return new SignUpResponse(memberRepository.insert(signUpRequest).getId());
    }
}
