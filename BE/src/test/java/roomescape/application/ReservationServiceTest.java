package roomescape.application;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.entity.Reservation;
import roomescape.entity.Theme;
import roomescape.entity.ThemeRepository;
import roomescape.fake.FakeThemeRepository;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.ReservationTimeException;
import roomescape.fake.FakeReservationRepository;
import roomescape.fake.FakeReservationTimeRepository;
import roomescape.entity.ReservationTime;
import roomescape.entity.ReservationRepository;
import roomescape.entity.ReservationTimeRepository;
import roomescape.presentation.dto.ReservationRequest;

class ReservationServiceTest {

    private final ReservationRepository reservationRepository = new FakeReservationRepository();;
    private final ReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
    private final ThemeRepository themeRepository = new FakeThemeRepository();
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository);
    }

    @Test
    @DisplayName("예약을 저장한다")
    void saveReservation_success() {
        // given
        ReservationTime savedTime = reservationTimeRepository.save(ReservationTime.createWithNullId(LocalTime.of(10, 0)));
        Theme savedTheme = themeRepository.save(Theme.createWithNullId("공포", "아니", "https://good.com/thumb-nail/1"));
        ReservationRequest request = new ReservationRequest(
                "흑곰",
                LocalDate.now(),
                savedTime.id(),
                savedTheme.id()
        );

        // when & then
        assertThatCode(() -> reservationService.saveReservation(
                request.name(),
                request.date(),
                request.timeId(),
                request.themeId()
        )).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간으로 예약하면 예외가 전파된다")
    void saveReservation_fail_with_not_found_time() {
        // given
        Long notExistTimeId = 999L;
        Long notExistThemeId = 999L;

        ReservationRequest request = new ReservationRequest(
                "흑곰",
                LocalDate.now(),
                notExistTimeId,
                notExistThemeId
        );

        // when & then
        assertThatThrownBy(
                () -> reservationService.saveReservation(
                request.name(),
                request.date(),
                request.timeId(),
                request.themeId()
            )
        ).isInstanceOf(ReservationTimeException.class)
                .hasMessage(ErrorCode.RESERVATION_TIME_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("존재하지 않는 테마 ID로 예약하면 예외가 전파된다")
    //메서드명-성공 혹은 실패 - (이유)
    void saveReservation_fail_with_not_found_theme() {
        // given
        Long notExistTimeId = 999L;
        Long notExistThemeId = 999L;

        ReservationRequest request = new ReservationRequest(
                "흑곰",
                LocalDate.now(),
                notExistTimeId,
                notExistThemeId
        );

        // when & then
        assertThatThrownBy(
                () -> reservationService.saveReservation(
                        request.name(),
                        request.date(),
                        request.timeId(),
                        request.themeId()
                )
        ).isInstanceOf(ReservationTimeException.class)
                .hasMessage(ErrorCode.RESERVATION_TIME_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("예약 목록을 조회한다")
    void getReservations_success() {
        // given
        ReservationTime savedTime = reservationTimeRepository.save(ReservationTime.createWithNullId(LocalTime.of(10, 0)));
        Theme savedTheme = themeRepository.save(Theme.createWithNullId("공포", "아니", "https://good.com/thumb-nail/1"));
        Reservation savedReservation = reservationRepository.save(Reservation.createWithNullId(
                "인직",
                LocalDate.now(),
                savedTime,
                savedTheme
        ));

        // when & then
        assertThatCode(() -> reservationService.getReservations())
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("예약을 삭제한다")
    void deleteReservation_success() {
        // given
        ReservationTime savedTime = reservationTimeRepository.save(ReservationTime.createWithNullId(LocalTime.of(10, 0)));
        Theme savedTheme = themeRepository.save(Theme.createWithNullId("공포", "아니", "https://good.com/thumb-nail/1"));
        Reservation savedReservation = reservationRepository.save(Reservation.createWithNullId(
                "인직",
                LocalDate.now(),
                savedTime,
                savedTheme
        ));

        // when & then
        assertThatCode(() -> reservationService.deleteReservation(savedReservation.id()))
                .doesNotThrowAnyException();
    }
}
