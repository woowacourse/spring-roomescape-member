package roomescape.dao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
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
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@JdbcTest
@Sql(scripts = "/test_data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationJDBCRepositoryTest {
    private ReservationRepository reservationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private String date;
    private ReservationTime reservationTime;
    private Theme theme;

    @BeforeEach
    void setUp() {
        reservationRepository = new ReservationJDBCRepository(jdbcTemplate);
        ReservationTimeRepository reservationTimeRepository = new ReservationTimeJDBCRepository(jdbcTemplate);
        ThemeRepository themeRepository = new ThemeJDBCRepository(jdbcTemplate);

        date = LocalDate.now().plusDays(1).toString();
        String startAt = LocalTime.now().toString();
        reservationTime = reservationTimeRepository.save(new ReservationTime(startAt));
        theme = themeRepository.save(new Theme("레벨5 탈출", "우테코 레벨5를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
    }

    @DisplayName("새로운 예약을 저장한다.")
    @Test
    void saveReservation() {
        //given
        Reservation reservation = new Reservation("브라운", date, reservationTime, theme);

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
        boolean result = reservationRepository.existsByTimeId(reservationTime.getId());

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
        boolean result = reservationRepository.existsByThemeId(theme.getId());

        //then
        assertThat(result).isFalse();
    }
}
