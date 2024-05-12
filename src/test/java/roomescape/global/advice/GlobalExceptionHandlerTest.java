package roomescape.global.advice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.exception.AccessDeniedException;
import roomescape.auth.exception.AuthorizationException;
import roomescape.global.advice.GlobalExceptionHandlerTest.TestController;
import roomescape.support.SimpleMockMvc;

@WebMvcTest(
        controllers = TestController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class)
)
class GlobalExceptionHandlerTest {
    private static final String URI = "/exception";

    @MockBean
    private TestController testController;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void IllegalArgumentException() throws Exception {
        String message = "잘못된 요청입니다.";
        doThrow(new IllegalArgumentException(message)).when(testController).invoke();

        ResultActions resultActions = SimpleMockMvc.get(mockMvc, URI);

        resultActions.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").value(message),
                result -> assertThat(result.getResolvedException()).isExactlyInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    void DuplicateKeyException() throws Exception {
        doThrow(new DuplicateKeyException("")).when(testController).invoke();

        ResultActions resultActions = SimpleMockMvc.get(mockMvc, URI);

        resultActions.andExpectAll(
                status().isConflict(),
                jsonPath("$.message").value("이미 존재하는 데이터입니다."),
                result -> assertThat(result.getResolvedException()).isExactlyInstanceOf(DuplicateKeyException.class)
        );
    }

    @Test
    void AuthorizationException() throws Exception {
        String message = "인증 에러입니다.";
        doThrow(new AuthorizationException(message)).when(testController).invoke();

        ResultActions resultActions = SimpleMockMvc.get(mockMvc, URI);

        resultActions.andExpectAll(
                status().isUnauthorized(),
                jsonPath("$.message").value(message),
                result -> assertThat(result.getResolvedException()).isExactlyInstanceOf(AuthorizationException.class)
        );
    }

    @Test
    void AccessDeniedException() throws Exception {
        doThrow(new AccessDeniedException()).when(testController).invoke();

        ResultActions resultActions = SimpleMockMvc.get(mockMvc, URI);

        resultActions.andExpectAll(
                status().isForbidden(),
                jsonPath("$.message").value("잘못된 접근입니다."),
                result -> assertThat(result.getResolvedException()).isExactlyInstanceOf(AccessDeniedException.class)
        );
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(classes = {NullPointerException.class, IllegalStateException.class, RuntimeException.class})
    void Exception(Class<? extends Exception> exception) throws Exception {
        doThrow(exception).when(testController).invoke();

        ResultActions resultActions = SimpleMockMvc.get(mockMvc, URI);

        resultActions.andExpectAll(
                status().isInternalServerError(),
                jsonPath("$.message").value("서버 에러입니다. 관리자에게 문의하세요."),
                result -> assertThat(result.getResolvedException()).isExactlyInstanceOf(exception)
        );
    }

    @Controller
    static class TestController {
        @GetMapping(URI)
        public void invoke() {
        }
    }
}
