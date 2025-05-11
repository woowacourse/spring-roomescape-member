package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.request.TokenRequest;
import roomescape.auth.dto.response.TokenResponse;
import roomescape.exception.ArgumentNullException;
import roomescape.member.Member;
import roomescape.member.dao.JdbcMemberDao;
import roomescape.member.infra.JwtProvider;

@Service
public class AuthService {

    private final JwtProvider jwtProvider;
    private final JdbcMemberDao jdbcMemberDao;

    public AuthService(JwtProvider jwtProvider, JdbcMemberDao jdbcMemberDao) {
        this.jwtProvider = jwtProvider;
        this.jdbcMemberDao = jdbcMemberDao;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        String token = jwtProvider.createToken(tokenRequest.email());
        return new TokenResponse(token);
    }

    public Member findMemberByToken(String token) {
        String payload = jwtProvider.getPayload(token);
        return jdbcMemberDao.findMember(payload)
                .orElseThrow(ArgumentNullException::new);
//        return new MemberResponse(member.getName());
    }
}
