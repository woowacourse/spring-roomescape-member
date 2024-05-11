package roomescape.application;


import org.springframework.stereotype.Service;
import roomescape.application.dto.MemberResponse;
import roomescape.application.dto.TokenRequest;
import roomescape.application.dto.TokenResponse;
import roomescape.domain.Member;
import roomescape.domain.MemberQueryRepository;
import roomescape.domain.TokenProvider;
import roomescape.exception.RoomescapeErrorCode;
import roomescape.exception.RoomescapeException;

@Service
public class AuthService {
    private final TokenProvider tokenProvider;
    private final MemberQueryRepository memberQueryRepository;

    public AuthService(TokenProvider tokenProvider, MemberQueryRepository memberQueryRepository) {
        this.tokenProvider = tokenProvider;
        this.memberQueryRepository = memberQueryRepository;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        String email = tokenRequest.email();
        String accessToken = tokenProvider.createToken(email);
        Member member = memberQueryRepository.findByEmail(email)
                .orElseThrow(() -> new RoomescapeException(RoomescapeErrorCode.NOT_FOUND_MEMBER));
        if (!member.getPassword().equals(tokenRequest.password())) {
            throw new RoomescapeException(RoomescapeErrorCode.BAD_REQUEST, "로그인 회원 정보가 일치하지 않습니다.");
        }
        return new TokenResponse(accessToken);
    }

    public MemberResponse findMemberByToken(String token) {
        String payload = tokenProvider.getPayload(token);
        return MemberResponse.from(memberQueryRepository.findByEmail(payload)
                .orElseThrow(() -> new RoomescapeException(RoomescapeErrorCode.NOT_FOUND_MEMBER)));
    }
}
