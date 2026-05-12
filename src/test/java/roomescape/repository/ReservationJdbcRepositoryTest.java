package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@JdbcTest
@Import(ReservationJdbcRepository.class)
class ReservationJdbcRepositoryTest {

    @Autowired
    private ReservationJdbcRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void save_저장하면_생성된_id를_반환한다() {
        Long themeId = insertTheme("공포");
        Long timeId = insertTime("10:00");
        Reservation reservation = new Reservation(
                null, "브라운", new Theme(themeId, null, null, null),
                LocalDate.of(2026, 5, 6), new ReservationTime(timeId, null));

        Long id = repository.save(reservation);

        assertThat(id).isPositive();
    }

    @Test
    void findById_조인된_theme과_time을_매핑하여_반환한다() {
        Long themeId = insertTheme("공포");
        Long timeId = insertTime("10:00");
        insertReservation("브라운", themeId, "2026-05-06", timeId);
        Long reservationId = jdbcTemplate.queryForObject(
                "SELECT id FROM reservation ORDER BY id DESC LIMIT 1", Long.class);

        Reservation found = repository.findById(reservationId);

        assertThat(found.getId()).isEqualTo(reservationId);
        assertThat(found.getName()).isEqualTo("브라운");
        assertThat(found.getDate()).isEqualTo(LocalDate.of(2026, 5, 6));
        assertThat(found.getTheme().getId()).isEqualTo(themeId);
        assertThat(found.getTheme().getName()).isEqualTo("공포");
        assertThat(found.getTime().getId()).isEqualTo(timeId);
        assertThat(found.getTime().getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void findAll_페이징은_id_오름차순으로_limit과_offset을_적용한다() {
        Long themeId = insertTheme("공포");
        Long timeId = insertTime("10:00");
        insertReservation("A", themeId, "2026-05-01", timeId);
        insertReservation("B", themeId, "2026-05-02", timeId);
        insertReservation("C", themeId, "2026-05-03", timeId);

        List<Reservation> firstPage = repository.findAll(2, 0);
        List<Reservation> secondPage = repository.findAll(2, 2);

        assertThat(firstPage).extracting(Reservation::getName).containsExactly("A", "B");
        assertThat(secondPage).extracting(Reservation::getName).containsExactly("C");
    }

    @Test
    void deleteById_삭제된_예약은_조회되지_않는다() {
        Long themeId = insertTheme("공포");
        Long timeId = insertTime("10:00");
        insertReservation("브라운", themeId, "2026-05-06", timeId);
        Long reservationId = jdbcTemplate.queryForObject(
                "SELECT id FROM reservation ORDER BY id DESC LIMIT 1", Long.class);

        repository.deleteById(reservationId);

        Integer count = jdbcTemplate.queryForObject(
                "SELECT count(1) FROM reservation WHERE id = ?", Integer.class, reservationId);
        assertThat(count).isZero();
    }

    @Test
    void findTimeIdsByThemeIdAndDate_매칭하는_time_id만_반환한다() {
        Long themeA = insertTheme("A");
        Long themeB = insertTheme("B");
        Long time1 = insertTime("10:00");
        Long time2 = insertTime("11:00");
        Long time3 = insertTime("12:00");
        insertReservation("u1", themeA, "2026-05-06", time1);
        insertReservation("u2", themeA, "2026-05-06", time2);
        insertReservation("u3", themeA, "2026-05-07", time3);
        insertReservation("u4", themeB, "2026-05-06", time3);

        List<Long> timeIds = repository.findTimeIdsByThemeIdAndDate(themeA, LocalDate.of(2026, 5, 6));

        assertThat(timeIds).containsExactlyInAnyOrder(time1, time2);
    }

    @Test
    void existsByDateAndTimeIdAndThemeId_같은_조합이_있으면_true() {
        Long themeId = insertTheme("공포");
        Long timeId = insertTime("10:00");
        insertReservation("브라운", themeId, "2026-05-06", timeId);

        boolean exists = repository.existsByDateAndTimeIdAndThemeId(
                LocalDate.of(2026, 5, 6), timeId, themeId);

        assertThat(exists).isTrue();
    }

    @Test
    void existsByDateAndTimeIdAndThemeId_같은_조합이_없으면_false() {
        Long themeId = insertTheme("공포");
        Long timeId = insertTime("10:00");

        boolean exists = repository.existsByDateAndTimeIdAndThemeId(
                LocalDate.of(2026, 5, 6), timeId, themeId);

        assertThat(exists).isFalse();
    }

    private Long insertTheme(String name) {
        jdbcTemplate.update(
                "INSERT INTO theme(name, description, thumbnail_image_url) VALUES (?, '설명', 'https://thumbnail.url')",
                name);
        return jdbcTemplate.queryForObject(
                "SELECT id FROM theme ORDER BY id DESC LIMIT 1", Long.class);
    }

    private Long insertTime(String startAt) {
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES (?)", startAt);
        return jdbcTemplate.queryForObject(
                "SELECT id FROM reservation_time ORDER BY id DESC LIMIT 1", Long.class);
    }

    private void insertReservation(String name, Long themeId, String date, Long timeId) {
        jdbcTemplate.update(
                "INSERT INTO reservation(name, theme_id, date, time_id) VALUES (?, ?, ?, ?)",
                name, themeId, date, timeId);
    }
}
