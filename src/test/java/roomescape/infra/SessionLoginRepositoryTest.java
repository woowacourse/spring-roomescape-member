package roomescape.infra;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import roomescape.domain.entity.Role;
import roomescape.dto.LoginInfo;
import roomescape.error.AccessDeniedException;

class SessionLoginRepositoryTest {

    private final LoginInfo loginInfo = new LoginInfo(1L, "홍길동", Role.USER);
    private final HttpSession session = new MockHttpSession();

    private final SessionLoginRepository sut = new SessionLoginRepository(session);

    @DisplayName("세션에 로그인 정보를 저장한다")
    @Test
    void setLoginInfo() {
        // when
        sut.setLoginInfo(loginInfo);
        var found = sut.getLoginInfo();

        // then
        assertSoftly(soft -> {
            soft.assertThat(found.id()).isEqualTo(loginInfo.id());
            soft.assertThat(found.name()).isEqualTo(loginInfo.name());
            soft.assertThat(found.role()).isEqualTo(loginInfo.role());
        });
    }

    @DisplayName("세션에서 로그인 정보를 조회한다")
    @Test
    void getLoginInfo() {
        // given
        session.setAttribute(SessionLoginRepository.LOGIN_INFO_KEY, loginInfo);

        // when
        var found = sut.getLoginInfo();

        // then
        assertSoftly(soft -> {
            soft.assertThat(found.id()).isEqualTo(loginInfo.id());
            soft.assertThat(found.name()).isEqualTo(loginInfo.name());
            soft.assertThat(found.role()).isEqualTo(loginInfo.role());
        });
    }

    @DisplayName("세션에 로그인 정보가 없으면 예외가 발생한다")
    @Test
    void getLoginInfo_notLoggedIn() {
        // when // then
        assertThatThrownBy(() -> sut.getLoginInfo())
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("로그인 정보가 없습니다.");
    }

    @DisplayName("세션에서 로그인 정보를 제거한다")
    @Test
    void clear() {
        // given
        session.setAttribute(SessionLoginRepository.LOGIN_INFO_KEY, loginInfo);

        // when
        sut.clear();

        // then
        assertThatThrownBy(sut::getLoginInfo)
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("로그인 정보가 없습니다.");
    }
}
