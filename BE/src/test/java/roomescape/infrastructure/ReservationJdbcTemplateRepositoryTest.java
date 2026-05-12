package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;

@JdbcTest
@Import(ReservationJdbcTemplateRepository.class)
class ReservationJdbcTemplateRepositoryTest {

    private static final LocalDate DATE_5_5 = LocalDate.of(2026, 5, 5);
    private static final LocalDate DATE_5_6 = LocalDate.of(2026, 5, 6);

    private final ReservationJdbcTemplateRepository reservationRepository;

    @Autowired
    ReservationJdbcTemplateRepositoryTest(ReservationJdbcTemplateRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Test
    @DisplayName("예약을 저장하면 생성된 ID를 포함한 예약 객체를 반환한다.")
    @Sql(scripts = {
            "/sql/cleanup.sql",
            "/sql/infrastructure/reservation/save-fixtures.sql"
    })
    void save_success() {
        // given
        ReservationTime time = ReservationTime.createWithId(1L, LocalTime.of(10, 0));
        Theme theme = Theme.createWithId(1L, "테마", "설명", "https://thumb.com");
        Reservation reservation = Reservation.createWithNullId("홍길동", DATE_5_5, time, theme);

        // when
        Reservation savedReservation = reservationRepository.save(reservation);

        // then
        Assertions.assertNotNull(savedReservation.id());
        Assertions.assertEquals("홍길동", savedReservation.name());
    }

    @Test
    @DisplayName("전체 예약 목록을 조회한다.")
    @Sql(scripts = {
            "/sql/cleanup.sql",
            "/sql/infrastructure/reservation/find-all-fixtures.sql"
    })
    void findAll_success() {
        // when
        List<Reservation> result = reservationRepository.findAll();

        // then
        Assertions.assertEquals(2, result.size());
    }

    @Test
    @DisplayName("특정 이름으로 된 예약이 존재하면 이름을 기반으로 예약을 조회 잘 한다")
    @Sql(scripts = {
            "/sql/cleanup.sql",
            "/sql/infrastructure/reservation/find-by-name-fixtures.sql"
    })
    void findByName_success() {
        // when
        String targetName = "홍길동";
        List<Reservation> result = reservationRepository.findByName(targetName);

        // then
        Assertions.assertEquals(2, result.size());
    }

    @Test
    @DisplayName("특정 이름으로 된 예약이 존재하지 않으면 빈 리스트를 반환한다")
    @Sql(scripts = {
            "/sql/cleanup.sql",
    })
    void findByName_fail_not_exist() {
        // when
        String targetName = "존재하지않음";
        List<Reservation> result = reservationRepository.findByName(targetName);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("date만 있으면 해당 날짜의 예약만 조회한다.")
    @Sql(scripts = {
            "/sql/cleanup.sql",
            "/sql/infrastructure/reservation/filter-by-date-fixtures.sql"
    })
    void findByDateAndThemeId_filter_by_date_only() {
        // when
        List<Reservation> result = reservationRepository.findByDateAndThemeId(DATE_5_5, null);

        // then
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.stream().allMatch(r -> r.date().equals(DATE_5_5)));
    }

    @Test
    @DisplayName("themeId만 있으면 해당 테마의 예약만 조회한다.")
    @Sql(scripts = {
            "/sql/cleanup.sql",
            "/sql/infrastructure/reservation/filter-by-theme-fixtures.sql"
    })
    void findByDateAndThemeId_filter_by_theme_only() {
        // when
        List<Reservation> result = reservationRepository.findByDateAndThemeId(null, 1L);

        // then
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.stream().allMatch(r -> r.theme().id().equals(1L)));
    }

    @Test
    @DisplayName("date, themeId 모두 있으면 두 조건 모두 일치하는 예약만 조회한다.")
    @Sql(scripts = {
            "/sql/cleanup.sql",
            "/sql/infrastructure/reservation/filter-by-date-and-theme-fixtures.sql"
    })
    void findByDateAndThemeId_filter_by_date_and_theme() {
        // when
        List<Reservation> result = reservationRepository.findByDateAndThemeId(DATE_5_5, 1L);

        // then
        Assertions.assertEquals(1, result.size());
        Reservation only = result.get(0);
        Assertions.assertEquals(DATE_5_5, only.date());
        Assertions.assertEquals(1L, only.theme().id());
    }

    @Test
    @DisplayName("일치하는 예약이 없으면 빈 목록을 반환한다.")
    @Sql(scripts = {
            "/sql/cleanup.sql",
            "/sql/infrastructure/reservation/no-match-fixtures.sql"
    })
    void findByDateAndThemeId_returns_empty_when_no_match() {
        // when
        List<Reservation> result = reservationRepository.findByDateAndThemeId(DATE_5_6, 2L);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("특정 시간 ID를 참조하는 예약이 존재하면 true를 반환한다.")
    @Sql(scripts = {
            "/sql/cleanup.sql",
            "/sql/infrastructure/reservation/exists-by-time-fixtures.sql"
    })
    void existsByReservationTimeId_returns_true_when_exists() {
        // when
        boolean exists = reservationRepository.existsByReservationTimeId(1L);

        // then
        Assertions.assertTrue(exists);
    }

    @Test
    @DisplayName("특정 시간 ID를 참조하는 예약이 존재하지 않으면 false를 반환한다.")
    @Sql(scripts = {
            "/sql/cleanup.sql",
            "/sql/infrastructure/reservation/exists-by-time-fixtures.sql"
    })
    void existsByReservationTimeId_returns_false_when_not_exists() {
        // when
        boolean exists = reservationRepository.existsByReservationTimeId(999L);

        // then
        Assertions.assertFalse(exists);
    }

    @Test
    @DisplayName("특정 테마 ID를 참조하는 예약이 존재하면 true를 반환한다.")
    @Sql(scripts = {
            "/sql/cleanup.sql",
            "/sql/infrastructure/reservation/exists-by-theme-fixtures.sql"
    })
    void existsByThemeId_returns_true_when_exists() {
        // when
        boolean exists = reservationRepository.existsByThemeId(1L);

        // then
        Assertions.assertTrue(exists);
    }

    @Test
    @DisplayName("특정 테마 ID를 참조하는 예약이 존재하지 않으면 false를 반환한다.")
    @Sql(scripts = {
            "/sql/cleanup.sql",
            "/sql/infrastructure/reservation/exists-by-theme-fixtures.sql"
    })
    void existsByThemeId_returns_false_when_not_exists() {
        // when
        boolean exists = reservationRepository.existsByThemeId(999L);

        // then
        Assertions.assertFalse(exists);
    }

    @Test
    @DisplayName("동일 date/time/theme 조합의 예약이 존재하면 true를 반환한다.")
    @Sql(scripts = {
            "/sql/cleanup.sql",
            "/sql/infrastructure/reservation/exists-by-date-time-theme-fixtures.sql"
    })
    void existsByDateAndTimeIdAndThemeId_returns_true_when_exists() {
        // when
        boolean exists = reservationRepository.existsByDateAndTimeIdAndThemeId(DATE_5_5, 1L, 1L);

        // then
        Assertions.assertTrue(exists);
    }

    @Test
    @DisplayName("동일 date/time/theme 조합의 예약이 없으면 false를 반환한다.")
    @Sql(scripts = {
            "/sql/cleanup.sql",
            "/sql/infrastructure/reservation/exists-by-date-time-theme-fixtures.sql"
    })
    void existsByDateAndTimeIdAndThemeId_returns_false_when_not_exists() {
        // when
        boolean exists = reservationRepository.existsByDateAndTimeIdAndThemeId(DATE_5_6, 1L, 1L);

        // then
        Assertions.assertFalse(exists);
    }

    @Test
    @DisplayName("동일 date/time/theme 조합으로 두 번 INSERT 하면 DB UNIQUE 제약조건에 의해 실패한다.")
    @Sql(scripts = {
            "/sql/cleanup.sql",
            "/sql/infrastructure/reservation/exists-by-date-time-theme-fixtures.sql"
    })
    void save_fail_when_duplicated_at_db_level() {
        // given
        ReservationTime time = ReservationTime.createWithId(1L, LocalTime.of(10, 0));
        Theme theme = Theme.createWithId(1L, "테마", "설명", "thumb");
        Reservation duplicate = Reservation.createWithNullId("다른사람", DATE_5_5, time, theme);

        // when & then
        assertThatThrownBy(() -> reservationRepository.save(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("ID를 기반으로 예약을 삭제한다.")
    @Sql(scripts = {
            "/sql/cleanup.sql",
            "/sql/infrastructure/reservation/delete-fixtures.sql"
    })
    void deleteById_success() {
        // when
        reservationRepository.deleteById(1L);

        // then
        Assertions.assertTrue(reservationRepository.findById(1L).isEmpty());
    }

    @Test
    @DisplayName("예약의 날짜와 시간을 수정한다.")
    @Sql(scripts = {
            "/sql/cleanup.sql",
            "/sql/infrastructure/reservation/update-fixtures.sql"
    })
    void update_success() {
        // given
        Reservation existing = reservationRepository.findById(1L).get();
        ReservationTime newTime = ReservationTime.createWithId(2L, LocalTime.of(11, 0));

        Reservation patched = existing.patch(builder -> {
            builder.date(DATE_5_6);
            builder.time(newTime);
        });

        // when
        reservationRepository.update(patched);

        // then
        Reservation updated = reservationRepository.findById(1L).get();
        Assertions.assertEquals(DATE_5_6, updated.date());
        Assertions.assertEquals(2L, updated.time().id());
        Assertions.assertEquals("홍길동", updated.name()); // 다른 필드는 유지되어야 함
    }
}
