package roomescape.application;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationRepository;
import roomescape.entity.ReservationTime;
import roomescape.entity.ReservationTimeRepository;
import roomescape.entity.Theme;
import roomescape.entity.ThemeRepository;
import roomescape.fake.FakeReservationRepository;
import roomescape.fake.FakeReservationTimeRepository;
import roomescape.fake.FakeThemeRepository;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.ConflictException;
import roomescape.global.exception.customException.DomainRuleViolationException;
import roomescape.global.exception.customException.NotFoundException;
import roomescape.presentation.dto.ReservationRequest;

class ReservationServiceTest {

    private static final String TESTER_NAME = "홍길동";

    private final ReservationRepository reservationRepository = new FakeReservationRepository();
    private final ReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
    private final ThemeRepository themeRepository = new FakeThemeRepository();

    private ReservationService reservationService;
    private ReservationTime savedTime;
    private Theme savedTheme;


    @BeforeEach
    void setUp() {
        reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository);

        savedTime = reservationTimeRepository.save(ReservationTime.createWithNullId(LocalTime.now().plusHours(3)));
        savedTheme = themeRepository.save(
                Theme.createWithNullId("테스트-테마", "테스트-테마-설명", "https://test.com/thumb-nail/1"));
    }

    @Test
    @DisplayName("예약을 저장한다")
    void save_success() {
        // given
        ReservationRequest request = new ReservationRequest(TESTER_NAME, LocalDate.now(), savedTime.id(),
                savedTheme.id());

        // when & then
        assertThatCode(
                () -> reservationService.save(request.name(), request.date(), request.timeId(), request.themeId())
        ).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간으로 예약하면 예외가 전파된다")
    void save_fail_with_not_found_time() {
        // given
        Long notExistTimeId = 999L;
        Long notExistThemeId = 999L;

        ReservationRequest request = new ReservationRequest(TESTER_NAME, LocalDate.now(), notExistTimeId,
                notExistThemeId);

        // when & then
        assertThatThrownBy(
                () -> reservationService.save(request.name(), request.date(), request.timeId(), request.themeId())
        ).isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.RESERVATION_TIME_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("지나간 날짜로 예약하면 오류가 발생한다")
    void save_fail_with_prev_date() {
        // given
        LocalDate prevDate = LocalDate.now().minusDays(1);

        // when & then
        assertThatThrownBy(
                () -> reservationService.save(
                        TESTER_NAME,
                        prevDate,
                        savedTime.id(),
                        savedTheme.id()
                )
        )
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessage(ErrorCode.PAST_DATE_OR_TIME.getMessage());

    }

    @Test
    @DisplayName("지나간 시간으로 예약하면 오류가 발생한다")
    void save_fail_with_prev_time() {
        // given
        ReservationTime pastTime = reservationTimeRepository.save(
                ReservationTime.createWithNullId(LocalTime.now().minusHours(1)));

        // when & then
        assertThatThrownBy(
                () -> reservationService.save(
                        TESTER_NAME,
                        LocalDate.now(),
                        pastTime.id(),
                        savedTheme.id()
                )
        )
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessage(ErrorCode.PAST_DATE_OR_TIME.getMessage());

    }

    @Test
    @DisplayName("존재하지 않는 테마 ID로 예약하면 예외가 전파된다")
    void save_fail_with_not_found_theme() {
        // given
        Long notExistTimeId = 999L;
        Long notExistThemeId = 999L;

        ReservationRequest request = new ReservationRequest(TESTER_NAME, LocalDate.now(), notExistTimeId,
                notExistThemeId);

        // when & then
        assertThatThrownBy(
                () -> reservationService.save(request.name(), request.date(), request.timeId(), request.themeId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.RESERVATION_TIME_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("같은 날짜, 시간, 테마로 두 번 예약하면 예외가 발생한다")
    void save_fail_when_duplicated() {
        // given
        LocalDate date = LocalDate.now();
        reservationService.save(TESTER_NAME, date, savedTime.id(), savedTheme.id());

        // when & then
        assertThatThrownBy(
                () -> reservationService.save(TESTER_NAME, date, savedTime.id(), savedTheme.id())
        ).isInstanceOf(ConflictException.class)
                .hasMessage(ErrorCode.RESERVATION_DUPLICATED.getMessage());
    }

    @Test
    @DisplayName("예약자 성함을 기반으로 예약 목록 조회 시 값이 존재한다면 오류가 발생하지 않는다")
    void findByName_success() {
        reservationService.save(TESTER_NAME, LocalDate.now(), savedTime.id(), savedTheme.id());

        // when & then
        assertThatCode(() -> reservationService.findByName(TESTER_NAME))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("예약자 성함을 기반으로 예약 목록 조회 시 값이 존재하지 않아도 오류가 발생하지 않는다")
    void findByName_success_even_though_not_exist() {
        // when & then
        assertThatCode(() -> reservationService.findByName(TESTER_NAME))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("예약 목록을 조회한다")
    void findAll_success() {

        // given
        reservationRepository.save(Reservation.createWithNullId(
                TESTER_NAME,
                LocalDate.now(),
                savedTime,
                savedTheme
        ));

        // when & then
        assertThatCode(() -> reservationService.findAll())
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("날짜와 테마를 기반으로 예약을 조회한다")
    void findAll_success_with_date_and_theme() {
        // given
        reservationRepository.save(Reservation.createWithNullId(
                TESTER_NAME,
                LocalDate.now(),
                savedTime,
                savedTheme
        ));

        // when & then
        assertThatCode(() -> reservationService.findAllByDateAndThemeId(LocalDate.now(), savedTheme.id()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("예약을 삭제한다")
    void deleteById_success() {
        // given
        Reservation savedReservation = reservationRepository.save(Reservation.createWithNullId(
                TESTER_NAME,
                LocalDate.now(),
                savedTime,
                savedTheme
        ));

        // when & then
        assertThatCode(() -> reservationService.deleteById(savedReservation.id()))
                .doesNotThrowAnyException();
    }
}
