package roomescape.time.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class JdbcReservationTimeRepositoryTest {

    private final JdbcTemplate jdbcTemplate;
    private final ReservationTimeRepository reservationTimeRepository;

    @Autowired
    public JdbcReservationTimeRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
    @DisplayName("존재하지 않는 ID로 조회하면 빈 Optional을 반환한다.")
    void findByIdWhenNotExistsTest() {
        // when
        Optional<ReservationTime> found = reservationTimeRepository.findById(999L);

        // then
        assertThat(found).isEmpty();
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

    @Test
    @DisplayName("저장된 시간이 없으면 빈 리스트를 반환한다.")
    void findAllWhenEmptyTest() {
        // when
        List<ReservationTime> times = reservationTimeRepository.findAll();

        // then
        assertThat(times).isEmpty();
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
    @DisplayName("해당 테마와 날짜에 예약되지 않은 시간만 조회된다.")
    void findAvailableTimes() {
        // given
        Long themeId = 1L;
        LocalDate date = LocalDate.of(2025, 1, 1);

        jdbcTemplate.update("insert into reservation_time(id, start_at) values (1, '10:00:00')");
        jdbcTemplate.update("insert into reservation_time(id, start_at) values (2, '11:00:00')");
        jdbcTemplate.update("insert into reservation_time(id, start_at) values (3, '12:00:00')");

        jdbcTemplate.update("insert into theme(id, name, description, thumbnail_url) values (1, '테마', '설명', 'url')");

        jdbcTemplate.update(
                "insert into reservation(name, reservation_date, time_id, theme_id) values ('user', ?, 2, ?)",
                date, themeId
        );

        // when
        List<ReservationTime> result = reservationTimeRepository.findAvailableTimes(themeId, date);

        // then
        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(ReservationTime::getStartAt)
                .containsExactlyInAnyOrder(LocalTime.of(10, 0), LocalTime.of(12, 0));
    }

    @Test
    @DisplayName("예약이 하나도 없으면 모든 시간이 조회된다.")
    void findAvailableTimesWhenNoReservationsTest() {
        // given
        Long themeId = 1L;
        LocalDate date = LocalDate.of(2025, 1, 1);

        jdbcTemplate.update("insert into reservation_time(id, start_at) values (1, '10:00:00')");
        jdbcTemplate.update("insert into reservation_time(id, start_at) values (2, '11:00:00')");

        jdbcTemplate.update("insert into theme(id, name, description, thumbnail_url) values (1, '테마', '설명', 'url')");

        // when
        List<ReservationTime> result = reservationTimeRepository.findAvailableTimes(themeId, date);

        // then
        assertThat(result)
                .extracting(ReservationTime::getStartAt)
                .containsExactlyInAnyOrder(LocalTime.of(10, 0), LocalTime.of(11, 0));
    }

    @Test
    @DisplayName("모든 시간이 예약되었으면 빈 리스트를 반환한다.")
    void findAvailableTimesWhenAllReservedTest() {
        // given
        Long themeId = 1L;
        LocalDate date = LocalDate.of(2025, 1, 1);

        jdbcTemplate.update("insert into reservation_time(id, start_at) values (1, '10:00:00')");
        jdbcTemplate.update("insert into reservation_time(id, start_at) values (2, '11:00:00')");

        jdbcTemplate.update("insert into theme(id, name, description, thumbnail_url) values (1, '테마', '설명', 'url')");

        jdbcTemplate.update(
                "insert into reservation(name, reservation_date, time_id, theme_id) values ('user1', ?, 1, ?)",
                date, themeId
        );
        jdbcTemplate.update(
                "insert into reservation(name, reservation_date, time_id, theme_id) values ('user2', ?, 2, ?)",
                date, themeId
        );

        // when
        List<ReservationTime> result = reservationTimeRepository.findAvailableTimes(themeId, date);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("동일한 시간이라도 다른 날짜의 예약은 가용 시간 조회에 영향을 주지 않는다.")
    void findAvailableTimesIgnoresOtherDatesTest() {
        // given
        Long themeId = 1L;
        LocalDate targetDate = LocalDate.of(2025, 1, 1);
        LocalDate otherDate = LocalDate.of(2025, 1, 2);

        jdbcTemplate.update("insert into reservation_time(id, start_at) values (1, '10:00:00')");
        jdbcTemplate.update("insert into theme(id, name, description, thumbnail_url) values (1, '테마', '설명', 'url')");

        jdbcTemplate.update(
                "insert into reservation(name, reservation_date, time_id, theme_id) values ('user', ?, 1, ?)",
                otherDate, themeId
        );

        // when
        List<ReservationTime> result = reservationTimeRepository.findAvailableTimes(themeId, targetDate);

        // then
        assertThat(result)
                .extracting(ReservationTime::getStartAt)
                .containsExactly(LocalTime.of(10, 0));
    }

    @Test
    @DisplayName("다른 테마의 예약은 해당 테마의 가용 시간 조회에 영향을 주지 않는다.")
    void findAvailableTimesIgnoresOtherThemesTest() {
        // given
        Long targetThemeId = 1L;
        Long otherThemeId = 2L;
        LocalDate date = LocalDate.of(2025, 1, 1);

        jdbcTemplate.update("insert into reservation_time(id, start_at) values (1, '10:00:00')");
        jdbcTemplate.update("insert into theme(id, name, description, thumbnail_url) values (1, '테마1', '설명', 'url1')");
        jdbcTemplate.update("insert into theme(id, name, description, thumbnail_url) values (2, '테마2', '설명', 'url2')");

        jdbcTemplate.update(
                "insert into reservation(name, reservation_date, time_id, theme_id) values ('user', ?, 1, ?)",
                date, otherThemeId
        );

        // when
        List<ReservationTime> result = reservationTimeRepository.findAvailableTimes(targetThemeId, date);

        // then
        assertThat(result)
                .extracting(ReservationTime::getStartAt)
                .containsExactly(LocalTime.of(10, 0));
    }

    @Test
    @DisplayName("예약에 사용 중인 시간 정보를 삭제하려고 하면 ResourceInUseException이 발생한다.")
    void deleteByIdInUseTest() {
        // given
        ReservationTime saved = reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));
        jdbcTemplate.update("insert into theme(id, name, description, thumbnail_url) values (1, '테마', '설명', 'url')");
        jdbcTemplate.update(
                "insert into reservation(name, reservation_date, time_id, theme_id) values ('user', '2025-01-01', ?, 1)",
                saved.getId()
        );

        // when & then
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->
                reservationTimeRepository.deleteById(saved.getId())
        ).isInstanceOf(roomescape.exception.ResourceInUseException.class);
    }

    @Test
    @DisplayName("이미 존재하는 시작 시간으로 저장하면 DuplicateResourceException이 발생한다.")
    void saveDuplicateTimeTest() {
        // given
        LocalTime sameTime = LocalTime.of(14, 0);
        reservationTimeRepository.save(new ReservationTime(null, sameTime));

        // when & then
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->
                reservationTimeRepository.save(new ReservationTime(null, sameTime))
        ).isInstanceOf(roomescape.exception.DuplicateResourceException.class);
    }

    @Test
    @DisplayName("findAll 조회 시 시간 순서대로 정렬되어 반환되는지 확인한다.")
    void findAllSortTest() {
        // given
        reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(22, 0)));
        reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));
        reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(15, 0)));

        // when
        List<ReservationTime> times = reservationTimeRepository.findAll();

        // then
        assertThat(times).extracting(ReservationTime::getStartAt).containsExactly(
                LocalTime.of(10, 0),
                LocalTime.of(15, 0),
                LocalTime.of(22, 0)
        );
    }
}
