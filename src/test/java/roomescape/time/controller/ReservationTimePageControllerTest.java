package roomescape.time.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.reservation.service.ReservationService;

@WebMvcTest(ReservationTimePageController.class)
class ReservationTimePageControllerTest {

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("/time 을 요청하면 time.html 를 반환한다.")
    void requestTime() throws Exception {
        mockMvc.perform(get("/time"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/time"));
    }
}
