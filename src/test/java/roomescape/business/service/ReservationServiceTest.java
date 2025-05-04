package roomescape.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.persistence.fakerepository.FakeReservationRepository;
import roomescape.persistence.fakerepository.FakeReservationThemeRepository;
import roomescape.persistence.fakerepository.FakeReservationTimeRepository;
import roomescape.business.service.ReservationService;
import roomescape.exception.ReservationException;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ReservationThemeRepository;
import roomescape.persistence.ReservationTimeRepository;
import roomescape.presentation.dto.ReservationRequestDto;

class ReservationServiceTest {

    private ReservationService reservationService;
    private ReservationTimeRepository reservationTimeRepository;
    private ReservationThemeRepository reservationThemeRepository;

    @BeforeEach
    void setUp() {
        ReservationRepository reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        reservationThemeRepository = new FakeReservationThemeRepository();
        reservationService = new ReservationService(
                reservationRepository,
                reservationTimeRepository,
                reservationThemeRepository
        );
    }

    @DisplayName("예약한다.")
    @Test
    void createReservation() {
        // given
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Long timeId = reservationTimeRepository.add(new ReservationTime(LocalTime.now()));
        Long themeId = reservationThemeRepository.add(new ReservationTheme("테마", "설명", "썸네일"));

        // when
        reservationService.createReservation(new ReservationRequestDto("예약자", tomorrow,timeId, themeId));

        // then
        assertThat(reservationService.getAllReservations())
                .isNotEmpty();
    }

    @DisplayName("과거 일시로 예약을 생성할 경우 예외가 발생한다.")
    @Test
    void createPastReservation() {
        // given
        LocalDateTime pastDateTime = LocalDateTime.now().minusDays(1);
        Long timeId = reservationTimeRepository.add(new ReservationTime(pastDateTime.toLocalTime()));
        Long themeId = reservationThemeRepository.add(new ReservationTheme("테마", "설명", "썸네일"));
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto("벨로", pastDateTime.toLocalDate(),
                timeId, themeId);

        // when
        // then
        assertThatCode(() -> reservationService.createReservation(reservationRequestDto))
                .isInstanceOf(ReservationException.class)
                .hasMessage("과거 일시로 예약을 생성할 수 없습니다.");
    }

    @DisplayName("이미 예약된 경우 예약할 수 없다.")
    @Test
    void failCreateReservation() {
        // given
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Long timeId = reservationTimeRepository.add(new ReservationTime(LocalTime.now()));
        Long themeId = reservationThemeRepository.add(new ReservationTheme("테마", "설명", "썸네일"));
        reservationService.createReservation(new ReservationRequestDto("예약자", tomorrow, timeId, themeId));

        // when
        // then
        assertThatCode(
                () -> reservationService.createReservation(new ReservationRequestDto("예약자", tomorrow, timeId, themeId)))
                .isInstanceOf(ReservationException.class)
                .hasMessage("이미 예약되었습니다.");
    }

    @DisplayName("예약을 취소한다.")
    @Test
    void deleteReservationById() {
        // given
        Long timeId = reservationTimeRepository.add(new ReservationTime(LocalTime.now()));
        Long themeId = reservationThemeRepository.add(new ReservationTheme("테마", "설명", "썸네일"));
        Long id = reservationService.createReservation(new ReservationRequestDto(
                "예약자",
                LocalDate.now().plusDays(1),
                timeId,
                themeId)
        );

        // when
        reservationService.deleteReservationById(id);

        // then
        assertThat(reservationService.getAllReservations()).isEmpty();
    }

    @DisplayName("예약 목록을 불러온다")
    @Test
    void getAllReservations() {
        // given
        Long timeId = reservationTimeRepository.add(new ReservationTime(LocalTime.now()));
        Long themeId = reservationThemeRepository.add(new ReservationTheme("테마", "설명", "썸네일"));
        Long id = reservationService.createReservation(
                new ReservationRequestDto(
                        "예약자",
                        LocalDate.now().plusDays(1),
                        timeId,
                        themeId)
        );

        // when
        int firstReadSize = reservationService.getAllReservations().size();
        reservationService.deleteReservationById(id);
        int secondReadSize = reservationService.getAllReservations().size();

        // then
        assertThat(firstReadSize).isEqualTo(1);
        assertThat(secondReadSize).isEqualTo(0);
    }
}
