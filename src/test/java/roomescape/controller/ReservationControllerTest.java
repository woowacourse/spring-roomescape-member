package roomescape.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.reservation.CreateReservationRequest;
import roomescape.dto.reservation.ReservationResponses;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.InvalidReservationDateTimeException;
import roomescape.service.ReservationService;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservationService reservationService;

    @Test
    void GET_reservations_서비스가_반환한_목록과_hasNext를_그대로_응답한다() throws Exception {
        given(reservationService.getReservations(0, 20))
                .willReturn(ReservationResponses.of(List.of(sampleReservation(1L)), false));

        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservations.size()").value(1))
                .andExpect(jsonPath("$.reservations[0].name").value("브라운"))
                .andExpect(jsonPath("$.hasNext").value(false));
    }

    @Test
    void GET_reservations_page와_size_쿼리_파라미터를_그대로_위임한다() throws Exception {
        given(reservationService.getReservations(2, 5))
                .willReturn(ReservationResponses.of(List.of(), true));

        mockMvc.perform(get("/reservations?page=2&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hasNext").value(true));

        verify(reservationService).getReservations(2, 5);
    }

    @Test
    void GET_reservations_id_단건을_응답한다() throws Exception {
        given(reservationService.getReservation(1L)).willReturn(sampleReservation(1L));

        mockMvc.perform(get("/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("브라운"));
    }

    @Test
    void POST_reservations_생성된_id를_Location_헤더에_담아_201을_반환한다() throws Exception {
        given(reservationService.createReservation(any(CreateReservationRequest.class)))
                .willReturn(sampleReservation(7L));

        Map<String, Object> body = Map.of(
                "name", "브라운",
                "date", "2026-05-06",
                "themeId", 1,
                "timeId", 1);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/reservations/7"));
    }

    @Test
    void POST_reservations_서비스가_DuplicateReservationException을_던지면_409과_메시지를_반환한다() throws Exception {
        willThrow(new DuplicateReservationException())
                .given(reservationService).createReservation(any(CreateReservationRequest.class));

        Map<String, Object> body = Map.of(
                "name", "브라운",
                "date", "2026-05-06",
                "themeId", 1,
                "timeId", 1);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("해당 날짜·시간·테마에 이미 예약이 존재합니다. 다른 날짜·시간·테마를 선택해주세요."));
    }

    @Test
    void POST_reservations_서비스가_InvalidReservationDateTimeException을_던지면_422_와_메시지를_반환한다() throws Exception {
        willThrow(new InvalidReservationDateTimeException())
                .given(reservationService).createReservation(any(CreateReservationRequest.class));

        Map<String, Object> body = Map.of(
                "name", "브라운",
                "date", "2026-05-06",
                "themeId", 1,
                "timeId", 1);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("예약 일정이 유효하지 않습니다. 예약 날짜와 시간은 현시간 이후여야 합니다."));
    }

    @Test
    void GET_reservations_id_서비스가_ResourceNotFoundException을_던지면_404과_메시지를_반환한다() throws Exception {
        org.mockito.BDDMockito.given(reservationService.getReservation(9999L))
                .willThrow(new roomescape.exception.ResourceNotFoundException("예약", 9999L));

        mockMvc.perform(get("/reservations/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("예약을(를) 찾을 수 없습니다. id=9999"));
    }

    @Test
    void DELETE_reservations_id_200을_반환하고_서비스에_위임한다() throws Exception {
        mockMvc.perform(delete("/reservations/3"))
                .andExpect(status().isOk());

        verify(reservationService).deleteReservation(3L);
    }

    @Test
    void POST_reservations_본문의_date가_형식_오류면_400과_메시지를_반환한다() throws Exception {
        String body = """
                {"name":"브라운","date":"abc","themeId":1,"timeId":1}
                """;

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("'date' 값 'abc'은(는) yyyy-MM-dd 형식이어야 합니다."));
    }

    @Test
    void GET_reservations_id가_숫자가_아니면_400과_메시지를_반환한다() throws Exception {
        mockMvc.perform(get("/reservations/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("'id' 값 'abc'은(는) 숫자 형식이어야 합니다."));
    }

    @Test
    void POST_reservations_본문의_date가_존재하지_않는_날짜면_400과_메시지를_반환한다() throws Exception {
        String body = """
                {"name":"브라운","date":"2026-13-40","themeId":1,"timeId":1}
                """;

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("'date' 값 '2026-13-40'은(는) yyyy-MM-dd 형식이어야 합니다."));
    }

    @Test
    void POST_reservations_본문의_timeId가_숫자가_아니면_400과_메시지를_반환한다() throws Exception {
        String body = """
                {"name":"브라운","date":"2026-05-08","themeId":1,"timeId":"abc"}
                """;

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("'timeId' 값 'abc'은(는) 숫자 형식이어야 합니다."));
    }

    @Test
    void POST_reservations_본문의_name이_빈_문자열이면_400과_메시지를_반환한다() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("name", "");
        body.put("date", "2026-05-08");
        body.put("themeId", 1);
        body.put("timeId", 1);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("name은(는) 필수 입력값입니다."));
    }

    @Test
    void POST_reservations_본문의_themeId가_누락되면_400과_메시지를_반환한다() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("name", "브라운");
        body.put("date", "2026-05-08");
        body.put("timeId", 1);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("themeId은(는) 필수 입력값입니다."));
    }

    @Test
    void POST_reservations_본문_JSON_문법_오류면_400과_메시지를_반환한다() throws Exception {
        String brokenBody = "{\"name\":\"홍길동";

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(brokenBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("요청 본문 형식이 올바르지 않습니다."));
    }

    private Reservation sampleReservation(Long id) {
        Theme theme = new Theme(1L, "공포", "무서움", "https://thumbnail.url");
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        return new Reservation(id, "브라운", theme, LocalDate.of(2026, 5, 6), time);
    }
}
