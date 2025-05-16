package roomescape.resolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import roomescape.domain.entity.Role;
import roomescape.dto.LoginInfo;
import roomescape.infra.SessionLoginRepository;

class LoginInfoArgumentResolverTest {

    private final SessionLoginRepository sessionLoginRepository = mock(SessionLoginRepository.class);
    private final LoginInfoArgumentResolver sut = new LoginInfoArgumentResolver(sessionLoginRepository);

    @DisplayName("LoginRequired 어노테이션과 타입 조건에 따라 supportsParameter 동작을 확인한다")
    @ParameterizedTest
    @MethodSource("provideMethodParameters")
    void supportsParameter(Method method, int parameterIndex, boolean expected) {
        // given
        var parameter = new MethodParameter(method, parameterIndex);

        // when
        var result = sut.supportsParameter(parameter);

        // then
        assertThat(result).isEqualTo(expected);
    }

    static Stream<Arguments> provideMethodParameters() throws NoSuchMethodException {
        return Stream.of(
                Arguments.of(TestController.class.getMethod("handle", LoginInfo.class), 0, true),
                Arguments.of(TestController.class.getMethod("handleNoAnnotation", String.class), 0, false)
        );
    }

    static class TestController {

        public void handle(@LoginRequired LoginInfo loginInfo) {
        }

        public void handleNoAnnotation(String name) {
        }
    }

    @DisplayName("세션이 존재하면 loginSession을 통해 LoginInfo를 반환한다")
    @Test
    void resolveArgument() {
        // given
        var request = new MockHttpServletRequest();
        var webRequest = new ServletWebRequest(request);

        var expectedLoginInfo = new LoginInfo(1L, "홍길동", Role.USER);
        when(sessionLoginRepository.getLoginInfo()).thenReturn(expectedLoginInfo);

        // when
        var result = (LoginInfo) sut.resolveArgument(null, null, webRequest, null);

        // then
        assertThat(result).isEqualTo(expectedLoginInfo);
    }
}
