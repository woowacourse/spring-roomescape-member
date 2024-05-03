package roomescape.presentation.advice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.mock.web.MockHttpServletResponse;
import roomescape.presentation.AdminController;
import roomescape.presentation.ControllerTest;

@WebMvcTest(AdminController.class)
class GlobalExceptionHandlerTest extends ControllerTest {

    @MockBean
    private AdminController controller;

    @DisplayName("ErrorResponse가 처리할 수 있는 예외의 경우, 적절한 상태코드로 응답한다.")
    @Test
    void shouldHandleException() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/admin/reservation"))
                .andExpect(status().isMethodNotAllowed())
                .andReturn()
                .getResponse();
        ProblemDetail problemDetail = objectMapper.readValue(response.getContentAsString(), ProblemDetail.class);
        assertThat(problemDetail.getStatus()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED.value());
    }

    @DisplayName("ErrorResponse가 처리할 수 없는 예외의 경우, 500 서버 오류를 응답한다.")
    @Test
    void shouldHandleExceptionWithInternalError() throws Exception {
        when(controller.reservationPage()).thenThrow(RuntimeException.class);
        MockHttpServletResponse response = mvc.perform(get("/admin/reservation"))
                .andExpect(status().isInternalServerError())
                .andReturn()
                .getResponse();
        ProblemDetail problemDetail = objectMapper.readValue(response.getContentAsString(), ProblemDetail.class);
        int statusCode = problemDetail.getStatus();
        assertThat(statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
