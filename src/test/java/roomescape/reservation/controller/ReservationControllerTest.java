package roomescape.reservation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import java.time.LocalDate;
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
import roomescape.auth.TokenProvider;
import roomescape.auth.config.AuthInfo;
import roomescape.member.domain.MemberRole;
import roomescape.reservation.dto.request.CreateReservationRequest;
import roomescape.reservation.dto.response.CreateReservationResponse;
import roomescape.reservation.dto.response.FindAvailableTimesResponse;
import roomescape.reservation.dto.response.FindReservationResponse;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.service.ReservationService;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.testutil.ControllerTest;
import roomescape.theme.model.Theme;
import roomescape.util.ReservationFixture;
import roomescape.util.ReservationTimeFixture;

@ControllerTest
@Import(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private TokenProvider tokenProvider;

    @Test
    @DisplayName("예약 생성 요청 시 201 상태와 Location 헤더에 생성된 리소스의 위치를 반환한다.")
    void createReservation() throws Exception {
        // given
        Reservation reservation = ReservationFixture.getOne();
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                LocalDate.of(3000, 1, 1), 1L, 1L);
        String token = "asdfsadf";
        AuthInfo authInfo = new AuthInfo(1L, "asdf", MemberRole.USER);

        // stub
        Mockito.when(tokenProvider.extractAuthInfo(any())).thenReturn(authInfo);
        Mockito.when(reservationService.createReservation(authInfo, createReservationRequest))
                .thenReturn(CreateReservationResponse.from(reservation));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
                        .content(objectMapper.writeValueAsString(createReservationRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("token", token)))
                .andExpectAll(
                        status().isCreated(),
                        header().stringValues("Location", "/reservations/" + reservation.getId()),
                        jsonPath("$.id").value(reservation.getId()),
                        jsonPath("$.name").value(reservation.getMember().getName()),
                        jsonPath("$.date").value(reservation.getDate().toString()),
                        jsonPath("$.time.id").value(reservation.getReservationTime().getId()),
                        jsonPath("$.time.startAt").value(reservation.getReservationTime().getTime().toString()),
                        jsonPath("$.theme.id").value(reservation.getTheme().getId()),
                        jsonPath("$.theme.name").value(reservation.getTheme().getName()),
                        jsonPath("$.theme.description").value(reservation.getTheme().getDescription()),
                        jsonPath("$.theme.thumbnail").value(reservation.getTheme().getThumbnail())
                );
    }

    @Test
    @DisplayName("예약 목록 조회 요청 성공 시 200과 해당 정보를 반환한다.")
    void getReservations() throws Exception {
        // stub
        List<Reservation> reservations = ReservationFixture.get(2);
        Reservation reservation1 = reservations.get(0);
        Reservation reservation2 = reservations.get(1);

        // stub
        Mockito.when(reservationService.getReservations())
                .thenReturn(List.of(FindReservationResponse.from(reservation1),
                        FindReservationResponse.from(reservation2)));

        mockMvc.perform(MockMvcRequestBuilders.get("/reservations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$.[0].id").value(reservation1.getId()),
                        jsonPath("$.[0].member.name").value(reservation1.getMember().getName()),
                        jsonPath("$.[0].date").value(reservation1.getDate().toString()),
                        jsonPath("$.[0].time.id").value(reservation1.getReservationTime().getId()),
                        jsonPath("$.[0].time.startAt").value(reservation1.getReservationTime().getTime().toString()),

                        jsonPath("$.[1].id").value(reservation2.getId()),
                        jsonPath("$.[1].member.name").value(reservation2.getMember().getName()),
                        jsonPath("$.[1].date").value(reservation2.getDate().toString()),
                        jsonPath("$.[1].time.id").value(reservation2.getReservationTime().getId()),
                        jsonPath("$.[1].time.startAt").value(reservation2.getReservationTime().getTime().toString()),
                        status().isOk()
                );
    }


    @Test
    @DisplayName("예약 단건 조회 요청 성공 시 200과 해당 정보를 반환한다.")
    void getReservation() throws Exception {
        // given
        Reservation reservation = ReservationFixture.getOne();
        ReservationTime time = reservation.getReservationTime();
        Theme theme = reservation.getTheme();

        // stub
        Mockito.when(reservationService.getReservation(1L))
                .thenReturn(FindReservationResponse.from(reservation));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/reservations/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$.id").value(reservation.getId()),
                        jsonPath("$.member.name").value(reservation.getMember().getName()),
                        jsonPath("$.date").value(reservation.getDate().toString()),
                        jsonPath("$.time.id").value(time.getId()),
                        jsonPath("$.time.startAt").value(time.getTime().toString()),
                        jsonPath("$.theme.id").value(theme.getId()),
                        jsonPath("$.theme.name").value(theme.getName()),
                        jsonPath("$.theme.description").value(theme.getDescription()),
                        jsonPath("$.theme.thumbnail").value(theme.getThumbnail()),
                        status().isOk()
                );
    }


    @Test
    @DisplayName("예약 가능 시간 요청한 시간 요청 성공 시 해당 정보과 200을 반환한다.")
    void getAvailableTimes() throws Exception {
        // given
        LocalDate date = LocalDate.parse("2024-01-01");
        List<ReservationTime> times = ReservationTimeFixture.get(2);

        // stub
        Mockito.when(reservationService.getAvailableTimes(date, 1L))
                .thenReturn(List.of(new FindAvailableTimesResponse(1L, times.get(0).getTime(), false),
                        new FindAvailableTimesResponse(2L, times.get(1).getTime(), true)));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(
                                "/reservations/times?date=" + date + "&themeId=" + 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$.[0].id").value(1),
                        jsonPath("$.[0].startAt").value(times.get(0).getTime().toString()),
                        jsonPath("$.[0].alreadyBooked").value(false),
                        jsonPath("$.[1].id").value(2),
                        jsonPath("$.[1].startAt").value(times.get(1).getTime().toString()),
                        jsonPath("$.[1].alreadyBooked").value(true),
                        status().isOk()
                );
    }

    @Test
    @DisplayName("예약 삭제 요청 성공 시 204 상태 코드를 반환한다.")
    void deleteReservation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/reservations/1"))
                .andExpect(status().isNoContent());
    }
}
