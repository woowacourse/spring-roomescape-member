package roomescape.dao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.domain.Reservation;

@JdbcTest
@Sql(scripts = "/test_data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationJDBCRepositoryTest {
    private ReservationRepository reservationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ReservationTimeRepository reservationTimeRepository;
    private ThemeRepository themeRepository;
    private LoginMemberRepository loginMemberRepository;
    private String date;

    @BeforeEach
    void setUp() {
        reservationRepository = new ReservationJDBCRepository(jdbcTemplate);
        reservationTimeRepository = new ReservationTimeJDBCRepository(jdbcTemplate);
        themeRepository = new ThemeJDBCRepository(jdbcTemplate);
        loginMemberRepository = new LoginMemberJDBCRepository(jdbcTemplate);

        date = LocalDate.now().plusDays(1).toString();
    }

    @DisplayName("새로운 예약을 저장한다.")
    @Test
    void saveReservation() {
        //given
        Reservation reservation = new Reservation(
                date, themeRepository.findById(1).get(), reservationTimeRepository.findById(1).get(),
                loginMemberRepository.findById(1).get());

        //when
        Reservation result = reservationRepository.save(reservation);

        //then
        assertThat(result.getId()).isNotZero();
    }

    @DisplayName("모든 예약 내역을 조회한다.")
    @Test
    void findAllReservationTest() {
        //given
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
        int expectedSize = 0;

        //when
        reservationRepository.deleteById(1);

        //then
        assertThat(reservationRepository.findAll().size()).isEqualTo(expectedSize);
    }

    @DisplayName("주어진 시간에 대한 예약이 존재한다.")
    @Test
    void existsByTimeIdTest() {
        //when
        boolean result = reservationRepository.existsByTimeId(1);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("주어진 시간에 대한 예약이 존재하지 않는다.")
    @Test
    void notExistsByTimeIdTest() {
        //when
        boolean result = reservationRepository.existsByTimeId(2);

        //then
        assertThat(result).isFalse();
    }

    @DisplayName("주어진 테마에 대한 예약이 존재한다.")
    @Test
    void existsByThemeIdTest() {
        //when
        boolean result = reservationRepository.existsByThemeId(1);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("주어진 테마에 대한 예약이 존재하지 않는다.")
    @Test
    void notExistsByThemeIdTest() {
        //when
        boolean result = reservationRepository.existsByThemeId(2);

        //then
        assertThat(result).isFalse();
    }
}
