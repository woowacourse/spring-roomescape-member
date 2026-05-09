package roomescape.infrastructure;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;


@JdbcTest
@Import({
        ReservationJdbcTemplateRepository.class,
        ReservationTimeJdbcTemplateRepository.class,
        ThemeJdbcTemplateRepository.class
})
class ReservationJdbcTemplateRepositoryTest {

    private static final String TEST_NAME = "홍길동";
    private static final LocalDate DATE_TODAY = LocalDate.now();
    private static final LocalDate DATE_TOMORROW = LocalDate.now().plusDays(1);

    private final ReservationJdbcTemplateRepository reservationRepository;
    private final ReservationTimeJdbcTemplateRepository timeRepository;
    private final ThemeJdbcTemplateRepository themeRepository;

    @Autowired
    ReservationJdbcTemplateRepositoryTest(ReservationJdbcTemplateRepository reservationRepository,
                                          ReservationTimeJdbcTemplateRepository timeRepository,
                                          ThemeJdbcTemplateRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    private ReservationTime savedTime1;
    private ReservationTime savedTime2;
    private Theme savedTheme1;
    private Theme savedTheme2;

    @BeforeEach
    void setUp() {
        savedTime1 = timeRepository.save(ReservationTime.createWithNullId(LocalTime.of(1, 0)));
        savedTime2 = timeRepository.save(ReservationTime.createWithNullId(LocalTime.of(2, 0)));
        savedTheme1 = themeRepository.save(
                Theme.createWithNullId("테스트 테마1", "테스트 테마 설명1", "https://good.com/thumb-nail/1"));
        savedTheme2 = themeRepository.save(
                Theme.createWithNullId("테스트 테마2", "테스트 테마 설명2", "https://good.com/thumb-nail/2"));
    }

    @Test
    @DisplayName("예약을 저장하면 생성된 ID를 포함한 예약 객체를 반환한다.")
    void save_success() {
        // given
        Reservation reservation = Reservation.createWithNullId(TEST_NAME, DATE_TODAY, savedTime1, savedTheme1);

        // when
        Reservation savedReservation = reservationRepository.save(reservation);

        // then
        Assertions.assertNotNull(savedReservation.id());
        Assertions.assertEquals(TEST_NAME, savedReservation.name());
    }

    @Test
    @DisplayName("전체 예약 목록을 조회한다.")
    void findByDateAndThemeId_findAll_when_both_null() {
        // given
        reservationRepository.save(Reservation.createWithNullId(TEST_NAME + "1", DATE_TODAY, savedTime1, savedTheme1));
        reservationRepository.save(Reservation.createWithNullId(TEST_NAME + "2", DATE_TOMORROW, savedTime2, savedTheme2));

        // when
        List<Reservation> result = reservationRepository.findAll();

        // then
        Assertions.assertEquals(2, result.size());
    }

    @Test
    @DisplayName("date만 있으면 해당 날짜의 예약만 조회한다.")
    void findByDateAndThemeId_filter_by_date_only() {
        // given
        reservationRepository.save(Reservation.createWithNullId(TEST_NAME + "1", DATE_TODAY, savedTime1, savedTheme1));
        reservationRepository.save(Reservation.createWithNullId(TEST_NAME + "2", DATE_TODAY, savedTime2, savedTheme2));
        reservationRepository.save(Reservation.createWithNullId(TEST_NAME + "3", DATE_TOMORROW, savedTime1, savedTheme1));

        // when
        List<Reservation> result = reservationRepository.findByDateAndThemeId(DATE_TODAY, null);

        // then
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.stream().allMatch(r -> r.date().equals(DATE_TODAY)));
    }

    @Test
    @DisplayName("themeId만 있으면 해당 테마의 예약만 조회한다.")
    void findByDateAndThemeId_filter_by_theme_only() {
        // given
        reservationRepository.save(Reservation.createWithNullId(TEST_NAME + "1", DATE_TODAY, savedTime1, savedTheme1));
        reservationRepository.save(Reservation.createWithNullId(TEST_NAME + "2", DATE_TOMORROW, savedTime2, savedTheme1));
        reservationRepository.save(Reservation.createWithNullId(TEST_NAME + "3", DATE_TODAY, savedTime1, savedTheme2));

        // when
        List<Reservation> result = reservationRepository.findByDateAndThemeId(null, savedTheme1.id());

        // then
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.stream().allMatch(r -> r.theme().id().equals(savedTheme1.id())));
    }

    @Test
    @DisplayName("date, themeId 모두 있으면 두 조건 모두 일치하는 예약만 조회한다.")
    void findByDateAndThemeId_filter_by_date_and_theme() {
        // given
        reservationRepository.save(Reservation.createWithNullId(TEST_NAME + "1", DATE_TODAY, savedTime1, savedTheme1));
        reservationRepository.save(Reservation.createWithNullId(TEST_NAME + "2", DATE_TODAY, savedTime2, savedTheme2));
        reservationRepository.save(Reservation.createWithNullId(TEST_NAME + "3", DATE_TOMORROW, savedTime1, savedTheme1));

        // when
        List<Reservation> result = reservationRepository.findByDateAndThemeId(DATE_TODAY, savedTheme1.id());

        // then
        Assertions.assertEquals(1, result.size());
        Reservation only = result.get(0);
        Assertions.assertEquals(DATE_TODAY, only.date());
        Assertions.assertEquals(savedTheme1.id(), only.theme().id());
    }

    @Test
    @DisplayName("일치하는 예약이 없으면 빈 목록을 반환한다.")
    void findByDateAndThemeId_returns_empty_when_no_match() {
        // given
        reservationRepository.save(Reservation.createWithNullId(TEST_NAME, DATE_TODAY, savedTime1, savedTheme1));

        // when
        List<Reservation> result = reservationRepository.findByDateAndThemeId(DATE_TOMORROW, savedTheme2.id());

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("특정 시간 ID를 참조하는 예약이 존재하는지 확인한다.")
    void existsByReservationTimeId_success() {
        // given
        reservationRepository.save(Reservation.createWithNullId(TEST_NAME, DATE_TODAY, savedTime1, savedTheme1));

        // when
        boolean exists = reservationRepository.existsByReservationTimeId(savedTime1.id());
        boolean notExists = reservationRepository.existsByReservationTimeId(999L);

        // then
        Assertions.assertTrue(exists);
        Assertions.assertFalse(notExists);
    }

    @Test
    @DisplayName("ID를 기반으로 예약을 삭제한다.")
    void deleteById_success() {
        // given
        Reservation saved = reservationRepository.save(
                Reservation.createWithNullId("삭제대상", DATE_TODAY, savedTime1, savedTheme1)
        );

        // when
        Long deleteTargetId = saved.id();
        reservationRepository.deleteById(deleteTargetId);

        // then
        Optional<Reservation> deleteTargetFindResult = reservationRepository.findAll()
                .stream()
                .filter(reservation -> deleteTargetId.equals(reservation.id()))
                .findAny();

        Assertions.assertTrue(deleteTargetFindResult.isEmpty());
    }
}
