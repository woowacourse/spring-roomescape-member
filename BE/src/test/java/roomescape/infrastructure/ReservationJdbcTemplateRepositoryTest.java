package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;

@JdbcTest
@Import({
        ReservationJdbcTemplateRepository.class,
        ReservationTimeJdbcTemplateRepository.class,
        ThemeJdbcTemplateRepository.class
})
class ReservationJdbcTemplateRepositoryTest {

    private static final LocalDate TOMORROW = LocalDate.now().plusDays(1);
    private static final LocalDate DAY_AFTER_TOMORROW = LocalDate.now().plusDays(2);

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

    private Theme saveTheme() {
        return themeRepository.save(Theme.createWithNullId("테마", "설명", "https://thumb.com"));
    }

    private ReservationTime saveTime(LocalTime time) {
        return timeRepository.save(ReservationTime.createWithNullId(time));
    }

    @Test
    @DisplayName("예약을 저장하면 생성된 ID를 포함한 예약 객체를 반환한다.")
    void save_success() {
        // given
        ReservationTime time = saveTime(LocalTime.of(10, 0));
        Theme theme = saveTheme();
        Reservation reservation = Reservation.createWithNullId("홍길동", DAY_AFTER_TOMORROW, time, theme);

        // when
        Reservation savedReservation = reservationRepository.save(reservation);

        // then
        Assertions.assertNotNull(savedReservation.id());
        Assertions.assertEquals("홍길동", savedReservation.name());
    }

    @Test
    @DisplayName("전체 예약 목록을 조회한다.")
    void findAll_success() {
        // given
        ReservationTime time = saveTime(LocalTime.of(10, 0));
        Theme theme = saveTheme();
        reservationRepository.save(Reservation.createWithNullId("홍길동1", TOMORROW, time, theme));
        reservationRepository.save(Reservation.createWithNullId("홍길동2", DAY_AFTER_TOMORROW, time, theme));

        // when
        List<Reservation> result = reservationRepository.findAll();

        // then
        Assertions.assertEquals(2, result.size());
    }

    @Test
    @DisplayName("특정 이름으로 된 예약이 존재하면 이름을 기반으로 예약을 조회 잘 한다")
    void findByName_success() {
        // given
        ReservationTime time1 = saveTime(LocalTime.of(10, 0));
        ReservationTime time2 = saveTime(LocalTime.of(11, 0));
        Theme theme = saveTheme();
        reservationRepository.save(Reservation.createWithNullId("홍길동", TOMORROW, time1, theme));
        reservationRepository.save(Reservation.createWithNullId("홍길동", DAY_AFTER_TOMORROW, time1, theme));
        reservationRepository.save(Reservation.createWithNullId("다른사람", DAY_AFTER_TOMORROW, time2, theme));

        // when
        String targetName = "홍길동";
        List<Reservation> result = reservationRepository.findByName(targetName);

        // then
        Assertions.assertEquals(2, result.size());
    }

    @Test
    @DisplayName("특정 이름으로 된 예약이 존재하지 않으면 빈 리스트를 반환한다")
    void findByName_fail_not_exist() {
        // when
        String targetName = "존재하지않음";
        List<Reservation> result = reservationRepository.findByName(targetName);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("date만 있으면 해당 날짜의 예약만 조회한다.")
    void findByDateAndThemeId_filter_by_date_only() {
        // given
        ReservationTime time1 = saveTime(LocalTime.of(10, 0));
        ReservationTime time2 = saveTime(LocalTime.of(11, 0));
        Theme theme = saveTheme();
        reservationRepository.save(Reservation.createWithNullId("A", TOMORROW, time1, theme));
        reservationRepository.save(Reservation.createWithNullId("B", TOMORROW, time2, theme));
        reservationRepository.save(Reservation.createWithNullId("C", DAY_AFTER_TOMORROW, time1, theme));

        // when
        List<Reservation> result = reservationRepository.findByDateAndThemeId(TOMORROW, null);

        // then
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.stream().allMatch(r -> r.date().equals(TOMORROW)));
    }

    @Test
    @DisplayName("themeId만 있으면 해당 테마의 예약만 조회한다.")
    void findByDateAndThemeId_filter_by_theme_only() {
        // given
        ReservationTime time1 = saveTime(LocalTime.of(10, 0));
        ReservationTime time2 = saveTime(LocalTime.of(11, 0));
        Theme theme1 = themeRepository.save(Theme.createWithNullId("테마1", "설명", "thumb"));
        Theme theme2 = themeRepository.save(Theme.createWithNullId("테마2", "설명", "thumb"));
        reservationRepository.save(Reservation.createWithNullId("A", TOMORROW, time1, theme1));
        reservationRepository.save(Reservation.createWithNullId("B", DAY_AFTER_TOMORROW, time2, theme1));
        reservationRepository.save(Reservation.createWithNullId("C", TOMORROW, time1, theme2));

        // when
        List<Reservation> result = reservationRepository.findByDateAndThemeId(null, theme1.id());

        // then
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.stream().allMatch(r -> r.theme().id().equals(theme1.id())));
    }

    @Test
    @DisplayName("date, themeId 모두 있으면 두 조건 모두 일치하는 예약만 조회한다.")
    void findByDateAndThemeId_filter_by_date_and_theme() {
        // given
        ReservationTime time1 = saveTime(LocalTime.of(10, 0));
        ReservationTime time2 = saveTime(LocalTime.of(11, 0));
        Theme theme1 = themeRepository.save(Theme.createWithNullId("테마1", "설명", "thumb"));
        Theme theme2 = themeRepository.save(Theme.createWithNullId("테마2", "설명", "thumb"));
        reservationRepository.save(Reservation.createWithNullId("A", TOMORROW, time1, theme1));
        reservationRepository.save(Reservation.createWithNullId("B", DAY_AFTER_TOMORROW, time2, theme1));
        reservationRepository.save(Reservation.createWithNullId("C", TOMORROW, time1, theme2));

        // when
        List<Reservation> result = reservationRepository.findByDateAndThemeId(TOMORROW, theme1.id());

        // then
        Assertions.assertEquals(1, result.size());
        Reservation only = result.get(0);
        Assertions.assertEquals(TOMORROW, only.date());
        Assertions.assertEquals(theme1.id(), only.theme().id());
    }

    @Test
    @DisplayName("일치하는 예약이 없으면 빈 목록을 반환한다.")
    void findByDateAndThemeId_returns_empty_when_no_match() {
        // given
        ReservationTime time = saveTime(LocalTime.of(10, 0));
        Theme theme = saveTheme();
        reservationRepository.save(Reservation.createWithNullId("A", TOMORROW, time, theme));

        // when
        List<Reservation> result = reservationRepository.findByDateAndThemeId(DAY_AFTER_TOMORROW, theme.id() + 999L);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("특정 시간 ID를 참조하는 예약이 존재하면 true를 반환한다.")
    void existsByReservationTimeId_returns_true_when_exists() {
        // given
        ReservationTime time = saveTime(LocalTime.of(10, 0));
        Theme theme = saveTheme();
        reservationRepository.save(Reservation.createWithNullId("A", TOMORROW, time, theme));

        // when
        boolean exists = reservationRepository.existsByReservationTimeId(time.id());

        // then
        Assertions.assertTrue(exists);
    }

    @Test
    @DisplayName("특정 시간 ID를 참조하는 예약이 존재하지 않으면 false를 반환한다.")
    void existsByReservationTimeId_returns_false_when_not_exists() {
        // when
        boolean exists = reservationRepository.existsByReservationTimeId(999L);

        // then
        Assertions.assertFalse(exists);
    }

    @Test
    @DisplayName("특정 테마 ID를 참조하는 예약이 존재하면 true를 반환한다.")
    void existsByThemeId_returns_true_when_exists() {
        // given
        ReservationTime time = saveTime(LocalTime.of(10, 0));
        Theme theme = saveTheme();
        reservationRepository.save(Reservation.createWithNullId("A", TOMORROW, time, theme));

        // when
        boolean exists = reservationRepository.existsByThemeId(theme.id());

        // then
        Assertions.assertTrue(exists);
    }

    @Test
    @DisplayName("특정 테마 ID를 참조하는 예약이 존재하지 않으면 false를 반환한다.")
    void existsByThemeId_returns_false_when_not_exists() {
        // when
        boolean exists = reservationRepository.existsByThemeId(999L);

        // then
        Assertions.assertFalse(exists);
    }

    @Test
    @DisplayName("동일 date/time/theme 조합의 예약이 존재하면 true를 반환한다.")
    void existsByDateAndTimeIdAndThemeId_returns_true_when_exists() {
        // given
        ReservationTime time = saveTime(LocalTime.of(10, 0));
        Theme theme = saveTheme();
        reservationRepository.save(Reservation.createWithNullId("A", DAY_AFTER_TOMORROW, time, theme));

        // when
        boolean exists = reservationRepository.existsByDateAndTimeIdAndThemeId(DAY_AFTER_TOMORROW, time.id(),
                theme.id());

        // then
        Assertions.assertTrue(exists);
    }

    @Test
    @DisplayName("동일 date/time/theme 조합의 예약이 없으면 false를 반환한다.")
    void existsByDateAndTimeIdAndThemeId_returns_false_when_not_exists() {
        // given
        ReservationTime time = saveTime(LocalTime.of(10, 0));
        Theme theme = saveTheme();
        reservationRepository.save(Reservation.createWithNullId("A", DAY_AFTER_TOMORROW, time, theme));

        // when
        boolean exists = reservationRepository.existsByDateAndTimeIdAndThemeId(TOMORROW, time.id(), theme.id());

        // then
        Assertions.assertFalse(exists);
    }

    @Test
    @DisplayName("동일 date/time/theme 조합으로 두 번 INSERT 하면 DB UNIQUE 제약조건에 의해 실패한다.")
    void save_fail_when_duplicated_at_db_level() {
        // given
        ReservationTime time = saveTime(LocalTime.of(10, 0));
        Theme theme = saveTheme();
        reservationRepository.save(Reservation.createWithNullId("홍길동", DAY_AFTER_TOMORROW, time, theme));

        Reservation duplicate = Reservation.createWithNullId("다른사람", DAY_AFTER_TOMORROW, time, theme);

        // when & then
        assertThatThrownBy(() -> reservationRepository.save(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("ID를 기반으로 예약을 삭제한다.")
    void deleteById_success() {
        // given
        ReservationTime time = saveTime(LocalTime.of(10, 0));
        Theme theme = saveTheme();
        Reservation saved = reservationRepository.save(Reservation.createWithNullId("홍길동", TOMORROW, time, theme));

        // when
        reservationRepository.deleteById(saved.id());

        // then
        Assertions.assertTrue(reservationRepository.findById(saved.id()).isEmpty());
    }

    @Test
    @DisplayName("예약의 날짜와 시간을 수정한다.")
    void update_success() {
        // given
        ReservationTime time1 = saveTime(LocalTime.of(10, 0));
        ReservationTime time2 = saveTime(LocalTime.of(11, 0));
        Theme theme = saveTheme();
        Reservation saved = reservationRepository.save(Reservation.createWithNullId("홍길동", TOMORROW, time1, theme));

        Reservation patched = new Reservation(
                saved.id(),
                saved.name(),
                DAY_AFTER_TOMORROW,
                time2,
                saved.theme()
        );

        // when
        reservationRepository.update(patched);

        // then
        Optional<Reservation> updated = reservationRepository.findById(saved.id());
        Assertions.assertTrue(updated.isPresent());
        Assertions.assertEquals(DAY_AFTER_TOMORROW, updated.get().date());
        Assertions.assertEquals(time2.id(), updated.get().time().id());
        Assertions.assertEquals("홍길동", updated.get().name());
    }
}
