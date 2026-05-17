package roomescape.reservation.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static roomescape.config.TestFixture.reservationRequestBody;
import static roomescape.config.TestFixture.reservationTimeRequest;
import static roomescape.config.TestFixture.reservationUpdateRequestBody;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.AccessDeniedException;
import roomescape.common.exception.DomainType;
import roomescape.common.exception.DuplicatedException;
import roomescape.common.exception.NotFoundException;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.service.ReservationTimeService;
import roomescape.theme.entity.Theme;
import roomescape.theme.service.ThemeService;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class ReservationControllerTest {

    private static final String RESERVATION_NAME = "봉구스";
    private static final String OTHER_RESERVATION_NAME = "밀란";
    private static final String THEME_NAME = "테마";
    private static final LocalDate DEFAULT_RESERVATION_DATE = LocalDate.of(2026, 5, 10);
    private static final LocalDate NEXT_RESERVATION_DATE = LocalDate.of(2026, 5, 11);
    private static final LocalDate INVALID_RESERVATION_DATE = LocalDate.of(2023, 8, 5);
    private static final LocalTime DEFAULT_START_AT = LocalTime.of(10, 0);
    private static final LocalTime UPDATED_START_AT = LocalTime.of(11, 0);
    private static final int NOT_FOUND_ID = 999;

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
        // given
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(DEFAULT_START_AT));
        Theme theme = themeService.save(themeRequest(THEME_NAME));
        Map<String, Object> request = reservationRequestBody(
                RESERVATION_NAME,
                DEFAULT_RESERVATION_DATE,
                reservationTime.getId(),
                theme.getId()
        );

        // when
        ResultActions result = mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/reservations/")))
                .andExpect(jsonPath("$.name").value(RESERVATION_NAME))
                .andExpect(jsonPath("$.date").value(DEFAULT_RESERVATION_DATE.toString()))
                .andExpect(jsonPath("$.time.id").value(reservationTime.getId()))
                .andExpect(jsonPath("$.theme.id").value(theme.getId()));
    }

    @Test
    void 예약을_수정한다() throws Exception {
        // given
        ReservationTime reservationTime1 = reservationTimeService.save(reservationTimeRequest(DEFAULT_START_AT));
        ReservationTime reservationTime2 = reservationTimeService.save(reservationTimeRequest(UPDATED_START_AT));
        Theme theme = themeService.save(themeRequest(THEME_NAME));
        LocalDate reservationDate = DEFAULT_RESERVATION_DATE;
        Map<String, Object> request = reservationRequestBody(
                RESERVATION_NAME,
                reservationDate,
                reservationTime1.getId(),
                theme.getId()
        );
        int id = postReservation(request);

        Map<String, Object> updateRequest = reservationUpdateRequestBody(
                reservationDate.plusDays(1),
                reservationTime2.getId()
        );

        // when
        ResultActions result = mockMvc.perform(patch("/reservations/{id}/schedule", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)));

        // then
        result.andExpect(status().isOk())
                .andExpect(header().string("Location", containsString("/reservations/" + id)))
                .andExpect(jsonPath("$.name").value(RESERVATION_NAME))
                .andExpect(jsonPath("$.date").value(reservationDate.plusDays(1).toString()))
                .andExpect(jsonPath("$.time.id").value(reservationTime2.getId().intValue()));
    }

    @Test
    void 예약_목록을_조회한다() throws Exception {
        // given
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(DEFAULT_START_AT));
        Theme theme = themeService.save(themeRequest(THEME_NAME));
        Map<String, Object> request = reservationRequestBody(
                RESERVATION_NAME,
                DEFAULT_RESERVATION_DATE,
                reservationTime.getId(),
                theme.getId()
        );
        postReservation(request);

        // when
        ResultActions result = mockMvc.perform(get("/admin/reservations"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", hasItem(RESERVATION_NAME)))
                .andExpect(jsonPath("$[*].date", hasItem(DEFAULT_RESERVATION_DATE.toString())))
                .andExpect(jsonPath("$[*].time.id", hasItem(reservationTime.getId().intValue())))
                .andExpect(jsonPath("$[*].theme.runtime", hasItem(60)));
    }

    @Test
    void 예약을_삭제한다() throws Exception {
        // given
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(DEFAULT_START_AT));
        Theme theme = themeService.save(themeRequest(THEME_NAME));
        Map<String, Object> request = reservationRequestBody(
                RESERVATION_NAME,
                DEFAULT_RESERVATION_DATE,
                reservationTime.getId(),
                theme.getId()
        );
        int id = postReservation(request);

        // when
        ResultActions deleteResult = mockMvc.perform(delete("/reservations/{id}?name={name}", id, RESERVATION_NAME));
        ResultActions findResult = mockMvc.perform(get("/admin/reservations"));

        // then
        deleteResult.andExpect(status().isNoContent());
        findResult.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    void 예약을_id와_이름으로_삭제한다() throws Exception {
        // given
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(DEFAULT_START_AT));
        Theme theme = themeService.save(themeRequest(THEME_NAME));
        Map<String, Object> request1 = reservationRequestBody(
                RESERVATION_NAME,
                DEFAULT_RESERVATION_DATE,
                reservationTime.getId(),
                theme.getId()
        );
        Map<String, Object> request2 = reservationRequestBody(
                RESERVATION_NAME,
                NEXT_RESERVATION_DATE,
                reservationTime.getId(),
                theme.getId()
        );
        int id = postReservation(request1);
        postReservation(request2);

        // when
        ResultActions deleteResult = mockMvc.perform(delete("/reservations/{id}?name={name}", id, RESERVATION_NAME));
        ResultActions findResult = mockMvc.perform(get("/admin/reservations"));

        // then
        deleteResult.andExpect(status().isNoContent());
        findResult.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void 이름으로_예약들을_조회한다() throws Exception {
        // given
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(DEFAULT_START_AT));
        Theme theme = themeService.save(themeRequest(THEME_NAME));
        Map<String, Object> request = reservationRequestBody(
                RESERVATION_NAME,
                DEFAULT_RESERVATION_DATE,
                reservationTime.getId(),
                theme.getId()
        );
        postReservation(request);

        // when
        ResultActions result = mockMvc.perform(get("/reservations?name={name}", RESERVATION_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", hasItem(RESERVATION_NAME)));
    }

    @Test
    void 존재하지_않는_예약을_삭제하면_404를_응답한다() throws Exception {
        // when
        ResultActions result = mockMvc.perform(delete("/reservations/{id}?name={name}", NOT_FOUND_ID, RESERVATION_NAME));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString(
                        NotFoundException.clientMessage(DomainType.RESERVATION)
                )));
    }

    @Test
    void 다른_이름으로_예약을_삭제하면_403을_응답한다() throws Exception {
        // given
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(DEFAULT_START_AT));
        Theme theme = themeService.save(themeRequest(THEME_NAME));
        Map<String, Object> request = reservationRequestBody(
                RESERVATION_NAME,
                DEFAULT_RESERVATION_DATE,
                reservationTime.getId(),
                theme.getId()
        );
        int id = postReservation(request);

        // when
        ResultActions result = mockMvc.perform(delete("/reservations/{id}?name={name}", id, OTHER_RESERVATION_NAME));

        // then
        result.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value(containsString(
                        AccessDeniedException.clientMessage(DomainType.RESERVATION.displayName())
                )));
    }

    @Test
    void 날짜_시간_테마가_같은_예약을_등록요청하면_409를_응답한다() throws Exception {
        // given
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(DEFAULT_START_AT));
        Theme theme = themeService.save(themeRequest(THEME_NAME));
        Map<String, Object> request = reservationRequestBody(
                RESERVATION_NAME,
                DEFAULT_RESERVATION_DATE,
                reservationTime.getId(),
                theme.getId()
        );
        postReservation(request);

        // when
        ResultActions result = mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(containsString(
                        DuplicatedException.clientMessage(DomainType.RESERVATION)
                )));
    }

    @ParameterizedTest(name = "{0}은 올바른 예약자 이름이 아니다")
    @CsvSource(value = {"'':예약자 이름은 필수입니다.", "12345678901:예약자 이름은 10자 이하입니다."}, delimiter = ':')
    void 예약을_추가할_때_이름이_올바르지_않으면_400과_예외_메시지를_응답한다(
            String name,
            String expectedMessage
    ) throws Exception {
        // given
        Map<String, Object> request = reservationRequestBody(name, INVALID_RESERVATION_DATE, 1L, 1L);

        // when
        ResultActions result = mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString(expectedMessage)));
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
