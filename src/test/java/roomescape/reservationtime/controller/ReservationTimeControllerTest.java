package roomescape.reservationtime.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static roomescape.config.TestFixture.reservationTimeRequest;

import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservationtime.service.ReservationTimeService;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class ReservationTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Test
    void 예약_시간_목록을_조회한다() throws Exception {
        reservationTimeService.save(reservationTimeRequest(LocalTime.of(10, 0)));

        mockMvc.perform(get("/times"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].startAt", hasItem("10:00:00")));
    }

}
