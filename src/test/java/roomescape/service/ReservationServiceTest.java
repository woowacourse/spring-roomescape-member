package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.common.exception.DuplicatedException;
import roomescape.dao.FakeReservationDao;
import roomescape.dao.FakeReservationTimeDao;
import roomescape.dao.FakeThemeDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dto.request.ReservationRequestDto;
import roomescape.dto.response.ReservationResponseDto;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

class ReservationServiceTest {

    private final ReservationService reservationService;
    private final ReservationTimeDao reservationTimeDao;
    private final FakeThemeDao themeDao;

    public ReservationServiceTest() {
        this.reservationTimeDao = new FakeReservationTimeDao();
        this.themeDao = new FakeThemeDao();
        this.reservationService = new ReservationService(
                new FakeReservationDao(),
                reservationTimeDao,
                themeDao
        );
    }

    @BeforeEach
    void setUp() {
        reservationTimeDao.saveTime(new ReservationTime(LocalTime.of(20, 0)));
        themeDao.saveTheme(new Theme("공포", "무서워요", "image-url"));
    }

    @DisplayName("예약을 정상적으로 저장한다.")
    @Test
    void test1() {
        // given
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        ReservationRequestDto request = new ReservationRequestDto(
                "다로",
                tomorrow.toString(),
                1L,
                1L
        );

        // when
        ReservationResponseDto response = reservationService.saveReservation(request);

        // then
        assertAll(
                () -> assertThat(response.name()).isEqualTo("다로"),
                () -> assertThat(response.date()).isEqualTo(tomorrow)
        );
    }

    @DisplayName("모든 예약을 조회한다")
    @Test
    void test2() {
        // given
        ReservationRequestDto request1 = new ReservationRequestDto(
                "다로", LocalDate.now().plusDays(1).toString(), 1L, 1L);
        ReservationRequestDto request2 = new ReservationRequestDto(
                "에러", LocalDate.now().plusDays(2).toString(), 1L, 1L);
        reservationService.saveReservation(request1);
        reservationService.saveReservation(request2);

        // when
        List<ReservationResponseDto> reservations = reservationService.getAllReservations();

        // then
        assertThat(reservations).hasSize(2);
        assertThat(reservations).extracting("name")
                .containsExactlyInAnyOrder("다로", "에러");
    }

    @DisplayName("예약을 취소한다")
    @Test
    void test3() {
        // given
        ReservationRequestDto request = new ReservationRequestDto(
                "다로", LocalDate.now().plusDays(1).toString(), 1L, 1L);
        ReservationResponseDto saved = reservationService.saveReservation(request);

        // when
        reservationService.cancelReservation(saved.id());

        // then
        List<ReservationResponseDto> reservations = reservationService.getAllReservations();
        assertThat(reservations).isEmpty();
    }

    @DisplayName("이미 존재하는 예약 시간에 예약한다면 예외를 던진다")
    @Test
    void test4() {
        // given
        ReservationRequestDto request = new ReservationRequestDto(
                "다로", LocalDate.now().plusDays(1).toString(), 1L, 1L);
        reservationService.saveReservation(request);
        ReservationRequestDto savedRequest = new ReservationRequestDto(
                "히로", LocalDate.now().plusDays(1).toString(), 1L, 1L);

        // when && then
        assertThatThrownBy(
                () -> reservationService.saveReservation(savedRequest))
                .isInstanceOf(DuplicatedException.class);
    }

    @DisplayName("당일 예약을 한다면 예외를 던진다")
    @Test
    void test5() {
        // given
        ReservationRequestDto request = new ReservationRequestDto(
                "다로", LocalDate.now().toString(), 1L, 1L);
        // when && then
        assertThatThrownBy(
                () -> reservationService.saveReservation(request))
                .isInstanceOf(IllegalStateException.class);
    }

}
