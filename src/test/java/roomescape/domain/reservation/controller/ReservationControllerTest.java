package roomescape.domain.reservation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import roomescape.common.exception.EntityNotFoundException;
import roomescape.domain.reservation.dto.ReservationRequest;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservation.dto.ReservationTimeResponse;
import roomescape.domain.reservation.dto.ThemeResponse;
import roomescape.domain.reservation.service.ReservationService;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservationController reservationController;

    @MockitoBean
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    private static String formatStartAt(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        return time.format(formatter);
    }

    @DisplayName("모든 예약 정보를 가져온다.")
    @Test
    void test1() throws Exception {
        // given
        List<String> names = List.of("꾹", "드라고", "히로");
        LocalDate now = LocalDate.now();
        LocalTime time = LocalTime.now();
        Long timeId = 1L;
        Long themeId = 1L;
        AtomicLong id = new AtomicLong(1L);
        ReservationTimeResponse timeResponse = new ReservationTimeResponse(timeId, time);
        ThemeResponse themeResponse = new ThemeResponse(themeId, "공포", "묘사", "url");

        List<ReservationResponse> responses = names.stream()
                .map(name -> new ReservationResponse(id.getAndIncrement(), name, now, timeResponse, themeResponse))
                .toList();

        // when
        when(reservationService.getAll()).thenReturn(responses);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(names.size()));
    }

    @DisplayName("예약 정보를 추가한다.")
    @Test
    void test2() throws Exception {
        Long reservationId = 1L;
        Long timeId = 1L;
        Long themeId = 1L;

        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.of(8, 0);
        String name = "꾹이";

        ReservationRequest request = new ReservationRequest(name, date, timeId, themeId);
        ReservationTimeResponse timeResponse = new ReservationTimeResponse(timeId, time);
        ThemeResponse themeResponse = new ThemeResponse(themeId, "공포", "묘사", "url");
        ReservationResponse response = new ReservationResponse(reservationId, name, date, timeResponse, themeResponse);

        String requestContent = objectMapper.writeValueAsString(request);

        when(reservationService.create(request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(reservationId))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.date").value(date.toString()))
                .andExpect(jsonPath("$.time.id").value(timeId))
                .andExpect(jsonPath("$.time.startAt").value(formatStartAt(time)));
    }

    @DisplayName("이름이 존재하지 않을 경우 400 상태 코드를 반환한다.")
    @Test
    void test3() throws Exception {
        // given
        Long timeId = 1L;
        Long themeId = 1L;
        LocalDate date = LocalDate.now();

        ReservationRequest request = new ReservationRequest(null, date, timeId, themeId);

        String requestContent = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("존재하지 않는 TimeId면 404 상태 코드를 반환한다.")
    @Test
    void test4() throws Exception {
        // given
        Long timeId = 1L;
        Long themeId = 1L;
        LocalDate date = LocalDate.now();
        String name = "꾹이";

        ReservationRequest request = new ReservationRequest(name, date, timeId, themeId);

        String requestContent = objectMapper.writeValueAsString(request);

        // when
        when(reservationService.create(any())).thenThrow(EntityNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isNotFound());
    }

    @DisplayName("예약 정보를 삭제한다.")
    @Test
    void test5() throws Exception {
        // given
        long reservationId = 1;

        // when
        doNothing().when(reservationService).delete(any());

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/reservations/" + reservationId))
                .andExpect(status().is(204));
    }

    @DisplayName("삭제할 예약 정보가 존재하지 않는다면 404 상태 코드를 반환한다.")
    @Test
    void test6() throws Exception {
        // given
        long reservationId = 1;

        // when
        doThrow(EntityNotFoundException.class).when(reservationService).delete(any());

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/reservations/" + reservationId))
                .andExpect(status().isNotFound());
    }

    @DisplayName("컨트롤러 jdbc 분리 테스트")
    @Test
    void jdbcBeanTest() {
        boolean isJdbcTemplateInjected = false;

        for (Field field : reservationController.getClass().getDeclaredFields()) {
            if (field.getType().equals(JdbcTemplate.class)) {
                isJdbcTemplateInjected = true;
                break;
            }
        }

        assertThat(isJdbcTemplateInjected).isFalse();
    }

    @DisplayName("이름이 공백일 경우 400 상태 코드를 반환한다.")
    @Test
    void test7() throws Exception {
        // given
        String name = "";
        LocalDate date = LocalDate.now();
        Long timeId = 1L;
        Long themeId = 1L;

        ReservationRequest request = new ReservationRequest(name, date, timeId, themeId);
        String content = objectMapper.writeValueAsString(request);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(status().isBadRequest());
    }
}
