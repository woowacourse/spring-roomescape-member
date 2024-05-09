package roomescape.reservation.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.common.RepositoryTest;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static roomescape.TestFixture.*;
import static roomescape.member.domain.Role.USER;

class ReservationRepositoryTest extends RepositoryTest {
    @Autowired
    private ReservationRepository reservationRepository;

    private SimpleJdbcInsert jdbcInsert;

    @BeforeEach
    void setUp() {
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
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
                new ReservationTime(1L, MIA_RESERVATION_TIME),
                WOOTECO_THEME(1L),
                USER_MIA(1L)
        );

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
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("member_id", 1L)
                .addValue("date", MIA_RESERVATION_DATE)
                .addValue("time_id", timeId)
                .addValue("theme_id", themeId);
        jdbcInsert.executeAndReturnKey(params);

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
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("member_id", 1L)
                .addValue("date", MIA_RESERVATION_DATE)
                .addValue("time_id", 1L)
                .addValue("theme_id", 1L);
        jdbcInsert.execute(params);

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
        jdbcInsert.execute(new MapSqlParameterSource()
                .addValue("member_id", 1L)
                .addValue("date", MIA_RESERVATION_DATE)
                .addValue("time_id", 1L)
                .addValue("theme_id", 1L));

        jdbcInsert.execute(new MapSqlParameterSource()
                .addValue("member_id", 1L)
                .addValue("date", MIA_RESERVATION_DATE.plusDays(2))
                .addValue("time_id", 1L)
                .addValue("theme_id", 1L));

        jdbcInsert.execute(new MapSqlParameterSource()
                .addValue("member_id", 2L)
                .addValue("date", MIA_RESERVATION_DATE)
                .addValue("time_id", 1L)
                .addValue("theme_id", 1L));

        jdbcInsert.execute(new MapSqlParameterSource()
                .addValue("member_id", 1L)
                .addValue("date", MIA_RESERVATION_DATE)
                .addValue("time_id", 1L)
                .addValue("theme_id", 2L));

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
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("member_id", 1L)
                .addValue("date", MIA_RESERVATION_DATE)
                .addValue("time_id", 1L)
                .addValue("theme_id", 1L);
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

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
        String insertSql = "INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?), (?, ?, ?, ?)";
        jdbcTemplate.update(
                insertSql,
                1L, Date.valueOf(MIA_RESERVATION_DATE), timeId, themeId,
                1L, Date.valueOf(MIA_RESERVATION_DATE), timeId, themeId
        );

        // when
        List<Long> reservationsByDateAndThemeId = reservationRepository.findAllTimeIdsByDateAndThemeId(MIA_RESERVATION_DATE, themeId);

        // then
        assertThat(reservationsByDateAndThemeId).hasSize(2);
    }
}
