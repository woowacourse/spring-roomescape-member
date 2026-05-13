package roomescape.reservation.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static roomescape.config.TestFixture.reservationRequestBody;
import static roomescape.config.TestFixture.reservationTimeRequest;
import static roomescape.config.TestFixture.themeRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.service.ReservationTimeService;
import roomescape.theme.entity.Theme;
import roomescape.theme.service.ThemeService;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ThemeService themeService;

    @Test
    void 예약을_추가한다() throws Exception {
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(LocalTime.of(10, 0)));
        Theme theme = themeService.save(themeRequest("테마"));
        Map<String, Object> request = reservationRequestBody(
                "밀란",
                LocalDate.of(2026, 5, 10),
                reservationTime.getId(),
                theme.getId()
        );

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/reservations/")))
                .andExpect(jsonPath("$.name").value("밀란"))
                .andExpect(jsonPath("$.date").value("2026-05-10"))
                .andExpect(jsonPath("$.time.id").value(reservationTime.getId()))
                .andExpect(jsonPath("$.theme.id").value(theme.getId()));
    }

    @Test
    void 예약_목록을_조회한다() throws Exception {
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(LocalTime.of(10, 0)));
        Theme theme = themeService.save(themeRequest("테마"));
        Map<String, Object> request = reservationRequestBody(
                "밀란",
                LocalDate.of(2026, 5, 10),
                reservationTime.getId(),
                theme.getId()
        );
        postReservation(request);

        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", hasItem("밀란")))
                .andExpect(jsonPath("$[*].date", hasItem("2026-05-10")))
                .andExpect(jsonPath("$[*].time.id", hasItem(reservationTime.getId().intValue())))
                .andExpect(jsonPath("$[*].theme.runtime", hasItem(60)));
    }

    @Test
    void 예약을_삭제한다() throws Exception {
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(LocalTime.of(10, 0)));
        Theme theme = themeService.save(themeRequest("테마"));
        Map<String, Object> request = reservationRequestBody(
                "밀란",
                LocalDate.of(2026, 5, 10),
                reservationTime.getId(),
                theme.getId()
        );
        int id = postReservation(request);

        mockMvc.perform(delete("/reservations/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    void 존재하지_않는_예약을_삭제하면_404를_응답한다() throws Exception {
        mockMvc.perform(delete("/reservations/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void 날짜_시간_테마가_같은_예약을_등록요청하면_409를_응답한다() throws Exception {
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(LocalTime.of(10, 0)));
        Theme theme = themeService.save(themeRequest("테마"));
        Map<String, Object> request = reservationRequestBody(
                "밀란",
                LocalDate.of(2026, 5, 10),
                reservationTime.getId(),
                theme.getId()
        );
        postReservation(request);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @ParameterizedTest(name = "{0}은 올바른 예약자 이름이 아니다")
    @CsvSource(value = {"'':예약자 이름은 필수입니다.", "12345678901:예약자 이름은 10자 이하입니다."}, delimiter = ':')
    void 예약을_추가할_때_이름이_올바르지_않으면_400과_예외_메시지를_응답한다(
            String name,
            String expectedMessage
    ) throws Exception {
        Map<String, Object> request = reservationRequestBody(name, LocalDate.of(2023, 8, 5), 1L, 1L);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(containsString(expectedMessage)));
    }

    private int postReservation(Map<String, Object> request) throws Exception {
        MvcResult result = mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString())
                .get("id")
                .asInt();
    }

}
