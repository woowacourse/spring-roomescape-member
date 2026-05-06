package roomescape.time.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.time.domain.ReservationTime;

@JdbcTest
class JdbcReservationTimeRepositoryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    ReservationTimeRepository reservationTimeRepository;

    @Autowired
    public JdbcReservationTimeRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.reservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("새로운 시간 정보를 저장하고 반환된 객체의 ID를 확인한다.")
    void saveTest() {
        // given
        LocalTime startTime = LocalTime.of(10, 0);
        ReservationTime time = new ReservationTime(null, startTime);

        // when
        ReservationTime savedTime = reservationTimeRepository.save(time);

        //then
        assertThat(savedTime.getId()).isNotNull();
        assertThat(savedTime.getStartAt()).isEqualTo(startTime);
    }

    @Test
    @DisplayName("ID를 통해 시간 정보를 삭제한다.")
    void deleteByIdTest() {
        // given
        ReservationTime saved = reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));

        // when
        reservationTimeRepository.deleteById(saved.getId());

        // then
        List<ReservationTime> all = reservationTimeRepository.findAll();
        assertThat(all).isEmpty();
    }

    @Test
    @DisplayName("ID를 통해 저장된 시간 정보를 정확히 조회한다.")
    void findByIdTest() {
        // given
        LocalTime targetTime = LocalTime.of(11, 0);
        ReservationTime savedTime = reservationTimeRepository.save(new ReservationTime(null, targetTime));

        // when
        ReservationTime foundTime = reservationTimeRepository.findById(savedTime.getId())
                .orElseThrow(() -> new AssertionError("조회된 결과가 없습니다. id: " + savedTime.getId()));

        // then
        assertThat(foundTime.getId()).isEqualTo(savedTime.getId());
        assertThat(foundTime.getStartAt()).isEqualTo(targetTime);
    }

    @Test
    @DisplayName("존재하는 모든 시간 목록을 리스트로 조회한다.")
    void findAllTest() {
        // given
        reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));
        reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(11, 0)));

        // when
        List<ReservationTime> times = reservationTimeRepository.findAll();

        // then
        assertThat(times).hasSize(2);
        assertThat(times).extracting("startAt").containsExactly(LocalTime.of(10, 0), LocalTime.of(11, 0));
    }

    @DisplayName("해당 시간이 존재하는지 조회한다.")
    @Test
    void existStartAtTest() {
        // given
        reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));

        // when
        boolean exists = reservationTimeRepository.existsByStartAt(LocalTime.of(10, 0));
        boolean notExists = reservationTimeRepository.existsByStartAt(LocalTime.of(11, 0));

        // then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("예약되지 않은 시간만 조회된다")
    void findAvailableTimes() {
        // given
        Long themeId = 1L;
        LocalDate date = LocalDate.of(2025, 1, 1);

        jdbcTemplate.update("insert into reservation_time(id, start_at) values (1, '10:00:00')");
        jdbcTemplate.update("insert into reservation_time(id, start_at) values (2, '11:00:00')");
        jdbcTemplate.update("insert into reservation_time(id, start_at) values (3, '12:00:00')");

        jdbcTemplate.update("insert into theme(id, name, description, thumbnail_url) values (1, '테마', '설명', 'url')");

        jdbcTemplate.update("""
            insert into reservation(name, reservation_date, time_id, theme_id)
            values ('user', ?, 2, ?)
        """, date, themeId);

        // when
        List<AvailableTimeQueryResult> result = reservationTimeRepository.findAvailableTimes(themeId, date);

        // then
        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(AvailableTimeQueryResult::startAt)
                .containsExactlyInAnyOrder(LocalTime.of(10, 0), LocalTime.of(12, 0));
    }
}
