package roomescape.service.auth;

import org.springframework.stereotype.Service;
import roomescape.dao.member.MemberDao;
import roomescape.domain.member.MemberInfo;
import roomescape.dto.auth.LoginRequest;
import roomescape.dto.auth.TokenResponse;
import roomescape.infrastructure.TokenProvider;

@Service
public class AuthService {

    private final TokenProvider tokenProvider;
    private final MemberDao memberDao;

    public AuthService(TokenProvider tokenProvider, MemberDao memberDao) {
        this.tokenProvider = tokenProvider;
        this.memberDao = memberDao;
    }

    public TokenResponse createToken(LoginRequest loginRequest) {
        if (memberDao.isMemberNotExist(loginRequest.email(), loginRequest.password())) {
            throw new IllegalArgumentException("아이디, 비밀번호를 확인해주세요.");
        }
        String accessToken = tokenProvider.createToken(loginRequest.email());
        return new TokenResponse(accessToken);
    }

    public MemberInfo findMemberByToken(String token) {
        String payload = tokenProvider.getPayload(token);
        return findMember(payload);
    }

    private MemberInfo findMember(String principal) {
        return memberDao.findByEmail(principal)
                .orElseThrow(() -> new IllegalArgumentException("멤버가 존재하지 않습니다."));
    }

    public Boolean isAllowedMember(String token) {
        if (tokenProvider.isValidToken(token)) {
            String payload = tokenProvider.getPayload(token);
            return memberDao.isEmailExist(payload);
        }
        return false;
    }

    public Boolean isAdminMember(String token) {
        if (isAllowedMember(token)) {
            String payload = tokenProvider.getPayload(token);
            MemberInfo member = findMember(payload);
            return member.getRole().isAdmin();
        }
        return false;
    }
}
