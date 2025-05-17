package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;

@JdbcTest
@ActiveProfiles("test")
class RoomescapeRepositoryTest {

    @Autowired
    DataSource dataSource;
    JdbcTemplate template;
    RoomescapeRepository repository;

    @BeforeEach
    void setUp() {
        repository = new RoomescapeRepositoryImpl(dataSource);
        template = new JdbcTemplate(dataSource);
        template.execute(
                "INSERT INTO member (name, email, password, role) VALUES ('제프리', 'jeffrey@gmail.com', '1234!@#$', 'USER')");
        template.execute(
                "INSERT INTO reservation_theme (name, description, thumbnail) VALUES ('레벨 1탈출', '설명1', '썸네일1')");
        template.execute("insert into reservation_time (start_at) values ('15:40')");
        template.execute("insert into reservation (date, member_id, time_id, theme_id) values ('2023-08-05',1, 1, 1)");
    }

    @AfterEach
    void tearDown() {
        template.execute("DELETE FROM reservation");
        template.execute("DELETE FROM reservation_time");
        template.execute("DELETE FROM reservation_theme");
        template.execute("DELETE FROM member");

        template.execute("ALTER TABLE member ALTER COLUMN id RESTART WITH 1");
        template.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        template.execute("ALTER TABLE reservation_theme ALTER COLUMN id RESTART WITH 1");
        template.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
    }

    @Test
    void findById() {
        //when
        Reservation reservation = repository.findById(1L).get();

        //then
        assertEqualReservationElements(reservation, 1L, "2023-08-05", "제프리", "15:40", "레벨 1탈출");
    }

    @Test
    void findByDate() {
        //when
        List<Reservation> found = repository.findByDate(LocalDate.of(2023, 8, 5));
        List<Reservation> notFound = repository.findByDate(LocalDate.of(2023, 8, 6));

        //then
        assertThat(found).hasSize(1);
        assertThat(notFound).isEmpty();
        assertEqualReservationElements(found.getFirst(), 1L, "2023-08-05", "제프리", "15:40", "레벨 1탈출");
    }

    @Test
    void findAll() {
        //when
        List<Reservation> reservations = repository.findAll(null, null, null, null);

        //then
        assertThat(reservations).isNotEmpty();
        assertEqualReservationElements(reservations.getFirst(), 1L, "2023-08-05", "제프리", "15:40", "레벨 1탈출");
    }

    @Test
    void findAllByFiltering() {
        //when
        List<Reservation> reservationsWithThemeId = repository.findAll(null, 1L, null, null);
        List<Reservation> reservationsWithMemberIdAndThemeId = repository.findAll(1L, 1L, null, null);
        List<Reservation> emptyReservations = repository.findAll(null, 999L, null, null);

        //then
        assertThat(reservationsWithThemeId).hasSize(1);
        assertThat(reservationsWithMemberIdAndThemeId).hasSize(1);
        assertEqualReservationElements(reservationsWithThemeId.getFirst(), 1L, "2023-08-05", "제프리", "15:40", "레벨 1탈출");
        assertEqualReservationElements(reservationsWithMemberIdAndThemeId.getFirst(), 1L, "2023-08-05", "제프리", "15:40", "레벨 1탈출");
        assertThat(emptyReservations).isEmpty();
    }

    @Test
    void save() {
        //given
        int beforeSize = repository.findAll(null, null, null, null).size();
        template.execute(
                "INSERT INTO member (name, email, password, role) VALUES ('윌슨', 'wilson@gmail.com', '1234!@#$', 'USER')");
        Member member = new Member(2L, "윌슨", "wilson@gmail.com", "1234!@#$", MemberRole.USER);
        ReservationTheme theme = new ReservationTheme(1L, "레벨 1탈출", "설명1", "썸네일1");
        ReservationTime time = new ReservationTime(1L, LocalTime.parse("15:40"));
        Reservation reservation = new Reservation(LocalDate.parse("2023-08-05"), member, time, theme);

        //when
        Reservation saved = repository.save(reservation);
        Reservation firstReservation = repository.findById(1L).get();
        Reservation secondReservation = repository.findById(2L).get();

        //then
        assertEqualReservationElements(saved, 2L, "2023-08-05", "윌슨", "15:40", "레벨 1탈출");
        assertEqualReservationElements(firstReservation, 1L, "2023-08-05", "제프리", "15:40", "레벨 1탈출");
        assertEqualReservationElements(secondReservation, 2L, "2023-08-05", "윌슨", "15:40", "레벨 1탈출");
        assertThat(repository.findAll(null, null, null, null)).hasSize(beforeSize + 1);
    }

    @Test
    void deleteById() {
        //when
        boolean result = repository.deleteById(1L);

        //then
        assertThat(result).isTrue();
        assertThat(repository.findAll(null, null, null, null)).isEmpty();
    }

    @Test
    void existsByThemeId() {
        //given
        long exists = 1L;
        long nonExists = 9999L;

        //when
        boolean isExists = repository.existsByThemeId(exists);
        boolean isNonExists = repository.existsByThemeId(nonExists);

        //then
        assertThat(isExists).isTrue();
        assertThat(isNonExists).isFalse();
    }

    @Test
    void existsByTimeId() {
        //given
        long exists = 1L;
        long nonExists = 9999L;

        //when
        boolean isExists = repository.existsByTimeId(exists);
        boolean isNonExists = repository.existsByTimeId(nonExists);

        //then
        assertThat(isExists).isTrue();
        assertThat(isNonExists).isFalse();
    }

    @Test
    void existsByDateAndTime() {
        //given
        LocalDate date = LocalDate.parse("2023-08-05");
        LocalDate anotherDate = LocalDate.parse("2023-08-06");
        ReservationTime time = new ReservationTime("15:40");
        ReservationTime anotherTime = new ReservationTime("15:41");

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

    private void assertEqualReservationElements(final Reservation reservation,
                                                final long id,
                                                final String date,
                                                final String name,
                                                final String time,
                                                final String theme) {

        assertThat(reservation.getId()).isEqualTo(id);
        assertThat(reservation.getDate()).isEqualTo(date);
        assertThat(reservation.getMember().getName()).isEqualTo(name);
        assertThat(reservation.getTime().getStartAt()).isEqualTo(time);
        assertThat(reservation.getTheme().getName()).isEqualTo(theme);
    }
}
