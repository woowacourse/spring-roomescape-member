package roomescape.application;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
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

@Transactional
class ReservationServiceTest {

    private static final String TESTER_NAME = "홍길동";

    private final ReservationRepository reservationRepository = new FakeReservationRepository();
    private final ReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
    private final ThemeRepository themeRepository = new FakeThemeRepository();

    private ReservationService reservationService;
    private ReservationTime savedTime;
    private Theme savedTheme;
    private LocalDate today;
    private LocalDate tomorrow;
    private LocalDate yesterday;


    @BeforeEach
    void setUp() {
        reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository);

        savedTime = reservationTimeRepository.save(ReservationTime.createWithNullId(LocalTime.now().plusHours(3)));
        savedTheme = themeRepository.save(
                Theme.createWithNullId("테스트-테마", "테스트-테마-설명", "https://test.com/thumb-nail/1"));
        today = LocalDate.now();
        tomorrow = today.plusDays(1);
        yesterday = today.minusDays(1);
    }

    @Test
    @DisplayName("예약을 저장한다")
    void save_success() {
        // given
        ReservationRequest request = new ReservationRequest(TESTER_NAME, tomorrow, savedTime.id(),
                savedTheme.id());

        // when & then
        assertThatCode(
                () -> reservationService.save(request.name(), request.date(), request.timeId(), request.themeId())
        ).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("존재하지 않는 의존성(시간, 테마)으로 예약하면 예외가 발생한다")
    void save_fail_with_not_found_dependency() {
        // given
        Long notExistId = 999L;

        // when & then
        assertThatThrownBy(
                () -> reservationService.save(TESTER_NAME, today, notExistId, savedTheme.id())
        ).isInstanceOf(NotFoundException.class);

        assertThatThrownBy(
                () -> reservationService.save(TESTER_NAME, today, savedTime.id(), notExistId)
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("도메인 제약 사항(과거 날짜, 중복 등) 위반 시 예외가 전파된다")
    void save_fail_due_to_domain_rule_propagation() {
        // 1. 과거 날짜 (DomainRuleViolationException 전파)
        assertThatThrownBy(
                () -> reservationService.save(TESTER_NAME, yesterday, savedTime.id(), savedTheme.id())
        ).isInstanceOf(DomainRuleViolationException.class);

        // 2. 중복 예약 (ConflictException 전파)
        reservationService.save(TESTER_NAME, tomorrow, savedTime.id(), savedTheme.id());
        assertThatThrownBy(
                () -> reservationService.save(TESTER_NAME, tomorrow, savedTime.id(), savedTheme.id())
        ).isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("예약자 성함을 기반으로 예약 목록을 조회한다")
    void findByName_success() {
        reservationService.save(TESTER_NAME, tomorrow, savedTime.id(), savedTheme.id());

        assertThatCode(() -> reservationService.findByName(TESTER_NAME))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("전체 예약 목록을 조회한다")
    void findAll_success() {
        reservationService.save(TESTER_NAME, tomorrow, savedTime.id(), savedTheme.id());

        assertThatCode(() -> reservationService.findAll())
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("날짜와 테마를 기반으로 예약을 조회한다")
    void findAll_success_with_date_and_theme() {
        reservationService.save(TESTER_NAME, tomorrow, savedTime.id(), savedTheme.id());

        assertThatCode(() -> reservationService.findAllByDateAndThemeId(tomorrow, savedTheme.id()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("예약을 삭제한다")
    void deleteById_success() {
        // given
        Reservation savedReservation = reservationService.save(TESTER_NAME, tomorrow, savedTime.id(),
                savedTheme.id());

        // when & then
        assertThatCode(() -> reservationService.deleteById(savedReservation.id(), TESTER_NAME))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("삭제 시 도메인 제약 사항 위반 시 예외가 전파된다")
    void deleteById_fail_due_to_domain_rule_propagation() {
        // given
        Reservation savedReservation = reservationRepository.save(Reservation.createWithNullId(
                TESTER_NAME, yesterday, savedTime, savedTheme
        ));

        // when & then
        assertThatThrownBy(
                () -> reservationService.deleteById(savedReservation.id(), TESTER_NAME)
        ).isInstanceOf(DomainRuleViolationException.class);
    }

    @Test
    @DisplayName("예약의 날짜와 시간을 수정한다")
    void update_DateAndTime_success() {
        // given
        Reservation saved = reservationService.save(TESTER_NAME, tomorrow, savedTime.id(),
                savedTheme.id());
        ReservationTime newTime = reservationTimeRepository.save(
                ReservationTime.createWithNullId(LocalTime.now().plusHours(4)));
        LocalDate newDate = tomorrow.plusDays(1);

        // when & then
        assertThatCode(
                () -> reservationService.updateDateAndTime(saved.id(), TESTER_NAME, Optional.of(newDate), Optional.of(newTime.id()))
        ).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("수정 시 도메인 예외(소유권, 제약 위반)가 전파된다")
    void update_DateAndTime_fail_due_to_domain_rule_propagation() {
        // given
        Reservation saved = reservationService.save(TESTER_NAME, tomorrow, savedTime.id(),
                savedTheme.id());

        // 1. 소유권 위반 (NotFoundException 전파)
        assertThatThrownBy(
                () -> reservationService.updateDateAndTime(saved.id(), "다른사람", Optional.empty(), Optional.empty())
        ).isInstanceOf(NotFoundException.class);

        // 2. 과거 날짜 수정 (DomainRuleViolationException 전파)
        assertThatThrownBy(
                () -> reservationService.updateDateAndTime(saved.id(), TESTER_NAME, Optional.of(yesterday), Optional.empty())
        ).isInstanceOf(DomainRuleViolationException.class);
    }
}
