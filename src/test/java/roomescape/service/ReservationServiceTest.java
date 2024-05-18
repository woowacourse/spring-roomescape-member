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
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationCreateRequest;
import roomescape.dto.response.AvailableTimeResponse;
import roomescape.dto.response.ReservationResponse;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
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
        Member member = memberRepository.findById(1L);
        Reservation reservation1 = new Reservation(
                member,
                LocalDate.parse("2025-10-05"),
                reservationTime,
                theme
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
        List<AvailableTimeResponse> availableTimeResponses = reservationService.findByDateAndThemeId(date, themeId);
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

        Member member = new Member(Role.ADMIN, "admin1@email.com", "password");

        //when
        ReservationResponse reservationResponse = reservationService.create(reservationCreateRequest, member);

        //then
        assertAll(
                () -> assertThat(reservationResponse.member().name()).isEqualTo("유저"),
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
        Member member = new Member(Role.ADMIN, "admin1@email.com", "password");
        ReservationResponse reservationResponse = reservationService.create(reservationCreateRequest, member);

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
        Member member = new Member(Role.ADMIN, "admin1@email.com", "password");
        ReservationResponse reservationResponse = reservationService.create(reservationCreateRequest, member);

        //when & then
        assertThatThrownBy(() -> reservationService.delete(0L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("삭제할 예약이 존재하지 않습니다");
    }
}
