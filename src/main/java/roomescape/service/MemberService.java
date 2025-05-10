package roomescape.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.dao.meber.MemberDao;
import roomescape.domain.Member;
import roomescape.dto.request.TokenRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.dto.response.TokenResponse;
import roomescape.support.auth.JwtTokenProvider;

@Service
public class MemberService {

    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse create(final TokenRequest tokenRequest) {
        final String accessToken = jwtTokenProvider.createToken(tokenRequest.email());
        return new TokenResponse(accessToken);
    }

    public MemberResponse findMemberByToken(final String token) {
        final String email = jwtTokenProvider.getPayload(token);
        final Optional<Member> member = memberDao.findByEmail(email);
        if (member.isEmpty()) {
            throw new NoSuchElementException("멤버 정보를 찾을 수 없습니다.");
        }
        return new MemberResponse(member.get().getName());
    }
}
