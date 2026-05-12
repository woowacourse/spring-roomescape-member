package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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
        List<Reservation> reservations = reservationService.getReservations();

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
        List<Reservation> reservations = reservationService.getReservationsByDateAndTheme(date, savedTheme.getId());

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
                .hasMessageContaining("지난 날짜로는 예약할 수 없습니다.");
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
                .hasMessageContaining("현재 시각보다 이전 시간으로는 예약할 수 없습니다.");
    }

    @Test
    @DisplayName("이름을 기반으로 자신 예약 목록 조회 기능")
    void getReservationsByName_success() {
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
        List<Reservation> reservations = reservationService.getReservationsByName("인직");

        // then
        assertThat(reservations).containsExactly(savedReservation);
    }

    @Test
    @DisplayName("자신의 예약 날짜 및 시간 수정 기능")
    void updateReservationSchedule_success() {
        // given
        ReservationTime savedTime1 = reservationTimeRepository.save(
                ReservationTime.create(LocalTime.now().plusHours(1))
        );

        ReservationTime savedTime2 = reservationTimeRepository.save(
                ReservationTime.create(LocalTime.now().plusHours(2))
        );

        Theme savedTheme = themeRepository.save(
                Theme.create("공포", "아니", "https://good.com/thumb-nail/1")
        );

        Reservation savedReservation = reservationRepository.save(
                Reservation.create(
                        "인직",
                        LocalDate.now().plusDays(1),
                        savedTime1,
                        savedTheme
                )
        );

        LocalDate changedDate = LocalDate.now().plusDays(2);

        // when
        reservationService.updateReservationSchedule(
                changedDate,
                savedTime2.getId(),
                savedReservation.getId(),
                "인직"
        );

        // then
        Reservation updatedReservation = reservationRepository.findById(savedReservation.getId())
                .orElseThrow();

        assertThat(updatedReservation.getDate()).isEqualTo(changedDate);
        assertThat(updatedReservation.getTime().getId()).isEqualTo(savedTime2.getId());
    }

    @Test
    @DisplayName("존재하지 않는 예약 ID로 수정하면 예외 발생")
    void updateReservationSchedule_fail_with_not_found_reservation() {
        // given
        ReservationTime savedTime = reservationTimeRepository.save(
                ReservationTime.create(LocalTime.now().plusHours(1))
        );

        // when & then
        assertThatThrownBy(() -> reservationService.updateReservationSchedule(
                LocalDate.now().plusDays(1),
                savedTime.getId(),
                999L,
                "인직"
        ))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("수정할 예약을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("본인의 예약이 아니면 수정 시 예외 발생")
    void updateReservationSchedule_fail_with_invalid_owner() {
        // given
        ReservationTime savedTime = reservationTimeRepository.save(
                ReservationTime.create(LocalTime.now().plusHours(1))
        );

        Theme savedTheme = themeRepository.save(
                Theme.create("공포", "설명", "https://good.com")
        );

        Reservation savedReservation = reservationRepository.save(
                Reservation.create(
                        "인직",
                        LocalDate.now().plusDays(1),
                        savedTime,
                        savedTheme
                )
        );

        // when & then
        assertThatThrownBy(() -> reservationService.updateReservationSchedule(
                LocalDate.now().plusDays(2),
                savedTime.getId(),
                savedReservation.getId(),
                "포비"
        ))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("본인의 예약만 수정할 수 있습니다.");
    }

    @Test
    @DisplayName("이미 예약된 시간으로 수정하면 예외 발생")
    void updateReservationSchedule_fail_with_duplicate_reservation() {
        // given
        ReservationTime savedTime1 = reservationTimeRepository.save(
                ReservationTime.create(LocalTime.now().plusHours(1))
        );

        ReservationTime savedTime2 = reservationTimeRepository.save(
                ReservationTime.create(LocalTime.now().plusHours(2))
        );

        Theme savedTheme = themeRepository.save(
                Theme.create("공포", "설명", "https://good.com")
        );

        reservationRepository.save(
                Reservation.create(
                        "브라운",
                        LocalDate.now().plusDays(1),
                        savedTime2,
                        savedTheme
                )
        );

        Reservation myReservation = reservationRepository.save(
                Reservation.create(
                        "인직",
                        LocalDate.now().plusDays(1),
                        savedTime1,
                        savedTheme
                )
        );

        // when & then
        assertThatThrownBy(() -> reservationService.updateReservationSchedule(
                LocalDate.now().plusDays(1),
                savedTime2.getId(),
                myReservation.getId(),
                "인직"
        ))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("이미 예약된 시간입니다. 다른 시간을 선택해 주세요.");
    }

    @Test
    @DisplayName("이름을 기반으로 자신의 예약 삭제 기능")
    void deleteReservationByName_success() {
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
        reservationService.deleteReservationByName(savedReservation.getId(), "인직");

        // then
        assertThat(reservationRepository.findById(savedReservation.getId())).isEmpty();
    }

    @Test
    @DisplayName("예약 ID가 null이면 삭제 시 예외 발생")
    void deleteReservation_fail_with_null_id() {
        assertThatThrownBy(() -> reservationService.deleteReservation(null))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("예약을 식별할 값이 비어있습니다.");
    }

    @Test
    @DisplayName("이름이 비어있으면 자신의 예약 삭제 시 예외 발생")
    void deleteReservationByName_fail_with_blank_name() {
        assertThatThrownBy(() -> reservationService.deleteReservationByName(1L, " "))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("예약자 이름을 입력해 주세요.");
    }
}
