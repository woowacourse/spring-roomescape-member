package roomescape.infra;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import roomescape.dto.LoginInfo;
import roomescape.error.AccessDeniedException;

@Component
@RequiredArgsConstructor
public class SessionLoginRepository {

    public static final String LOGIN_INFO_KEY = "loginInfo";

    private final HttpSession session;

    public void setLoginInfo(final LoginInfo loginInfo) {
        session.setAttribute(LOGIN_INFO_KEY, loginInfo);
    }

    public LoginInfo getLoginInfo() {
        final Object value = session.getAttribute(LOGIN_INFO_KEY);
        if (value == null) {
            throw new AccessDeniedException("로그인 정보가 없습니다.");
        }
        return (LoginInfo) value;
    }

    public void clear() {
        session.removeAttribute(LOGIN_INFO_KEY);
    }
}
