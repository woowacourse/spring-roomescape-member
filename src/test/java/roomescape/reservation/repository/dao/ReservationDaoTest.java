package roomescape.reservation.repository.dao;


import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.jdbc.Sql;
import roomescape.reservation.repository.entity.ReservationEntity;

@Sql(scripts = {"/import_reservation-time.sql", "/import_theme.sql"})
@JdbcTest
class ReservationDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    ReservationDao reservationDao;

    private static final RowMapper<ReservationEntity> reservationRowMapper = (rs, rowNum) ->
            new ReservationEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getObject("date", LocalDate.class),
                    rs.getLong("time_id"),
                    rs.getLong("theme_id"),
                    rs.getBoolean("is_cancelled")
            );

    @BeforeEach
    void setup() {
        reservationDao = new ReservationDao(jdbcTemplate);
    }

    @Test
    void 예약을_저장한다() {
        //given
        String name = "user";
        LocalDate date = LocalDate.now();
        Long timeId = 1L;
        Long themeId = 1L;

        //when
        Long savedId = reservationDao.save(name, date, timeId, themeId);

        //then
        ReservationEntity reservationEntity = jdbcTemplate.queryForObject(
                "SELECT * FROM reservation WHERE id = ? AND is_deleted = FALSE;",
                reservationRowMapper, savedId);

        ReservationEntity expected = new ReservationEntity(savedId, name, date, timeId, themeId, false);

        Assertions.assertThat(reservationEntity)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void 예약_전체_목록을_조회한다() {
        //given
        reservationDao.save("userA", LocalDate.now(), 1L, 1L);
        reservationDao.save("userB", LocalDate.now().plusDays(1), 1L, 1L);

        //when
        List<ReservationEntity> reservations = reservationDao.findAll();

        //then
        Assertions.assertThat(reservations).hasSize(2);
    }

    @Test
    void 이름을_비교하여_예약_목록을_조회한다() {
        //given
        String targetUserName = "userA";
        String otherUserName = "userB";

        reservationDao.save(targetUserName, LocalDate.now(), 1L, 1L);
        reservationDao.save(otherUserName, LocalDate.now().plusDays(1), 1L, 1L);

        //when
        List<ReservationEntity> reservations = reservationDao.findByName(targetUserName);

        //then
        Assertions.assertThat(reservations)
                .extracting(ReservationEntity::getName)
                .containsExactly(targetUserName);
    }

    @Test
    void 기준_날짜_이후의_예약만_조회한다() {
        //given
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        reservationDao.save("userA", yesterday, 1L, 1L);
        reservationDao.save("userB", today, 1L, 1L);
        reservationDao.save("userC", tomorrow, 1L, 1L);

        //when
        List<LocalDate> findDates = reservationDao.findAllOnOrAfter(today).stream()
                .map(ReservationEntity::getDate)
                .toList();

        //then
        Assertions.assertThat(findDates).containsExactly(today, tomorrow);
    }

    @Test
    void 취소된_예약은_유효한_예약으로_취급하지_않는다() {
        //given
        Long timeId = 1L;
        Long themeId = 1L;
        LocalDate date = LocalDate.now().plusDays(1);

        Long reservationId = reservationDao.save("userC", date, timeId, themeId);

        //예약을 취소한 경우, 해당 예약은 유효하지 않다
        reservationDao.updateCancelledById(reservationId, true);

        //when & then
        Assertions.assertThat(reservationDao.existsValidReservationAt(themeId, date, timeId))
                .isFalse();
    }

    @Test
    void 예약을_삭제한다() {
        //given
        String name = "user";
        LocalDate date = LocalDate.now();
        Long timeId = 1L;
        Long themeId = 1L;

        Long savedId = reservationDao.save(name, date, timeId, themeId);

        //when
        reservationDao.deleteById(savedId);

        //then
        Assertions.assertThat(reservationDao.findById(savedId)).isNotPresent();
    }

    @Test
    void 예약을_취소한다() {
        //given
        String name = "user";
        LocalDate date = LocalDate.now();
        Long timeId = 1L;
        Long themeId = 1L;

        Long savedId = reservationDao.save(name, date, timeId, themeId);

        //when
        reservationDao.updateCancelledById(savedId, true);

        //then
        ReservationEntity entity = reservationDao.findById(savedId).orElseThrow();
        Assertions.assertThat(entity.isCancelled()).isTrue();
    }
}
