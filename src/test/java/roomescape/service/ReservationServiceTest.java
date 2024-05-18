package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.ReservationTime;
import roomescape.theme.domain.Theme;
import roomescape.reservation.dto.AvailableTimeResponse;
import roomescape.auth.dto.LoginMember;
import roomescape.reservation.dto.ReservationCreateRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.global.exception.exceptions.NotExistingEntryException;
import roomescape.member.domain.MemberRepository;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.time.domain.ReservationTimeRepository;
import roomescape.theme.domain.ThemeRepository;
import roomescape.reservation.application.ReservationService;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationServiceTest {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        reservationTimeRepository.save(new ReservationTime(LocalTime.parse("10:00")));
        ReservationTime reservationTime = reservationTimeRepository.findByTimeId(1L);
        themeRepository.save(new Theme("테마명", "테마 설명", "테마 이미지"));
        Theme theme = themeRepository.findByThemeId(1L);
        Member member = memberRepository.findByMemberId(1L);
        Reservation reservation1 = new Reservation(
                LocalDate.parse("2025-10-05"),
                reservationTime,
                theme,
                member
        );
        reservationRepository.save(reservation1);
    }

    @Test
    @DisplayName("날짜와 테마가 주어졌을 때 예약이 되어 있는 시간들을 확인한다.")
    void checkIsAlreadyBookedTime() {
        //given
        LocalDate date = LocalDate.parse("2025-10-05");
        Long themeId = 1L;

        //when
        List<AvailableTimeResponse> availableTimeResponses
                = reservationService.findByDateAndThemeId(date, themeId)
                .responses();
        AvailableTimeResponse availableTimeResponse = availableTimeResponses.get(0);

        //then
        assertAll(
                () -> assertThat(availableTimeResponse.alreadyBooked()).isTrue(),
                () -> assertThat(availableTimeResponse.timeId()).isEqualTo(1L),
                () -> assertThat(availableTimeResponse.startAt()).isEqualTo("10:00")
        );
    }

    @Test
    @DisplayName("예약 생성 기능을 확인한다.")
    void checkReservationCreate() {
        //given
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(
                LocalDate.parse("2025-04-10"),
                1L,
                1L
        );
        LoginMember loginMember = new LoginMember("어드민");

        //when
        ReservationResponse reservationResponse = reservationService.create(reservationCreateRequest, loginMember);

        //then
        assertAll(
                () -> assertThat(reservationResponse.memberResponse().name()).isEqualTo("어드민"),
                () -> assertThat(reservationResponse.date()).isEqualTo("2025-04-10"),
                () -> assertThat(reservationResponse.time().id()).isEqualTo(1L),
                () -> assertThat(reservationResponse.theme().id()).isEqualTo(1L)
        );
    }

    @Test
    @DisplayName("예약 삭제 기능을 확인한다.")
    void checkReservationDelete() {
        //given
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(
                LocalDate.parse("2025-04-10"),
                1L,
                1L
        );
        LoginMember loginMember = new LoginMember("어드민");
        ReservationResponse reservationResponse = reservationService.create(reservationCreateRequest, loginMember);

        //when & then
        assertDoesNotThrow(() -> reservationService.delete(reservationResponse.id()));
    }

    @Test
    @DisplayName("예약 삭제 기능의 실패를 확인한다.")
    void checkReservationDeleteFail() {
        //given
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(
                LocalDate.parse("2025-04-10"),
                1L,
                1L
        );
        LoginMember loginMember = new LoginMember("어드민");
        reservationService.create(reservationCreateRequest, loginMember);

        //when & then
        assertThatThrownBy(() -> reservationService.delete(0L))
                .isInstanceOf(NotExistingEntryException.class)
                .hasMessage("삭제할 예약이 존재하지 않습니다");
    }
}
