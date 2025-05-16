package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;

@JdbcTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationRepositoryTest {

    ReservationRepository repository;
    @Autowired
    JdbcTemplate template;

    @BeforeEach
    void setUp() {
        repository = new ReservationRepositoryImpl(template);
    }

    @Test
    void save() {
        //given
        final ReservationTime time = new ReservationTime(1L, LocalTime.now());
        final ReservationTheme theme = new ReservationTheme(1L, "test", "test", "test");
        final Member member = new Member(1L, MemberRole.ADMIN, "test", " test", "test", "1111");
        final Reservation reservation = new Reservation(member, LocalDate.now().plusDays(1), time, theme);

        //when
        final Reservation expected = repository.saveWithMember(reservation);

        //then
        assertAll(
                () -> assertThat(expected.getMemberId()).isEqualTo(member.getId()),
                () -> assertThat(expected.getDate()).isEqualTo(reservation.getDate()),
                () -> assertThat(expected.getTheme().getId()).isEqualTo(theme.getId()),
                () -> assertThat(expected.getTime().getId()).isEqualTo(time.getId())
        );
    }

    @Test
    void findById() {
        //given
        final ReservationTime time = new ReservationTime(1L, LocalTime.now());
        final ReservationTheme theme = new ReservationTheme(1L, "test", "test", "test");
        final Member member = new Member(1L, MemberRole.ADMIN, "test", " test", "test", "1111");
        final Reservation reservation = new Reservation(member, LocalDate.now().plusDays(1), time, theme);
        final Reservation saved = repository.saveWithMember(reservation);

        //when
        final Reservation expected = repository.findById(saved.getId()).get();

        //then
        assertThat(expected.getId()).isEqualTo(saved.getId());
    }


    @Test
    void findAll() {
        //given
        final ReservationTime time = new ReservationTime(1L, LocalTime.now());
        final ReservationTheme theme = new ReservationTheme(1L, "test", "test", "test");
        final Member member = new Member(1L, MemberRole.ADMIN, "test", " test", "test", "1111");
        final Reservation reservation = new Reservation(member, LocalDate.now().plusDays(1), time, theme);
        repository.saveWithMember(reservation);

        //when
        List<Reservation> reservations = repository.findAllReservationsV2();

        //then
        assertThat(reservations).hasSizeGreaterThan(1);
    }


    @Test
    void deleteById() {
        //given
        final ReservationTime time = new ReservationTime(1L, LocalTime.now());
        final ReservationTheme theme = new ReservationTheme(1L, "test", "test", "test");
        final Member member = new Member(1L, MemberRole.ADMIN, "test", " test", "test", "1111");
        final Reservation reservation = new Reservation(member, LocalDate.now().plusDays(1), time, theme);
        final Reservation saved = repository.saveWithMember(reservation);

        //when
        final int expected = repository.deleteById(saved.getId());

        //then
        assertThat(expected).isPositive();
    }

    @Test
    void existByDateAndTimeIdAndThemeId() {
        //given
        final ReservationTime time = new ReservationTime(1L, LocalTime.now());
        final ReservationTheme theme = new ReservationTheme(1L, "test", "test", "test");
        final Member member = new Member(1L, MemberRole.ADMIN, "test", " test", "test", "1111");
        final Reservation reservation = new Reservation(member, LocalDate.now().plusDays(1), time, theme);
        repository.saveWithMember(reservation);

        //when
        final boolean expected = repository.existByDateAndTimeIdAndThemeId(LocalDate.now().plusDays(1), time.getId(),
                theme.getId());

        //then
        assertThat(expected).isTrue();
    }
}
