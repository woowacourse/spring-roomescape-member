package roomescape.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.member.MemberDao;
import roomescape.domain.Member;
import roomescape.dto.request.TokenRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.dto.response.TokenResponse;
import roomescape.support.auth.JwtTokenProvider;

@Service
public class MemberLoginService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public MemberLoginService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    @Transactional(readOnly = true)
    public MemberResponse findByToken(final String token) {
        final String email = jwtTokenProvider.getPayload(token);
        final Optional<Member> member = memberDao.findByEmail(email);
        if (member.isEmpty()) {
            throw new NoSuchElementException("멤버 정보를 찾을 수 없습니다.");
        }
        return MemberResponse.from(member.get());
    }

    public TokenResponse createToken(final TokenRequest tokenRequest) {
        final String accessToken = jwtTokenProvider.createToken(tokenRequest.email());
        return new TokenResponse(accessToken);
    }

}
