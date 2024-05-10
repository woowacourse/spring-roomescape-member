package roomescape.reservation.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.common.RepositoryTest;
import roomescape.reservation.persistence.ReservationDao;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static roomescape.TestFixture.*;
import static roomescape.member.domain.Role.USER;

class ReservationRepositoryTest extends RepositoryTest {
    private ReservationRepository reservationRepository;
    private SimpleJdbcInsert jdbcInsert;

    @BeforeEach
    void setUp() {
        this.reservationRepository = new ReservationDao(jdbcTemplate, dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");

        String insertTimeSql = "INSERT INTO reservation_time (start_at) VALUES (?)";
        jdbcTemplate.update(insertTimeSql, Time.valueOf(MIA_RESERVATION_TIME));
        String insertThemeSql = "INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?), (?, ?, ?)";
        jdbcTemplate.update(insertThemeSql,
                WOOTECO_THEME_NAME, WOOTECO_THEME_DESCRIPTION, THEME_THUMBNAIL,
                HORROR_THEME_NAME, HORROR_THEME_DESCRIPTION, THEME_THUMBNAIL);
        String insertMemberSql = "INSERT INTO member (name, email, password, role) " +
                "VALUES (?, ?, ?, ?), (?, ?, ?, ?)";
        jdbcTemplate.update(insertMemberSql,
                MIA_NAME, MIA_EMAIL, TEST_PASSWORD, USER.name(),
                TOMMY_NAME, TOMMY_EMAIL, TEST_PASSWORD, USER.name());
    }

    @Test
    @DisplayName("예약을 저장한다.")
    void save() {
        // given
        Reservation reservation = MIA_RESERVATION(
                new ReservationTime(1L, MIA_RESERVATION_TIME), WOOTECO_THEME(1L), USER_MIA(1L));

        // when
        Reservation savedReservation = reservationRepository.save(reservation);

        // then
        assertThat(savedReservation.getId()).isNotNull();
    }

    @Test
    @DisplayName("동일 시간대의 예약이 존재하는지 조회한다.")
    void existByDateAndTimeIdAndThemeId() {
        // given
        Long timeId = 1L;
        Long themeId = 1L;
        insertReservation(1L, MIA_RESERVATION_DATE, timeId, themeId);

        // when
        boolean existByDateAndTimeIdAndThemeId = reservationRepository.existByDateAndTimeIdAndThemeId(
                MIA_RESERVATION_DATE, timeId, themeId);

        // then
        assertThat(existByDateAndTimeIdAndThemeId).isTrue();
    }

    @Test
    @DisplayName("모든 예약 목록을 조회한다.")
    void findAll() {
        // given
        insertReservation(1L, MIA_RESERVATION_DATE, 1L, 1L);

        // when
        List<Reservation> reservations = reservationRepository.findAll();

        // then
        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertSoftly(softly -> {
            softly.assertThat(reservations.size()).isEqualTo(count);
            softly.assertThat(reservations).extracting(Reservation::getTheme)
                    .extracting(Theme::getName)
                    .containsExactly(WOOTECO_THEME_NAME);
            softly.assertThat(reservations).extracting(Reservation::getTime)
                    .extracting(ReservationTime::getStartAt)
                    .containsExactly(MIA_RESERVATION_TIME);
        });
    }

    @Test
    @DisplayName("예약자, 테마, 날짜로 예약 목록을 조회한다.")
    void findAllByMemberIdAndThemeIdAndDateBetween() {
        // given
        insertReservation(1L, MIA_RESERVATION_DATE, 1L, 1L);
        insertReservation(1L, MIA_RESERVATION_DATE.plusDays(2), 1L, 1L);
        insertReservation(2L, MIA_RESERVATION_DATE, 1L, 1L);
        insertReservation(1L, MIA_RESERVATION_DATE, 1L, 2L);

        // when
        List<Reservation> reservations = reservationRepository.findAllByMemberIdAndThemeIdAndDateBetween(
                1L, 1L, MIA_RESERVATION_DATE, MIA_RESERVATION_DATE.plusDays(1));

        // then
        assertSoftly(softly -> {
            softly.assertThat(reservations).hasSize(1);
            softly.assertThat(reservations.get(0).getMemberId()).isEqualTo(1);
            softly.assertThat(reservations.get(0).getThemeId()).isEqualTo(1);
            softly.assertThat(reservations.get(0).getDate()).isEqualTo(MIA_RESERVATION_DATE);
        });
    }

    @Test
    @DisplayName("Id로 예약을 삭제한다.")
    void deleteById() {
        // given
        Long id = insertReservationWithKey(1L, MIA_RESERVATION_DATE, 1L, 1L);

        // when
        reservationRepository.deleteById(id);

        // then
        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation where id = ?", Integer.class, id);
        assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("timeId에 해당하는 예약 건수를 조회한다.")
    void countByTimeId() {
        // given
        long timeId = 2L;

        // when
        int count = reservationRepository.countByTimeId(timeId);

        // then
        assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("날짜와 themeId로 예약 목록을 조회한다.")
    void findAllByDateAndThemeId() {
        // given
        Long timeId = 1L;
        Long themeId = 1L;
        insertReservation(1L, MIA_RESERVATION_DATE, timeId, themeId);
        insertReservation(1L, MIA_RESERVATION_DATE, timeId, themeId);

        // when
        List<Long> reservationsByDateAndThemeId = reservationRepository.findAllTimeIdsByDateAndThemeId(MIA_RESERVATION_DATE, themeId);

        // then
        assertThat(reservationsByDateAndThemeId).hasSize(2);
    }

    private void insertReservation(Long memberId, LocalDate date, Long timeId, Long themeId) {
        jdbcInsert.execute(new MapSqlParameterSource()
                .addValue("member_id", memberId)
                .addValue("date", date)
                .addValue("time_id", timeId)
                .addValue("theme_id", themeId));
    }

    private Long insertReservationWithKey(Long memberId, LocalDate date, Long timeId, Long themeId) {
        return jdbcInsert.executeAndReturnKey(new MapSqlParameterSource()
                        .addValue("member_id", memberId)
                        .addValue("date", date)
                        .addValue("time_id", timeId)
                        .addValue("theme_id", themeId))
                .longValue();
    }
}
