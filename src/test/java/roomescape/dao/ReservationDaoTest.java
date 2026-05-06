package roomescape.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import roomescape.domain.Reservation;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@ActiveProfiles("test")
@Import(ReservationDao.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationDaoTest {

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 테마ID_외래키_제약조건_위반_테스트() {
        Long timeId = insertTime(LocalTime.of(10, 0));
        Long nonExistentThemeId = 999L;

        assertThatThrownBy(() ->
                reservationDao.insertReservation("이든", LocalDate.of(2026, 5, 6), timeId, nonExistentThemeId)
        ).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void 시간ID_외래키_제약조건_위반_테스트() {
        Long nonExistentTimeId = 999L;
        Long themeId = insertTheme("테마", "설명", "url");

        assertThatThrownBy(() ->
                reservationDao.insertReservation("이든", LocalDate.of(2026, 5, 6), nonExistentTimeId, themeId)
        ).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void 예약_조회_매핑_테스트() {
        Long themeId = insertTheme("테마", "설명", "url");
        Long timeId = insertTime(LocalTime.of(15, 30));

        Long reservationId = reservationDao.insertReservation("이든", LocalDate.of(2026, 5, 5), timeId, themeId);

        Reservation reservation = reservationDao.findReservationById(reservationId);

        assertThat(reservation.getName()).isEqualTo("이든");
        assertThat(reservation.getTime().getId()).isEqualTo(timeId);
        assertThat(reservation.getTime().getStartAt()).isEqualTo(LocalTime.of(15, 30));
    }

    private Long insertTheme(String name, String description, String imgUrl) {
        String sql = "INSERT INTO theme (name, description, imgUrl) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setString(3, imgUrl);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    private Long insertTime(LocalTime startAt) {
        String sql = "INSERT INTO reservation_time (start_at) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, startAt.toString());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }
}