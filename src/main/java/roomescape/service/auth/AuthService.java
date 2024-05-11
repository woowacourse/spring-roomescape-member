package roomescape.service.auth;

import org.springframework.stereotype.Service;
import roomescape.controller.login.TokenRequest;
import roomescape.controller.login.TokenResponse;
import roomescape.controller.member.MemberResponse;
import roomescape.domain.Member;
import roomescape.repository.MemberRepository;
import roomescape.service.auth.exception.InvalidTokenException;
import roomescape.service.auth.exception.MemberNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public List<MemberResponse> getMembers() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .toList();
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("존재 하지 않는 로그인 정보 입니다."));
    }

    public Member getMemberByToken(String token) {
        String payload = jwtTokenProvider.getPayloadOrElseThrow(
                token,
                () -> new InvalidTokenException("유효하지 않은 토큰입니다.")
        );

        return getMemberByEmail(payload);
    }

    public TokenResponse createToken(TokenRequest request) {
        validateInformation(request);
        String accessToken = jwtTokenProvider.createToken(request.email());

        return new TokenResponse(accessToken);
    }

    private void validateInformation(TokenRequest request) {
        Optional<Member> member = memberRepository.findByEmail(request.email());
        if (member.isEmpty() || !member.get().getPassword().equals(request.password())) {
            throw new MemberNotFoundException("로그인 정보가 일치하지 않습니다.");
        }
    }
}
