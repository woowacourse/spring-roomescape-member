package roomescape.web.exception;


import static org.mockito.BDDMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.NoSuchElementException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.service.auth.AuthService;
import roomescape.service.auth.UnauthorizedException;
import roomescape.web.security.CookieTokenExtractor;

@WebMvcTest(GlobalExceptionHandlingTest.TestHandler.class)
class GlobalExceptionHandlingTest {

    @MockBean
    TestHandler target;

    @MockBean
    AuthService authService;

    @MockBean
    CookieTokenExtractor extractor;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("존재하지 않는 경로 요청을 처리한다.")
    void unknownPath() throws Exception {
        mockMvc.perform(get("/unknown"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("존재하지 않는 요청 경로입니다."));
    }

    @Test
    @DisplayName("존재하지 않는 HTTP 메서드 요청을 처리한다.")
    void unknownMethod() throws Exception {
        mockMvc.perform(delete("/"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.message").value("지원하지 않는 HTTP 메서드입니다."));
    }

    @Test
    @DisplayName("읽을 수 없는 HTTP 메시지를 처리한다.")
    void notReadable() throws Exception {
        mockMvc.perform(
                        post("/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString("}{"))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("읽을 수 없는 HTTP 메세지입니다."));
    }

    @ParameterizedTest
    @MethodSource("exceptions")
    @DisplayName("표준 예외를 처리한다.")
    void handleJvmException(RuntimeException exceptionSource) throws Exception {
        when(target.get()).thenThrow(exceptionSource);

        mockMvc.perform(get("/"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details").isEmpty());
    }

    @Test
    @DisplayName("인가 예외를 처리한다.")
    void unauthorized() throws Exception {
        when(target.get()).thenThrow(new UnauthorizedException());

        mockMvc.perform(get("/"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("유효한 인가 정보를 입력해주세요."));
    }

    @Test
    @DisplayName("예기치 못한 예외를 처리한다.")
    void unExpectedException() throws Exception {
        when(target.get()).thenThrow(new UnexpectedException());

        mockMvc.perform(get("/"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("서버 오류입니다."));
    }

    private static List<RuntimeException> exceptions() {
        return List.of(
                new IllegalArgumentException(),
                new IllegalStateException(),
                new NoSuchElementException()
        );
    }

    private static class UnexpectedException extends RuntimeException {
    }

    @Controller
    static class TestHandler {

        @GetMapping
        ResponseEntity<String> get() {
            return ResponseEntity.ok("sample");
        }

        @PostMapping
        ResponseEntity<Void> post(@RequestBody TestRequest request) {
            return ResponseEntity.noContent().build();
        }

        record TestRequest(String name) {
        }
    }
}
