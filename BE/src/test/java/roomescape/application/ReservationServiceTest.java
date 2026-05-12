package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.fake.FakeThemeRepository;
import roomescape.global.exception.BusinessException;
import roomescape.fake.FakeReservationRepository;
import roomescape.fake.FakeReservationTimeRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTimeRepository;
import roomescape.global.exception.EntityNotFoundException;
import roomescape.presentation.dto.ReservationRequest;

class ReservationServiceTest {

    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ThemeRepository themeRepository;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        themeRepository = new FakeThemeRepository();
        reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository);
    }

    @Test
    @DisplayName("예약을 저장한다")
    void saveReservation_success() {
        // given
        ReservationTime savedTime = reservationTimeRepository.save(ReservationTime.create(LocalTime.now().plusHours(1)));
        Theme savedTheme = themeRepository.save(Theme.create("공포", "아니", "https://good.com/thumb-nail/1"));
        ReservationRequest request = new ReservationRequest(
                "흑곰",
                LocalDate.now(),
                savedTime.getId(),
                savedTheme.getId()
        );

        // when
        Reservation reservation = reservationService.saveReservation(
                request.name(),
                request.date(),
                request.timeId(),
                request.themeId()
        );

        // then
        assertThat(reservation.getId()).isNotNull();
        assertThat(reservation.getName()).isEqualTo(request.name());
        assertThat(reservation.getDate()).isEqualTo(request.date());
        assertThat(reservation.getTime()).isEqualTo(savedTime);
        assertThat(reservation.getTheme()).isEqualTo(savedTheme);
        assertThat(reservationRepository.findById(reservation.getId())).contains(reservation);
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간으로 예약하면 예외가 전파된다")
    void saveReservation_fail_with_not_found_time() {
        // given
        Long notExistTimeId = 999L;
        Theme savedTheme = themeRepository.save(Theme.create("공포", "아니", "https://good.com/thumb-nail/1"));

        ReservationRequest request = new ReservationRequest(
                "흑곰",
                LocalDate.now(),
                notExistTimeId,
                savedTheme.getId()
        );

        // when & then
        assertThatThrownBy(
                () -> reservationService.saveReservation(
                request.name(),
                request.date(),
                request.timeId(),
                request.themeId()
            )
        ).isInstanceOf(EntityNotFoundException.class);
        assertThat(reservationRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 테마 ID로 예약하면 예외가 전파된다")
    //메서드명-성공 혹은 실패 - (이유)
    void saveReservation_fail_with_not_found_theme() {
        // given
        ReservationTime savedTime = reservationTimeRepository.save(ReservationTime.create(LocalTime.now().plusHours(1)));
        Long notExistThemeId = 999L;

        ReservationRequest request = new ReservationRequest(
                "흑곰",
                LocalDate.now(),
                savedTime.getId(),
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
        ).isInstanceOf(EntityNotFoundException.class);
        assertThat(reservationRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("예약 목록을 조회한다")
    void getReservations_success() {
        // given
        ReservationTime savedTime = reservationTimeRepository.save(ReservationTime.create(LocalTime.now().plusHours(1)));
        Theme savedTheme = themeRepository.save(Theme.create("공포", "아니", "https://good.com/thumb-nail/1"));
        Reservation savedReservation = reservationRepository.save(Reservation.create(
                "인직",
                LocalDate.now(),
                savedTime,
                savedTheme
        ));

        // when
        var reservations = reservationService.getReservations();

        // then
        assertThat(reservations).containsExactly(savedReservation);
    }

    @Test
    @DisplayName("날짜와 테마를 기반으로 예약을 조회한다")
    void getReservations_success_with_date_and_theme() {
        // given
        LocalDate date = LocalDate.now();
        ReservationTime savedTime = reservationTimeRepository.save(ReservationTime.create(LocalTime.now().plusHours(1)));
        Theme savedTheme = themeRepository.save(Theme.create("공포", "아니", "https://good.com/thumb-nail/1"));
        Reservation savedReservation = reservationRepository.save(Reservation.create(
                "인직",
                date,
                savedTime,
                savedTheme
        ));

        // when
        var reservations = reservationService.getReservationsByDateAndTheme(date, savedTheme.getId());

        // then
        assertThat(reservations).containsExactly(savedReservation);
    }

    @Test
    @DisplayName("예약을 삭제한다")
    void deleteReservation_success() {
        // given
        ReservationTime savedTime = reservationTimeRepository.save(ReservationTime.create(LocalTime.now().plusHours(1)));
        Theme savedTheme = themeRepository.save(Theme.create("공포", "아니", "https://good.com/thumb-nail/1"));
        Reservation savedReservation = reservationRepository.save(Reservation.create(
                "인직",
                LocalDate.now(),
                savedTime,
                savedTheme
        ));

        // when
        reservationService.deleteReservation(savedReservation.getId());

        // then
        assertThat(reservationRepository.findById(savedReservation.getId())).isEmpty();
    }

    @Test
    @DisplayName("중복 예약이 있으면 예외를 반환한다")
    void saveReservation_fail_with_duplicate_reservation() {
        // given
        ReservationTime savedTime = reservationTimeRepository.save(ReservationTime.create(LocalTime.now().plusHours(1)));
        Theme savedTheme = themeRepository.save(Theme.create("공포", "아니", "https://good.com/thumb-nail/1"));
        ReservationRequest request = new ReservationRequest(
                "흑곰",
                LocalDate.now(),
                savedTime.getId(),
                savedTheme.getId()
        );
        reservationService.saveReservation(
                request.name(),
                request.date(),
                request.timeId(),
                request.themeId()
        );

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(
                request.name(),
                request.date(),
                request.timeId(),
                request.themeId())
        ).isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("이미 지난 날짜로 예약 시 예외 반환")
    void saveReservation_fail_with_past_date() {
        // given
        ReservationTime savedTime = reservationTimeRepository.save(ReservationTime.create(LocalTime.now().plusHours(1)));
        Theme savedTheme = themeRepository.save(Theme.create("공포", "아니", "https://good.com/thumb-nail/1"));
        ReservationRequest request = new ReservationRequest(
                "흑곰",
                LocalDate.now().minusDays(1),
                savedTime.getId(),
                savedTheme.getId()
        );

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(
                request.name(),
                request.date(),
                request.timeId(),
                request.themeId())
        )
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("이미 지난 날짜입니다.");
    }

    @Test
    @DisplayName("현재 시간보다 이전인 날짜로 예외 반환")
    void saveReservation_fail_with_past_time() {
        // given
        ReservationTime savedPastTime = reservationTimeRepository.save(ReservationTime.create(LocalTime.now().minusHours(1)));
        Theme savedTheme = themeRepository.save(Theme.create("공포", "아니", "https://good.com/thumb-nail/1"));
        ReservationRequest request = new ReservationRequest(
                "흑곰",
                LocalDate.now(),
                savedPastTime.getId(),
                savedTheme.getId()
        );

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(
                request.name(),
                request.date(),
                request.timeId(),
                request.themeId())
        )
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("예약 시간이 현재보다 이전일 수 없습니다.");
    }
}
