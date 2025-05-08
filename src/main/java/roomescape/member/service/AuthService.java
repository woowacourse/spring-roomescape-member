package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.exception.ArgumentNullException;
import roomescape.member.Member;
import roomescape.member.dao.JdbcMemberDao;
import roomescape.member.dto.request.TokenRequest;
import roomescape.member.dto.response.MemberResponse;
import roomescape.member.dto.response.TokenResponse;
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

    public MemberResponse findMemberByToken(String token) {
        String payload = jwtProvider.getPayload(token);
        Member member = jdbcMemberDao.findMember(payload)
                .orElseThrow(ArgumentNullException::new);
        return new MemberResponse(member.getName());
    }
}
