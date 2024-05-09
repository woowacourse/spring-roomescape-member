package roomescape.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.dto.reservationtime.ReservationTimeResponse;
import roomescape.dto.theme.ThemeResponse;
import roomescape.fixture.MemberFixtures;
import roomescape.service.AuthService;
import roomescape.service.MemberService;
import roomescape.service.ReservationService;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReservationService reservationService;
    @MockBean
    private AuthService authService;
    @MockBean
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        given(authService.findPayload(anyString())).willReturn("test@test.com");
        given(memberService.findAuthInfo(anyString())).willReturn(MemberFixtures.createUserMember("daon"));
    }

    @Test
    @DisplayName("전체 예약을 조회한다.")
    void getAllReservationsTest() throws Exception {
        //given
        String firstName = "daon";
        String secondDate = "2022-02-05";
        String secondStartAt = "23:22";
        List<ReservationResponse> expectedResponses = getExpectedResponses(firstName, secondDate, secondStartAt);
        given(reservationService.findAll()).willReturn(expectedResponses);

        //when //then
        mockMvc.perform(get("/reservations"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].member.name", is(firstName)))
                .andExpect(jsonPath("$[1].date", is(secondDate)))
                .andExpect(jsonPath("$[1].time.startAt", is(secondStartAt)));
    }

    @Test
    @DisplayName("예약을 성공적으로 삭제한다.")
    void deleteReservationTest() throws Exception {
        //when //then
        mockMvc.perform(delete("/reservations/{id}", 1))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    private List<ReservationResponse> getExpectedResponses(String firstName, String secondDate, String secondStartAt) {
        return List.of(
                ReservationResponse.of(
                        1L,
                        MemberFixtures.createNameResponse(firstName),
                        "2022-02-23",
                        ReservationTimeResponse.of(1L, "12:12"),
                        ThemeResponse.of(1L, "방탈출1", "1번 방탈출", "썸네일1")
                ),
                ReservationResponse.of(
                        2L,
                        MemberFixtures.createNameResponse("ikjo"),
                        secondDate,
                        ReservationTimeResponse.of(2L, secondStartAt),
                        ThemeResponse.of(1L, "방탈출1", "1번 방탈출", "썸네일1")
                )
        );
    }
}
