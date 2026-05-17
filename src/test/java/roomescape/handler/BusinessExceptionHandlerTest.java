package roomescape.handler;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.format.DateTimeParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.exception.DuplicatedReservationException;
import roomescape.exception.EmptyNameException;
import roomescape.exception.PastDateReservationException;
import roomescape.exception.ReservationTimeInUseException;
import roomescape.exception.ReservationTimeNotFoundException;
import roomescape.exception.ThemeNotFoundException;

@WebMvcTest(BusinessExceptionHandler.class)
class BusinessExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TestController testController;

    @Nested
    class 예외를_404_Not_Found로_변환한다 {

        @DisplayName("ReservationTimeNotFoundException이 발생하면 404 Not Found로 변환하여 응답한다")
        @Test
        void ReservationTimeNotFoundException을_404로_변환한다() throws Exception {
            Mockito.doThrow(new ReservationTimeNotFoundException("test"))
                    .when(testController).throwException();

            mockMvc.perform(get("/exception-handling-test"))
                    .andExpect(status().isNotFound());
        }

        @DisplayName("ThemeNotFoundException이 발생하면 404 Not Found로 변환하여 응답한다")
        @Test
        void ThemeNotFoundException을_404로_변환한다() throws Exception {
            Mockito.doThrow(new ThemeNotFoundException("test"))
                    .when(testController).throwException();

            mockMvc.perform(get("/exception-handling-test"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class 예외를_409_Conflict로_변환한다 {

        @DisplayName("ReservationTimeInUseException이 발생하면 409 Conflict로 변환하여 응답한다")
        @Test
        void ReservationTimeInUseException을_409로_변환한다() throws Exception {
            Mockito.doThrow(new ReservationTimeInUseException("test"))
                    .when(testController).throwException();

            mockMvc.perform(get("/exception-handling-test"))
                    .andExpect(status().isConflict());
        }

        @DisplayName("DuplicatedReservationException이 발생하면 409 Conflict로 변환하여 응답한다")
        @Test
        void DuplicatedReservationException을_409로_변환한다() throws Exception {
            Mockito.doThrow(DuplicatedReservationException.class)
                    .when(testController).throwException();

            mockMvc.perform(get("/exception-handling-test"))
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    class 예외를_422_Unprocessable_Entity로_변환한다 {

        @DisplayName("PastDateReservationException이 발생하면 422 Unprocessable Entity로 변환하여 응답한다")
        @Test
        void PastDateReservationException을_422로_변환한다() throws Exception {
            Mockito.doThrow(new PastDateReservationException("test"))
                    .when(testController).throwException();

            mockMvc.perform(get("/exception-handling-test"))
                    .andExpect(status().isUnprocessableEntity());
        }

        @DisplayName("EmptyNameException이 발생하면 422 Unprocessable Entity로 변환하여 응답한다")
        @Test
        void EmptyNameException을_422로_변환한다() throws Exception {
            Mockito.doThrow(new EmptyNameException())
                    .when(testController).throwException();

            mockMvc.perform(get("/exception-handling-test"))
                    .andExpect(status().isUnprocessableEntity());
        }

        @DisplayName("DateTimeParseException이 발생하면 422 Unprocessable Entity로 변환하여 응답한다")
        @Test
        void DateTimeParseException을_422로_변환한다() throws Exception {
            Mockito.doThrow(new DateTimeParseException("test", "test", 0))
                    .when(testController).throwException();

            mockMvc.perform(get("/exception-handling-test"))
                    .andExpect(status().isUnprocessableEntity());
        }
    }
}
