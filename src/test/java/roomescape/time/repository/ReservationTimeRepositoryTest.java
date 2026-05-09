package roomescape.time.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.JdbcReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.JdbcThemeRepository;
import roomescape.time.domain.ReservationTime;

@JdbcTest
class ReservationTimeRepositoryTest {
    private JdbcReservationTimeRepository jdbcReservationTimeRepository;
    private JdbcThemeRepository jdbcThemeRepository;
    private JdbcReservationRepository jdbcReservationRepository;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcReservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
        jdbcThemeRepository = new JdbcThemeRepository(jdbcTemplate);
        jdbcReservationRepository = new JdbcReservationRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("모든 예약 시간 정보를 조회한다.")
    void findAll() {
        // given
        List<ReservationTime> reservationTimes = List.of(
                ReservationTime.create(LocalTime.of(12, 0)),
                ReservationTime.create(LocalTime.of(13, 0)),
                ReservationTime.create(LocalTime.of(14, 0))
        );
        List<ReservationTime> savedTimes = savedAll(reservationTimes);

        // when & then
        assertThat(jdbcReservationTimeRepository.findAll().size()).isEqualTo(savedTimes.size());
    }

    @Test
    @DisplayName("id로 특정 예약 시간 정보를 조회한다.")
    void findById() {
        // given
        List<ReservationTime> reservationTimes = List.of(
                ReservationTime.create(LocalTime.of(12, 0)),
                ReservationTime.create(LocalTime.of(13, 0)),
                ReservationTime.create(LocalTime.of(14, 0))
        );
        List<ReservationTime> savedTimes = savedAll(reservationTimes);
        LocalTime expected = savedTimes.getFirst().startAt();

        // when
        LocalTime actual = jdbcReservationTimeRepository.findById(savedTimes.getFirst().id()).get().startAt();

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("예약 시간을 추가한다.")
    void save() {
        // given
        List<ReservationTime> emptyTimes = List.of();
        LocalTime newTime = LocalTime.of(12, 0);

        // when
        jdbcReservationTimeRepository.save(ReservationTime.create(newTime));

        // then
        assertThat(jdbcReservationTimeRepository.findAll().size()).isEqualTo(emptyTimes.size() + 1);
    }

    @Test
    @DisplayName("예약 시간을 삭제한다.")
    void delete() {
        // given
        List<ReservationTime> reservationTimes = List.of(
                ReservationTime.create(LocalTime.of(12, 0)),
                ReservationTime.create(LocalTime.of(13, 0)),
                ReservationTime.create(LocalTime.of(14, 0))
        );
        List<ReservationTime> savedTimes = savedAll(reservationTimes);

        // when
        jdbcReservationTimeRepository.delete(savedTimes.getFirst().id());

        // then
        assertThat(jdbcReservationTimeRepository.findAll().size()).isEqualTo(savedTimes.size() - 1);
    }

    @Test
    @DisplayName("예약 시작 시간 값으로 예약 시간이 존재하는지 확인한다.")
    void existsByStartAt() {
        // given
        LocalTime duplicatedTime = LocalTime.of(15, 0);
        LocalTime nonSavedTime = LocalTime.of(12, 0);
        savedTime(ReservationTime.create(duplicatedTime));

        // when & then
        assertThat(jdbcReservationTimeRepository.existsByStartAt(duplicatedTime)).isTrue();
        assertThat(jdbcReservationTimeRepository.existsByStartAt(nonSavedTime)).isFalse();
    }

    @Test
    @DisplayName("예약 가능한 시간을 조회한다.")
    void findAvailableTimes() {
        // given
        ReservationTime time1 = savedTime(ReservationTime.create(LocalTime.of(12, 0)));
        ReservationTime time2 = savedTime(ReservationTime.create(LocalTime.of(13, 0)));
        ReservationTime time3 = savedTime(ReservationTime.create(LocalTime.of(14, 0)));
        LocalDate date = LocalDate.of(2099, 10, 10);
        Theme theme1 = Theme.create("테마1", "테마 설명", "테마 썸네일");
        theme1.updateStatus(true);
        Theme theme2 = jdbcThemeRepository.save(theme1);
        jdbcReservationRepository.save(Reservation.create("한다", date, time1.startAt(), theme2));
        jdbcReservationRepository.save(Reservation.create("한다", date, time2.startAt(), theme2));

        // when
        List<ReservationTime> availableTimes = jdbcReservationTimeRepository.findAvailableByDateAndThemeId(
                date, theme2.id());

        // then
        assertThat(availableTimes)
                .hasSize(1);
    }

    private List<ReservationTime> savedAll(List<ReservationTime> reservationTimes) {
        List<ReservationTime> savedTimes = new ArrayList<>();
        for (ReservationTime reservationTime : reservationTimes) {
            savedTimes.add(savedTime(reservationTime));
        }
        return savedTimes;
    }

    private ReservationTime savedTime(ReservationTime reservationTime) {
        Long savedId = jdbcReservationTimeRepository.save(reservationTime);
        return ReservationTime.load(savedId, reservationTime.startAt());
    }
}
