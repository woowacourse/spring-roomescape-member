package roomescape.repository.reservationtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.test.fixture.DateFixture.TODAY;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.MemberRole;
import roomescape.domain.ReservationTime;
import roomescape.dto.other.TimeWithBookState;

@JdbcTest
@Import(H2ReservationTimeRepository.class)
class H2ReservationTimeRepositoryTest {

    @Autowired
    private JdbcTemplate template;
    @Autowired
    private H2ReservationTimeRepository timeRepository;

    @BeforeEach
    void setup() {
        template.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        template.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        template.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
    }

    @DisplayName("모든 예약시간을 조회할 수 있다")
    @Test
    void canFindAll() {
        // given
        template.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(10, 0));
        template.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(11, 0));
        template.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(12, 0));

        // when
        List<ReservationTime> actualReservationTimes = timeRepository.findAll();

        // then
        assertThat(actualReservationTimes).hasSize(3);
    }

    @DisplayName("ID를 통해 예약을 조회할 수 있다")
    @Test
    void canFindById() {
        // given
        template.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(10, 0));

        // when
        Optional<ReservationTime> actualTime = timeRepository.findById(1L);

        // then
        ReservationTime expectedTime = new ReservationTime(1L, LocalTime.of(10, 0));
        assertAll(
                () -> assertThat(actualTime).isPresent(),
                () -> assertThat(actualTime.get()).isEqualTo(expectedTime)
        );
    }

    @DisplayName("예약 시간을 통해 예약을 조회할 수 있다")
    @Test
    void canCheckExistenceByStartAt() {
        // given
        LocalTime startAt = LocalTime.of(10, 0);
        template.update("INSERT INTO reservation_time (start_at) VALUES (?)", startAt);

        // when
        boolean isInOfStartAt = timeRepository.checkExistenceByStartAt(startAt);
        boolean isOutOfStartAT = timeRepository.checkExistenceByStartAt(startAt.plusSeconds(1));

        // then
        assertAll(
                () -> assertThat(isInOfStartAt).isTrue(),
                () -> assertThat(isOutOfStartAT).isFalse()
        );
    }

    @DisplayName("예약 여부와 함께 예약 시간을 조회활 수 있다")
    @Test
    void canFindAllWithBookState() {
        // given
        template.update("INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?)",
                "회원", "test@test.com", "zdsa123!", MemberRole.GENERAL.toString());
        template.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(10, 0));
        template.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(11, 0));
        template.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마1", "설명1", "썸네일1");
        template.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                1L, TODAY.toString(), 1L, 1L);

        // when
        List<TimeWithBookState> times = timeRepository.findAllWithBookState(TODAY, 1L);

        // then
        TimeWithBookState timeBooked = times.stream().filter(time -> time.startAt() == LocalTime.of(10, 0))
                .findFirst().get();
        TimeWithBookState timeNotBooked = times.stream().filter(time -> time.startAt() == LocalTime.of(11, 0))
                .findFirst().get();
        assertAll(
                () -> assertThat(times).hasSize(2),
                () -> assertThat(timeBooked.isBooked()).isTrue(),
                () -> assertThat(timeNotBooked.isBooked()).isFalse()
        );
    }

    @DisplayName("예약 시간을 추가할 수 있다")
    @Test
    void canAdd() {
        // given
        ReservationTime time = ReservationTime.createWithoutId(LocalTime.of(10, 0));

        // when
        timeRepository.add(time);

        // then
        assertThat(timeRepository.findAll()).hasSize(1);
    }

    @DisplayName("예약 시간을 삭제할 수 있다")
    @Test
    void canDeleteById() {
        // given
        template.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(10, 0));

        // when
        timeRepository.deleteById(1L);

        // then
        assertThat(timeRepository.findAll()).isEmpty();
    }
}
