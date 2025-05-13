package roomescape.time.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.time.entity.ReservationTime;
import roomescape.time.repository.dto.ReservationTimeWithBookedDataResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@JdbcTest
class JdbcReservationTimeRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ReservationTimeRepository timeRepository;
    private final Long memberId = 1L;

    @BeforeEach
    void setup() {
        timeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
        jdbcTemplate.update("INSERT INTO member (id, name, email, password, role) VALUES ( ?, ?, ?, ?, ? )", memberId, "test", "test@example.com", "password", "USER");
    }

    @DisplayName("생성 테스트")
    @Test
    void createTest() {
        // given
        ReservationTime time = new ReservationTime(null, LocalTime.of(10, 0));

        // when
        timeRepository.save(time);

        // then
        assertThat(timeRepository.findAll()).hasSize(1);
    }

    @DisplayName("삭제 테스트")
    @Test
    void deleteTest() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (?, ?)", 1, "10:00");

        // when
        timeRepository.deleteById(1L);

        // then
        assertThat(timeRepository.findAll()).hasSize(0);
    }

    @DisplayName("id로 조회 테스트")
    @Test
    void findByIdTest() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (?, ?)", 1, "10:00");

        // when
        Optional<ReservationTime> entity = timeRepository.findById(1L);

        // then
        assertThat(entity).isNotEmpty();
        assertThat(entity.get().getId()).isEqualTo(1L);
    }

    @DisplayName("날짜, 테마가 주어지면 예약 여부를 같이 반환할 수 있다.")
    @Test
    void findAllByDateAndTheme() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (?, ?)", 1, "10:00");
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (?, ?)", 2, "12:00");
        jdbcTemplate.update("INSERT INTO theme (id, name, description, thumbnail) VALUES (?, ?, ?, ?)", 1, "hello", "hi", "thumbnail");
        jdbcTemplate.update("INSERT INTO reservation (id, member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?, ?)", 1, memberId, "2025-01-01", 1, 1);
        List<ReservationTimeWithBookedDataResponse> expected = List.of(
                new ReservationTimeWithBookedDataResponse(
                        1L,
                        LocalTime.of(10, 0),
                        true
                ),
                new ReservationTimeWithBookedDataResponse(
                        2L,
                        LocalTime.of(12, 0),
                        false
                )
        );

        // when
        List<ReservationTimeWithBookedDataResponse> allWithBooked = timeRepository.findAllWithBooked(LocalDate.of(2025, 1, 1), 1L);

        // then
        assertThat(allWithBooked).containsExactlyInAnyOrderElementsOf(expected);
    }

    @DisplayName("존재하지 않는 id로 조회 테스트")
    @Test
    void findNotExistEntity() {
        // given

        // when
        Optional<ReservationTime> found = timeRepository.findById(1L);

        // then
        assertThat(found).isEmpty();
    }

    @DisplayName("start_at 필드를 기준으로 올바른 time 데이터를 조회할 수 있다.")
    @Test
    void findByStartAt() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ( ? )", "10:00");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ( ? )", "11:00");

        // when
        LocalTime time = LocalTime.of(10, 0);
        Optional<ReservationTime> found = timeRepository.findByStartAt(time);

        // then
        assertSoftly(softly -> {
            softly.assertThat(found).isNotEmpty();
            softly.assertThat(found.get().getStartAt()).isEqualTo(time);
        });
    }
}
