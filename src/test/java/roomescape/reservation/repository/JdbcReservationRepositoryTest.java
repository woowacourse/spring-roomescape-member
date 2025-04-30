package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;

class JdbcReservationRepositoryTest {

    private static EmbeddedDatabase db;
    private JdbcReservationRepository repository;

    @BeforeAll
    static void initDatabase() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema.sql")
                .addScript("classpath:data.sql")
                .build();
    }

    @BeforeEach
    void setUp() {
        repository = new JdbcReservationRepository(db);
    }

    @AfterEach
    void cleanUp() {
        new JdbcTemplate(db).execute("DELETE FROM reservation");
    }

    @AfterAll
    static void shutdownDatabase() {
        db.shutdown();
    }

    @Test
    void 예약이_올바르게_생성된다() {
        // given
        String name = "레포지토리테스트";
        LocalDate date = LocalDate.of(2025, 7, 1);
        LocalTime time = LocalTime.of(10, 0);
        Reservation reservation = new Reservation(null, name, date, new ReservationTime(1L, time));

        // when
        var saved = repository.save(reservation);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo(name);
    }

    @Test
    void 기존에_날짜와_시간이_같은_예약이_있는지_확인() {
        // given
        String existedName = "레포지토리테스트";
        LocalDate existedDate = LocalDate.of(2025, 7, 1);
        LocalTime existedTime = LocalTime.of(10, 0);
        LocalDate date = LocalDate.of(2026, 1, 1);
        LocalTime time = LocalTime.of(14, 1);
        Reservation reservation = new Reservation(null, existedName, existedDate,
                new ReservationTime(1L, existedTime));

        repository.save(reservation);

        // when
        // then
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(repository.existsByDateAndTime(existedDate, existedTime)).isTrue();
            soft.assertThat(repository.existsByDateAndTime(date, time)).isFalse();
        });
    }

    @Test
    void 모든_예약_조회() {
        // given
        String name1 = "레포지토리테스트1";
        String name2 = "레포지토리테스트2";
        LocalDate date = LocalDate.of(2025, 7, 1);
        LocalTime time1 = LocalTime.of(10, 0);
        LocalTime time2 = LocalTime.of(11, 0);
        Reservation reservation1 = new Reservation(null, name1, date, new ReservationTime(1L, time1));
        Reservation reservation2 = new Reservation(null, name2, date, new ReservationTime(2L, time2));

        // when
        repository.save(reservation1);
        repository.save(reservation2);
        // given
        // when
        List<Reservation> reservations = repository.findAll();

        // then
        assertThat(reservations).hasSize(2);
    }

    @Test
    void 예약에_예약_시간이_존재하는지_확인() {
        // given
        LocalTime localTime = LocalTime.of(10, 0);
        ReservationTime reservationTime = new ReservationTime(1L, localTime);
        Reservation reservation = new Reservation("테스트", LocalDate.of(2999, 7, 1), reservationTime);
        repository.save(reservation);

        // when
        boolean exists = repository.existsByReservationTimeId(reservationTime.getId());

        // then
        assertThat(exists).isTrue();
    }
}
