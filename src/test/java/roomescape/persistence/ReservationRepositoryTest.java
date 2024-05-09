package roomescape.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.domain.Member;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;

/*
 * 테스트 데이터베이스 초기 데이터
 * {ID=1, NAME=브라운, DATE=2024-05-04, TIME={ID=1, START_AT="10:00"}}
 * {ID=2, NAME=엘라, DATE=2024-05-04, TIME={ID=2, START_AT="11:00"}}
 * {ID=3, NAME=릴리, DATE=2023-08-05, TIME={ID=2, START_AT="11:00"}}
 */
@JdbcTest
@Sql(scripts = "/reset_test_data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
class ReservationRepositoryTest {

    private ReservationRepository reservationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        reservationRepository = new ReservationRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("모든 예약 데이터를 가져온다.")
    void findAll() {
        // when
        List<Reservation> reservations = reservationRepository.findAll();

        // then
        assertThat(reservations).hasSize(3);
    }

    @Test
    @DisplayName("특정 예약 id의 데이터를 조회한다.")
    void findById() {
        // given
        Reservation targetReservation = new Reservation(
                2L,
                new Member(1L, new Name("test"), "test@gmail.com", Role.USER),
                new ReservationDate(LocalDate.parse("2024-05-04")),
                new ReservationTime(LocalTime.parse("10:00")),
                new Theme(null, null, null)
        );

        // when
        Reservation findReservation = reservationRepository.findById(targetReservation.getId());

        // then
        assertThat(findReservation).isEqualTo(targetReservation);
    }

    @Test
    @DisplayName("새로운 예약을 생성한다.")
    void create() {
        // given
        Member member = new Member(1L, new Name("test"), "test@gmail.com", Role.USER);
        ReservationDate date = new ReservationDate(LocalDate.parse("2023-08-05"));
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.parse("10:00"));
        Theme theme = new Theme(1L, null, null, null);
        Reservation createReservation = new Reservation(member, date, reservationTime, theme);

        // when
        reservationRepository.create(createReservation);
        List<Reservation> reservations = reservationRepository.findAll();

        // then
        assertThat(reservations).hasSize(4);
    }

    @Test
    @DisplayName("특정 id를 가진 예약을 삭제한다.")
    void remove() {
        // given
        Long id = 2L;

        // when
        reservationRepository.removeById(id);

        // then
        assertThatThrownBy(() -> reservationRepository.findById(id)).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("동일한 날짜, 시간, 테마의 예약이 있는지 확인한다.")
    void hasDuplicateDateTimeThemeReservation() {
        // given
        Member member = new Member(1L, new Name("test"), "test@gmail.com", Role.USER);
        ReservationDate date = new ReservationDate(LocalDate.parse("2024-05-04"));
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.parse("10:00"));
        Theme theme = new Theme(1L, "테마1", "테마1설명", "테마1이미지");
        Reservation reservation = new Reservation(member, date, reservationTime, theme);

        // when
        boolean result = reservationRepository.hasDuplicateReservation(reservation);

        // then
        assertThat(result).isTrue();
    }
}
