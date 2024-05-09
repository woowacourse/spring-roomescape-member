package roomescape.controller;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.controller.api.ReservationApiController;
import roomescape.controller.api.dto.request.LoginMemberRequest;
import roomescape.controller.api.dto.request.ReservationRequest;
import roomescape.exception.AuthorizationException;
import roomescape.service.MemberService;
import roomescape.service.dto.input.MemberCreateInput;
import roomescape.service.dto.input.MemberLoginInput;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class LoginMemberArgumentResolverTest {
    @Autowired
    private LoginMemberArgumentResolver loginMemberArgumentResolver;
    @Autowired
    private MemberService memberService;

    private MethodParameter methodParameter;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        final Method method = ReservationApiController.class.getMethod("createReservation", ReservationRequest.class, LoginMemberRequest.class);
        this.methodParameter = new MethodParameter(method, 1);
    }

    @Test
    @DisplayName("특정 메소드의 매개변수 타입에 대해서 처리할 수 있으면 참을 반환한다.")
    void true_if_can_handle_parameter_type() {
        assertThat(loginMemberArgumentResolver.supportsParameter(methodParameter)).isTrue();
    }

    @Test
    @DisplayName("토큰을 통해서 사용자 정보로 변환한다.")
    void resolve_token_to_loginMember_when_member_exist() throws Exception {
        final var createOutput = memberService.createMember(new MemberCreateInput("조이썬", "joyson5582@gmail.com", "password"));
        final var output = memberService.loginMember(new MemberLoginInput("joyson5582@gmail.com", "password"));


        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setCookies(new Cookie("token", output.token()));

        final NativeWebRequest nativeWebRequest = new ServletWebRequest(mockRequest);
        final LoginMemberRequest member = (LoginMemberRequest) loginMemberArgumentResolver.resolveArgument(methodParameter, new ModelAndViewContainer(), nativeWebRequest, null);
        final LoginMemberRequest loginMemberRequest = new LoginMemberRequest(createOutput.id(),"joyson5582@gmail.com", "password", "조이썬");
        assertThat(member).isEqualTo(loginMemberRequest);
    }

    @Test
    @DisplayName("토큰이 없으면 예외를 발생한다.")
    void throw_exception_when_token_not_exist() {
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        final NativeWebRequest nativeWebRequest = new ServletWebRequest(mockRequest);
        final ModelAndViewContainer container = new ModelAndViewContainer();
        assertThatThrownBy(() -> loginMemberArgumentResolver.resolveArgument(methodParameter, container, nativeWebRequest, null))
                .isInstanceOf(AuthorizationException.class);
    }
}
