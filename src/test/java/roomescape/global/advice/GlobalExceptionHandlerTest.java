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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.annotation.GetMapping;
import roomescape.global.advice.GlobalExceptionHandlerTest.TestController;
import roomescape.support.SimpleMockMvc;

@WebMvcTest(TestController.class)
class GlobalExceptionHandlerTest {
    @MockBean
    private TestController testController;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void IllegalArgumentException() throws Exception {
        String message = "잘못된 요청입니다.";
        doThrow(new IllegalArgumentException(message)).when(testController).invoke();

        ResultActions resultActions = SimpleMockMvc.get(mockMvc, "/exception");

        resultActions.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").value(message),
                result -> assertThat(result.getResolvedException()).isExactlyInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    void DuplicateKeyException() throws Exception {
        doThrow(new DuplicateKeyException("")).when(testController).invoke();

        ResultActions resultActions = SimpleMockMvc.get(mockMvc, "/exception");

        resultActions.andExpectAll(
                status().isConflict(),
                jsonPath("$.message").value("이미 존재하는 데이터입니다."),
                result -> assertThat(result.getResolvedException()).isExactlyInstanceOf(DuplicateKeyException.class)
        );
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(classes = {NullPointerException.class, IllegalStateException.class, RuntimeException.class})
    void Exception(Class<? extends Exception> exception) throws Exception {
        doThrow(exception).when(testController).invoke();

        ResultActions resultActions = SimpleMockMvc.get(mockMvc, "/exception");

        resultActions.andExpectAll(
                status().isInternalServerError(),
                jsonPath("$.message").value("서버 에러입니다. 관리자에게 문의하세요."),
                result -> assertThat(result.getResolvedException()).isExactlyInstanceOf(exception)
        );
    }

    @Controller
    static class TestController {
        @GetMapping("/exception")
        public void invoke() {
        }
    }
}
