package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

@JdbcTest
class RoomescapeRepositoryTest {

    RoomescapeRepository repository;
    @Autowired
    JdbcTemplate template;

    @BeforeEach
    void setUp() {
        repository = new RoomescapeRepositoryImpl(template);
        template.execute("DELETE FROM reservation");
        template.execute("DELETE FROM reservation_time");
        template.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        template.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        template.execute("insert into reservation_time (start_at) values ('15:40')");
        template.execute("insert into reservation (name, date, time_id) values ('브라운', '2023-08-05', 1)");
    }

    @Test
    void findById() {
        //when
        Reservation reservation = repository.findById(1L);

        //then
        assertEqualReservationElements(reservation, 1L, "브라운", "2023-08-05", 1L, "15:40");
    }

    @Test
    void findByDate() {
        //when
        List<Reservation> found = repository.findByDate(LocalDate.of(2023, 8, 5));
        List<Reservation> notFound = repository.findByDate(LocalDate.of(2023, 8, 6));

        //then
        assertThat(found).hasSize(1);
        assertThat(notFound).isEmpty();
        assertEqualReservationElements(found.getFirst(), 1L, "브라운", "2023-08-05", 1L, "15:40");
    }

    @Test
    void findAll() {
        //when
        List<Reservation> reservations = repository.findAll();

        //then
        assertThat(reservations).hasSize(1);
        assertEqualReservationElements(reservations.getFirst(), 1L, "브라운", "2023-08-05", 1L, "15:40");
    }

    @Test
    void save() {
        //given
        Reservation reservation = new Reservation("네오", LocalDate.parse("2023-08-05"),
                ReservationTime.parse("15:40").toEntity(1L));

        //when
        Reservation saved = repository.save(reservation);
        Reservation firstReservation = repository.findById(1L);
        Reservation secondReservation = repository.findById(2L);

        //then
        assertEqualReservationElements(saved, 2L, "네오", "2023-08-05", 1L, "15:40");
        assertEqualReservationElements(firstReservation, 1L, "브라운", "2023-08-05", 1L, "15:40");
        assertEqualReservationElements(secondReservation, 2L, "네오", "2023-08-05", 1L, "15:40");
        assertThat(repository.findAll()).hasSize(2);
    }

    @Test
    void deleteById() {
        //when
        int deleteCounts = repository.deleteById(1L);

        //then
        assertThat(deleteCounts).isEqualTo(1);
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void existsByDateAndTime() {
        //given
        LocalDate date = LocalDate.parse("2023-08-05");
        LocalDate anotherDate = LocalDate.parse("2023-08-06");
        ReservationTime time = ReservationTime.parse("15:40");
        ReservationTime anotherTime = ReservationTime.parse("15:41");

        //when
        boolean found = repository.existsByDateAndTime(date, time);
        boolean notFound1 = repository.existsByDateAndTime(date, anotherTime);
        boolean notFound2 = repository.existsByDateAndTime(anotherDate, time);
        boolean notFound3 = repository.existsByDateAndTime(anotherDate, anotherTime);

        //then
        assertThat(found).isTrue();
        assertThat(notFound1).isFalse();
        assertThat(notFound2).isFalse();
        assertThat(notFound3).isFalse();
    }

    private void assertEqualReservationElements(final Reservation reservation, final long id, final String name,
                                                final String date, final long timeId, final String time) {

        assertThat(reservation.getId()).isEqualTo(id);
        assertThat(reservation.getName()).isEqualTo(name);
        assertThat(reservation.getDate()).isEqualTo(date);
        assertThat(reservation.getTime().getId()).isEqualTo(timeId);
        assertThat(reservation.getTime().getStartAt()).isEqualTo(time);
    }

}
