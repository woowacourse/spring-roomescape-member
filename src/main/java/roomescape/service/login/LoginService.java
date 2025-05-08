package roomescape.service.login;

import jakarta.servlet.http.Cookie;
import roomescape.dto.login.TokenRequest;
import roomescape.dto.member.MemberResponse;
import roomescape.entity.MemberEntity;

public interface LoginService {

    MemberEntity findMember(TokenRequest tokenRequest);
    String createToken(TokenRequest tokenRequest);
    MemberResponse findMemberByToken(Cookie[] cookies);
}
