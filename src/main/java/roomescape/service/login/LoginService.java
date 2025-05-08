package roomescape.service.login;

import jakarta.servlet.http.Cookie;
import roomescape.domain.Member;
import roomescape.dto.login.TokenRequest;
import roomescape.dto.member.MemberResponse;

public interface LoginService {

    Member findMember(TokenRequest tokenRequest);
    String createToken(TokenRequest tokenRequest);
    MemberResponse findMemberByToken(Cookie[] cookies);
}
