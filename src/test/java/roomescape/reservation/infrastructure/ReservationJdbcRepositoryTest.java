package roomescape.reservation.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.domain.repository.MemberRepository;
import roomescape.member.infrastructure.MemberJdbcRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.repostiory.ReservationRepository;
import roomescape.reservation.domain.repostiory.ReservationTimeRepository;
import roomescape.reservation.domain.repostiory.ThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class ReservationJdbcRepositoryTest {
    private ReservationRepository reservationRepository;
    private String reservationDate;
    private ReservationTime reservationTime;
    private Theme theme;
    private Member member;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        reservationRepository = new ReservationJdbcRepository(jdbcTemplate);
        ReservationTimeRepository reservationTimeRepository = new ReservationTimeJdbcRepository(jdbcTemplate);
        ThemeRepository themeRepository = new ThemeJdbcRepository(jdbcTemplate);
        MemberRepository memberRepository = new MemberJdbcRepository(jdbcTemplate);

        reservationDate = LocalDate.now().plusDays(1).toString();
        reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.now().toString()));
        theme = themeRepository.save(new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        member = memberRepository.save(new Member("lini", "lini@email.com", "lini123", Role.GUEST));
    }

    @DisplayName("새로운 예약을 저장한다.")
    @Test
    void saveReservation() {
        //given
        Reservation reservation = new Reservation(reservationDate, member, reservationTime, theme);

        //when
        Reservation result = reservationRepository.save(reservation);

        //then
        assertThat(result.getId()).isNotZero();
    }

    @DisplayName("모든 예약 내역을 조회한다.")
    @Test
    void findAllReservationTest() {
        //given
        Reservation reservation = new Reservation(reservationDate, member, reservationTime, theme);
        reservationRepository.save(reservation);
        int expectedSize = 1;

        //when
        List<Reservation> reservations = reservationRepository.findAll();

        //then
        assertThat(reservations.size()).isEqualTo(expectedSize);
    }

    @DisplayName("id로 예약을 삭제한다.")
    @Test
    void deleteReservationByIdTest() {
        //given
        Reservation reservation = new Reservation(reservationDate, member, reservationTime, theme);
        Reservation target = reservationRepository.save(reservation);
        int expectedSize = 0;

        //when
        reservationRepository.deleteById(target.getId());

        //then
        assertThat(reservationRepository.findAll().size()).isEqualTo(expectedSize);
    }

    @DisplayName("주어진 일정, 테마가 동일한 예약이 존재한다.")
    @Test
    void existsByDateAndTimeAndThemeTest() {
        //given
        Reservation reservation = new Reservation(reservationDate, member, reservationTime, theme);
        reservationRepository.save(reservation);

        //when
        boolean result = reservationRepository.existsByDateAndTimeAndTheme(reservationDate, reservationTime.getId(), theme.getId());

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("주어진 일정, 테마가 동일한 예약이 존재하지 않는다.")
    @Test
    void notExistsByDateAndTimeAndThemeTest() {
        //given
        String newDate = LocalDate.now().plusDays(2).toString();
        Reservation reservation = new Reservation(reservationDate, member, reservationTime, theme);
        reservationRepository.save(reservation);

        //when
        boolean result = reservationRepository.existsByDateAndTimeAndTheme(newDate, reservationTime.getId(),
                theme.getId());

        //then
        assertThat(result).isFalse();
    }

    @DisplayName("주어진 시간에 대한 예약이 존재한다.")
    @Test
    void existsByTimeIdTest() {
        //given
        Reservation reservation = new Reservation(reservationDate, member, reservationTime, theme);
        reservationRepository.save(reservation);

        //when
        boolean result = reservationRepository.existsByTimeId(reservationTime.getId());

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("주어진 시간에 대한 예약이 존재하지 않는다.")
    @Test
    void notExistsByTimeIdTest() {
        //when
        boolean result = reservationRepository.existsByTimeId(reservationTime.getId());

        //then
        assertThat(result).isFalse();
    }

    @DisplayName("주어진 테마에 대한 예약이 존재한다.")
    @Test
    void existsByThemeIdTest() {
        //given
        Reservation reservation = new Reservation(reservationDate, member, reservationTime, theme);
        reservationRepository.save(reservation);

        //when
        boolean result = reservationRepository.existsByThemeId(theme.getId());

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("주어진 테마에 대한 예약이 존재하지 않는다.")
    @Test
    void notExistsByThemeIdTest() {
        //when
        boolean result = reservationRepository.existsByThemeId(theme.getId());

        //then
        assertThat(result).isFalse();
    }

    @DisplayName("주어진 식별자의 예약이 존재한다.")
    @Test
    void existsByIdTest() {
        //given
        Reservation reservation = new Reservation(reservationDate, member, reservationTime, theme);
        Reservation target = reservationRepository.save(reservation);

        //when
        boolean result = reservationRepository.existsById(target.getId());

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("주어진 식별자의 예약이 존재하지 않는다.")
    @Test
    void notExistsByIdTest() {
        //when
        boolean result = reservationRepository.existsByThemeId(0);

        //then
        assertThat(result).isFalse();
    }


}
