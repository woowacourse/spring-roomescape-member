package roomescape.web.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import roomescape.service.ReservationService;
import roomescape.web.dto.request.ReservationRequest;
import roomescape.web.dto.response.ReservationResponse;
import roomescape.web.dto.response.ReservationTimeResponse;
import roomescape.web.dto.response.ThemeResponse;

@WebMvcTest(controllers = ReservationController.class)
class ReservationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ReservationService reservationService;

    @Test
    @DisplayName("예약 저장 시 모든 필드가 유효한 값이라면 201Created를 반환한다.")
    void saveReservation_ShouldReturn201StatusCode_WhenInsertAllValidateField() throws Exception {
        // given
        ReservationRequest request = new ReservationRequest(LocalDate.now(), 1, 1);
        Mockito.when(reservationService.saveReservation(request))
                .thenReturn(
                        new ReservationResponse(1L, LocalDate.now(),
                                new ReservationTimeResponse(1L, LocalTime.now()),
                                new ThemeResponse(1L, "n", "d", "t"))
                );

        // when & then
        mockMvc.perform(
                        post("/reservations")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("예약 저장 시 모든 필드가 유효한 값이라면 location 헤더가 추가된다.")
    void saveReservation_ShouldRedirect_WhenInsertAllValidateField() throws Exception {
        // given
        ReservationRequest request = new ReservationRequest(LocalDate.now(), 1, 1);
        Mockito.when(reservationService.saveReservation(request))
                .thenReturn(
                        new ReservationResponse(1L, LocalDate.now(),
                                new ReservationTimeResponse(1L, LocalTime.now()),
                                new ThemeResponse(1L, "n", "d", "t"))
                );

        // when & then
        mockMvc.perform(
                        post("/reservations")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(header().string(LOCATION, "/reservations/1"));
    }

    @Test
    @DisplayName("예약 저장 시 날짜에 빈값이면 400 BadRequest를 반환한다.")
    void saveReservation_ShouldReturn400StatusCode_WhenInsertNullDate() throws Exception {
        // given
        ReservationRequest request = new ReservationRequest(null, 1, 1);

        // when & then
        mockMvc.perform(
                        post("/reservations")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(content().string(containsString("날짜는 빈값을 허용하지 않습니다.")));
    }

    @Test
    @DisplayName("예약 저장 시 타임아이디가 0이하의 값이면 400 BadRequest를 반환한다.")
    void saveReservation_ShouldReturn400StatusCode_WhenInsertLessThen0TimeId() throws Exception {
        // given
        ReservationRequest request = new ReservationRequest(LocalDate.now(), 0, 1);

        // when & then
        mockMvc.perform(
                        post("/reservations")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(content().string(containsString("타임 아이디는 1이상의 정수만 허용합니다.")));
    }

    @Test
    @DisplayName("예약 저장 시 테마아이디가 0이하의 값이면 400 BadRequest를 반환한다.")
    void saveReservation_ShouldReturn400StatusCode_WhenInsertLessThen0ThemeId() throws Exception {
        // given
        ReservationRequest request = new ReservationRequest(LocalDate.now(), 1, 0);

        // when & then
        mockMvc.perform(
                        post("/reservations")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(content().string(containsString("테마 아이디는 1이상의 정수만 허용합니다.")));
    }
}
