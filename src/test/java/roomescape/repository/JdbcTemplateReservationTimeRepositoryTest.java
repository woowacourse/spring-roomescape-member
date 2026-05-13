package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.ReservationTime;

@JdbcTest
@Import(JdbcTemplateReservationTimeRepository.class)
class JdbcTemplateReservationTimeRepositoryTest {

    private static final long THEME_ID = 1L;
    private static final LocalTime DEFAULT_START_AT = LocalTime.of(10, 0);

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("예약 시간을 저장하면 id가 포함된 예약 시간을 반환한다")
    void saveReservationTime() {
        ReservationTime saved = reservationTimeRepository.addTime(new ReservationTime(null, LocalTime.of(10, 0)));

        assertThat(saved.id()).isNotNull();
        assertThat(saved.startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    @DisplayName("모든 예약 시간을 조회한다")
    void findAllReservationTimes() {
        addTime(LocalTime.of(10, 0));
        addTime(LocalTime.of(11, 0));

        List<ReservationTime> times = reservationTimeRepository.findAllReservationTimes();

        assertThat(times).hasSize(2);
    }

    @Test
    @DisplayName("예약 시간이 없으면 빈 목록을 반환한다")
    void findEmptyReservationTimes() {
        List<ReservationTime> times = reservationTimeRepository.findAllReservationTimes();

        assertThat(times).isEmpty();
    }

    @Test
    @DisplayName("id로 예약 시간을 조회한다")
    void findReservationTimeById() {
        long id = addTime(LocalTime.of(10, 0));

        ReservationTime time = reservationTimeRepository.findById(id).get();

        assertThat(time.id()).isEqualTo(id);
        assertThat(time.startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    @DisplayName("id로 예약 시간을 삭제한다")
    void deleteReservationTimeById() {
        long id = addTime(LocalTime.of(10, 0));

        reservationTimeRepository.deleteTime(id);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM reservation_time", Integer.class);
        assertThat(count).isEqualTo(0);
    }


    @Test
    @DisplayName("예약이 존재하는 경우 예약 시간 삭제를 할 수 없다")
    void existsTime_WhenReservationAlreadyExist() {
        long timeId = addReservedTime();

        boolean exists = reservationTimeRepository.existsByTimeId(timeId);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("예약이 존재하지 않는 경우 예약 시간 삭제를 할 수 있다")
    void notExistsTime_WhenReservationDoesNotExist() {
        long timeId = addTime(DEFAULT_START_AT);

        boolean exists = reservationTimeRepository.existsByTimeId(timeId);

        assertThat(exists).isFalse();
    }

    private long addTime(LocalTime startAt) {
        return reservationTimeRepository.addTime(new ReservationTime(null, startAt)).id();
    }

    private long addReservedTime() {
        long timeId = addTime(DEFAULT_START_AT);
        addTheme();
        addReservation(timeId);

        return timeId;
    }

    private void addTheme() {
        jdbcTemplate.update("""
                INSERT INTO theme (id, name, description, thumbnail_url)
                VALUES (?, '우주 정거장', '우주 정거장에서 탈출하세요.', 'https://picsum.photos/seed/theme1/400/300')
                """, THEME_ID);
    }

    private void addReservation(long timeId) {
        jdbcTemplate.update("""
                INSERT INTO reservation (name, date, time_id, theme_id)
                VALUES ('브라운', '2026-05-03', ?, ?)
                """, timeId, THEME_ID);
    }
}
