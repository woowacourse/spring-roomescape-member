package roomescape.reservationtime.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import roomescape.reservationtime.dto.request.CreateReservationTimeRequest;
import roomescape.reservationtime.dto.response.CreateReservationTimeResponse;
import roomescape.reservationtime.dto.response.FindReservationTimeResponse;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.reservationtime.service.ReservationTimeService;
import roomescape.testutil.ControllerTest;
import roomescape.fixture.ReservationTimeFixture;

@ControllerTest
@Import(ReservationTimeController.class)
class ReservationTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("예약 시간 생성 요청 시 201 상태와 Location 헤더에 생성된 리소스의 위치를 반환한다.")
    void createReservationTime() throws Exception {
        // given
        CreateReservationTimeRequest createReservationTimeRequest = new CreateReservationTimeRequest(
                LocalTime.parse("11:00"));
        ReservationTime time = createReservationTimeRequest.toReservationTime();

        // stub
        Mockito.when(reservationTimeService.createReservationTime(createReservationTimeRequest))
                .thenReturn(CreateReservationTimeResponse.from(time));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/times")
                        .content(objectMapper.writeValueAsString(createReservationTimeRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isCreated(),
                        header().stringValues("Location", "/times/" + time.getId()),
                        jsonPath("$.id").value(time.getId()),
                        jsonPath("$.startAt").value(time.getTime().toString())
                );
    }

    @Test
    @DisplayName("예약 시간 조회 요청 성공 시 200과 해당 정보를 반환한다.")
    void getReservationTimes() throws Exception {
        // given
        List<ReservationTime> times = ReservationTimeFixture.get(2);
        ReservationTime reservationTime1 = times.get(0);
        ReservationTime reservationTime2 = times.get(1);

        // stub
        Mockito.when(reservationTimeService.getReservationTimes())
                .thenReturn(List.of(
                        FindReservationTimeResponse.from(reservationTime1),
                        FindReservationTimeResponse.from(reservationTime2)));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/times")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].id").value(reservationTime1.getId()),
                        jsonPath("$[0].startAt").value(reservationTime1.getTime().toString()),

                        jsonPath("$[1].id").value(reservationTime2.getId()),
                        jsonPath("$[1].startAt").value(reservationTime2.getTime().toString())
                );
    }


    @Test
    @DisplayName("예약 단건 조회 요청 성공 시 200과 해당 정보를 반환한다.")
    void getReservationTime() throws Exception {
        // given
        ReservationTime time = ReservationTimeFixture.getOne();

        // stub
        Mockito.when(reservationTimeService.getReservationTime(1L))
                .thenReturn(FindReservationTimeResponse.from(time));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/times/" + 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$.id").value(time.getId()),
                        jsonPath("$.startAt").value(time.getTime().toString()),
                        status().isOk()
                );
    }

    @Test
    @DisplayName("예약 시간 삭제 요청 성공 시 204 상태 코드를 반환한다.")
    void deleteReservationTime() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/times/1"))
                .andExpect(status().isNoContent());
    }
}
