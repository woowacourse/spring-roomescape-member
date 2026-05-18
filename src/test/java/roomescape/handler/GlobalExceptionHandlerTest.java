package roomescape.handler;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TestController testController;

    @DisplayName("IllegalArgumentException이 발생하면 400 Bad Request로 변환하여 응답한다")
    @Test
    void illegalArgumentException을_400으로_변환한다() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("Invalid data"))
                .when(testController).throwException();

        mockMvc.perform(get("/exception-handling-test"))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("처리되지 않은 예외가 발생하면 500 Internal Server Error로 변환하여 응답한다")
    @Test
    void unhandledException을_500으로_변환한다() throws Exception {
        Mockito.doThrow(new RuntimeException("Unexpected error"))
                .when(testController).throwException();

        mockMvc.perform(get("/exception-handling-test"))
                .andExpect(status().isInternalServerError());
    }
}
