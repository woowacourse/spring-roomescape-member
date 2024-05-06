package roomescape.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.domain.exception.InvalidValueException;
import roomescape.dto.reservation.ReservationCreateRequest;
import roomescape.dto.reservationtime.ReservationTimeCreateRequest;
import roomescape.dto.reservationtime.ReservationTimeResponse;
import roomescape.fixture.ReservationFixtures;
import roomescape.fixture.ReservationTimeFixtures;
import roomescape.service.ReservationTimeService;
import roomescape.service.exception.InvalidRequestException;

@WebMvcTest(ReservationTimeController.class)
class ReservationTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("전체 예약 시간을 조회한다.")
    void readAll() throws Exception {
        //given
        String firstStartAt = "12:40";
        String secondStartAt = "23:25";
        List<ReservationTimeResponse> responses = List.of(
                ReservationTimeFixtures.createReservationTimeResponse(1L, firstStartAt),
                ReservationTimeFixtures.createReservationTimeResponse(2L, secondStartAt)
        );
        given(reservationTimeService.findAll())
                .willReturn(responses);

        //when //then
        mockMvc.perform(get("/times"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].startAt", is(firstStartAt)))
                .andExpect(jsonPath("$[1].startAt", is(secondStartAt)));
    }

    @Test
    @DisplayName("예약 시간을 성공적으로 추가한다.")
    void create() throws Exception {
        //given
        String startAt = "22:04";
        ReservationTimeCreateRequest givenRequest = ReservationTimeFixtures.createReservationTimeCreateRequest(startAt);
        ReservationTimeResponse expectedResponse = ReservationTimeFixtures.createReservationTimeResponse(2L, startAt);
        given(reservationTimeService.add(givenRequest))
                .willReturn(expectedResponse);
        String requestBody = objectMapper.writeValueAsString(givenRequest);

        //when //then
        mockMvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.startAt", is(startAt)));
    }

    @Test
    @DisplayName("예약 시간을 성공적으로 삭제한다.")
    void deleteWithId() throws Exception {
        //given
        long giveId = 1L;

        //when //then
        mockMvc.perform(delete("/times/{id}", giveId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("InvalidValueException이 발생하면 Bad Request 응답을 반환한다.")
    void createReservationTimeByInvalidValue() throws Exception {
        //given
        ReservationTimeCreateRequest givenRequest =
                ReservationTimeFixtures.createReservationTimeCreateRequest("InvalidStartAt");
        given(reservationTimeService.add(givenRequest))
                .willThrow(InvalidValueException.class);
        String requestBody = objectMapper.writeValueAsString(givenRequest);

        //when //then
        mockMvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("InvalidRequestException이 발생하면 Bad Request 응답을 반환한다.")
    void createReservationByInvalidRequest() throws Exception {
        //given
        ReservationTimeCreateRequest givenRequest =
                ReservationTimeFixtures.createReservationTimeCreateRequest("InvalidStartAt");
        given(reservationTimeService.add(givenRequest))
                .willThrow(InvalidRequestException.class);
        String requestBody = objectMapper.writeValueAsString(givenRequest);

        //when //then
        mockMvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
