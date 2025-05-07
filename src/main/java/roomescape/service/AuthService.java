package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.AuthenticatedUserResponse;
import roomescape.exception.LoginFailedException;

@Service
public class AuthService {

    private final JwtTokenProvider tokenProvider;
    private final MemberDao memberDao;


    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.tokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public String createToken(LoginRequest loginRequest) {
        Member member = memberDao.findByEmail(loginRequest.email())
                .orElseThrow(LoginFailedException::new);
        member.validatePassword(loginRequest.password());

        return tokenProvider.createToken(String.valueOf(member.getId()));
    }

    public AuthenticatedUserResponse getAuthenticatedUserFromToken(String token) {
        String subject = tokenProvider.extractSubject(token);
        Member member = memberDao.findById(Long.parseLong(subject))
                .orElseThrow();
        return new AuthenticatedUserResponse(member.getName());
    }
}
