package roomescape.domain;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import roomescape.dto.LoginInfo;
import roomescape.error.AccessDeniedException;

@Component
public class LoginSession {  // NOTE. 유틸 클래스는 테스트하기 어렵기에 Bean으로 등록함. 지노에게 질문하기

    public static final String LOGIN_INFO_KEY = "loginInfo";

    public void setLoginInfo(final HttpSession session, final LoginInfo loginInfo) {
        session.setAttribute(LOGIN_INFO_KEY, loginInfo);
    }

    public LoginInfo getLoginInfo(final HttpSession session) {
        final Object value = session.getAttribute(LOGIN_INFO_KEY);
        if (value == null) {
            throw new AccessDeniedException("로그인 정보가 없습니다.");
        }
        return (LoginInfo) value;
    }

    public void clear(final HttpSession session) {
        session.removeAttribute(LOGIN_INFO_KEY);
    }
}
