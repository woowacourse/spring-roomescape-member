package roomescape.repository.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.test.fixture.DateFixture.NEXT_DAY;
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
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@JdbcTest
@Import(H2ReservationRepository.class)
class H2ReservationRepositoryTest {

    @Autowired
    private JdbcTemplate template;
    @Autowired
    private H2ReservationRepository reservationRepository;

    @BeforeEach
    void setup() {
        template.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        template.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        template.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
    }

    @DisplayName("모든 예약을 조회할 수 있다")
    @Test
    void canFindAll() {
        template.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(10, 0));
        template.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명1", "썸네일1");

        template.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "이름1", NEXT_DAY.toString(), 1L, 1L);
        template.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "이름2", NEXT_DAY.toString(), 1L, 1L);
        template.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "이름3", NEXT_DAY.toString(), 1L, 1L);

        List<Reservation> actualReservations = reservationRepository.findAll();
        assertThat(actualReservations).hasSize(3);
    }

    @DisplayName("ID를 기반으로 예약을 조회할 수 있다")
    @Test
    void canFindById() {
        template.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(10, 0));
        template.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명1", "썸네일1");
        template.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "이름1", NEXT_DAY.toString(), 1L, 1L);

        Optional<Reservation> reservation = reservationRepository.findById(1L);
        assertThat(reservation).isPresent();
        assertThat(reservation.get().getId()).isEqualTo(1L);
    }

    @DisplayName("이미 예약된 예외인지 조회할 수 있다")
    @Test
    void canCheckAlreadyReserved() {
        template.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(10, 0));
        template.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명1", "썸네일1");
        template.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "이름1", NEXT_DAY.toString(), 1L, 1L);

        boolean isAlreadyReserved = reservationRepository.checkAlreadyReserved(NEXT_DAY, 1L, 1L);
        boolean isNotAlreadyReserved = reservationRepository.checkAlreadyReserved(TODAY, 1L, 1L);

        assertAll(
                () -> assertThat(isAlreadyReserved).isTrue(),
                () -> assertThat(isNotAlreadyReserved).isFalse()
        );
    }

    @DisplayName("특정 예약시간 해당하는 예약이 존재하는지 조회할 수 있다")
    @Test
    void canCheckExistenceInTime() {
        template.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(10, 0));
        template.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명1", "썸네일1");
        template.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "이름1", NEXT_DAY.toString(), 1L, 1L);

        boolean isInTime = reservationRepository.checkExistenceInTime(1L);
        boolean isNotInTime = reservationRepository.checkExistenceInTime(100L);

        assertAll(
                () -> assertThat(isInTime).isTrue(),
                () -> assertThat(isNotInTime).isFalse()
        );
    }

    @DisplayName("특정 테마에 해당하는 예약이 존재하는지 조회할 수 있다")
    @Test
    void canCheckExistenceInTheme() {
        template.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(10, 0));
        template.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명1", "썸네일1");
        template.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "이름1", NEXT_DAY.toString(), 1L, 1L);

        boolean isInTheme = reservationRepository.checkExistenceInTheme(1L);
        boolean isNotInTheme = reservationRepository.checkExistenceInTheme(100L);

        assertAll(
                () -> assertThat(isInTheme).isTrue(),
                () -> assertThat(isNotInTheme).isFalse()
        );
    }

    @DisplayName("예약을 추가할 수 있다")
    @Test
    void canAdd() {
        template.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(10, 0));
        template.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명1", "썸네일1");

        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "이름", "설명", "섬네일");
        reservationRepository.add(Reservation.createWithoutId("이름1", NEXT_DAY, time, theme));

        Optional<Reservation> actualReservation = reservationRepository.findById(1L);
        Reservation expectedReservation = new Reservation(1L, "이름1", NEXT_DAY, time, theme);
        assertAll(
                () -> assertThat(actualReservation).isPresent(),
                () -> assertThat(actualReservation.get()).isEqualTo(expectedReservation)
        );
    }

    @DisplayName("ID를 기반으로 예약을 제거할 수 있다")
    @Test
    void canDeleteById() {
        template.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(10, 0));
        template.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명1", "썸네일1");
        template.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "이름1", NEXT_DAY.toString(), 1L, 1L);

        reservationRepository.deleteById(1L);

        assertThat(reservationRepository.findAll()).isEmpty();
    }
}
