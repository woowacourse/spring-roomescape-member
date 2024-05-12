package roomescape.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.config.AuthenticationExtractor;
import roomescape.domain.member.Member;
import roomescape.dto.reservationtime.ReservationTimeCreateRequest;
import roomescape.dto.reservationtime.ReservationTimeResponse;
import roomescape.fixture.MemberFixtures;
import roomescape.service.AuthService;
import roomescape.service.ReservationTimeService;

@WebMvcTest(ReservationTimeController.class)
class ReservationTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ReservationTimeService reservationTimeService;
    @MockBean
    private AuthService authService;
    @MockBean
    private AuthenticationExtractor authenticationExtractor;

    @BeforeEach
    void setUp() {
        Member member = MemberFixtures.createUserMember("daon");
        given(authService.findAuthInfo(anyString())).willReturn(member);
        given(authenticationExtractor.extractAuthInfo(any(HttpServletRequest.class))).willReturn(member);
    }

    @Test
    @DisplayName("전체 예약 시간을 조회한다.")
    void readAll() throws Exception {
        //given
        String firstStartAt = "12:40";
        String secondStartAt = "23:25";
        List<ReservationTimeResponse> responses = List.of(
                ReservationTimeResponse.of(1L, firstStartAt),
                ReservationTimeResponse.of(2L, secondStartAt)
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
        ReservationTimeCreateRequest givenRequest = ReservationTimeCreateRequest.from(startAt);
        ReservationTimeResponse expectedResponse = ReservationTimeResponse.of(2L, startAt);
        given(reservationTimeService.add(any(ReservationTimeCreateRequest.class)))
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

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"1212", "anytime"})
    @DisplayName("유효하지않는 값이 입력되면 Bad Request 응답을 반환한다.")
    void createReservationTimeByInvalidStartAt(String given) throws Exception {
        //given
        ReservationTimeCreateRequest givenRequest = ReservationTimeCreateRequest.from(given);
        given(reservationTimeService.add(any(ReservationTimeCreateRequest.class)))
                .willThrow(IllegalArgumentException.class);
        String requestBody = objectMapper.writeValueAsString(givenRequest);

        //when //then
        mockMvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
