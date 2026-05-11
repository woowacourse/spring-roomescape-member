package roomescape.reservationtime.infra;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.domain.repository.ReservationTimeRepository;

@JdbcTest
class JdbcReservationTimeRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ReservationTimeRepository timeRepository;

    @BeforeEach
    void setUp() {
        timeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
    }

    @DisplayName("예약 시간 저장을 테스트합니다.")
    @Test
    void save_time() {
        ReservationTime savedTime = timeRepository.save(ReservationTime.builder()
                .startAt(LocalTime.of(9, 0))
                .build());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(savedTime.getId()).isPositive();
            softly.assertThat(savedTime.getStartAt()).isEqualTo(LocalTime.of(9, 0));
        });
    }

    @DisplayName("id로 예약 시간 조회를 테스트합니다.")
    @Test
    void find_by_id() {
        ReservationTime savedTime = timeRepository.save(ReservationTime.builder()
                .startAt(LocalTime.of(9, 0))
                .build());

        assertThat(timeRepository.findById(savedTime.getId()))
                .contains(savedTime);
    }

    @DisplayName("예약 시간의 시작 시간 오름차순 전체 조회를 테스트합니다.")
    @Test
    void find_all_order_by_start_at() {
        timeRepository.save(ReservationTime.builder().startAt(LocalTime.of(10, 0)).build());
        timeRepository.save(ReservationTime.builder().startAt(LocalTime.of(9, 0)).build());
        timeRepository.save(ReservationTime.builder().startAt(LocalTime.of(11, 0)).build());

        List<ReservationTime> times = timeRepository.findAll();

        assertThat(times)
                .extracting(ReservationTime::getStartAt)
                .containsExactly(
                        LocalTime.of(9, 0),
                        LocalTime.of(10, 0),
                        LocalTime.of(11, 0)
                );
    }

    @DisplayName("예약 시간 삭제를 테스트합니다.")
    @Test
    void delete_time() {
        ReservationTime savedTime = timeRepository.save(ReservationTime.builder()
                .startAt(LocalTime.of(9, 0))
                .build());

        Integer deletedCount = timeRepository.delete(savedTime.getId());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(deletedCount).isEqualTo(1);
            softly.assertThat(timeRepository.findById(savedTime.getId())).isEmpty();
        });
    }

    @DisplayName("같은 시작 시간의 예약 시간 존재 여부 조회를 테스트합니다.")
    @Test
    void exists_by_start_at() {
        timeRepository.save(ReservationTime.builder()
                .startAt(LocalTime.of(9, 0))
                .build());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(timeRepository.existsByStartAt(LocalTime.of(9, 0))).isTrue();
            softly.assertThat(timeRepository.existsByStartAt(LocalTime.of(10, 0))).isFalse();
        });
    }
}
