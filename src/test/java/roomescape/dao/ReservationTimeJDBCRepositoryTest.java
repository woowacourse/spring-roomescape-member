package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@JdbcTest
class ReservationTimeJDBCRepositoryTest {
    private ReservationTimeRepository reservationTimeRepository;
    private ReservationRepository reservationRepository;
    private ThemeRepository themeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        reservationTimeRepository = new ReservationTimeJDBCRepository(jdbcTemplate);
        reservationRepository = new ReservationJDBCRepository(jdbcTemplate);
        themeRepository = new ThemeJDBCRepository(jdbcTemplate);
    }

    @DisplayName("새로운 예약 시간을 저장한다.")
    @Test
    void saveReservationTime() {
        //given
        String startAt = "10:00";
        ReservationTime reservationTime = new ReservationTime(startAt);

        //when
        ReservationTime result = reservationTimeRepository.save(reservationTime);

        //then
        assertThat(result.getId()).isNotZero();
    }

    @DisplayName("모든 예약 시간을 조회한다.")
    @Test
    void findAllReservationTimesTest() {
        //given
        String startAt = "10:00";
        ReservationTime reservationTime = new ReservationTime(startAt);
        reservationTimeRepository.save(reservationTime);
        int expectedSize = 1;

        //when
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        //then
        assertThat(reservationTimes.size()).isEqualTo(expectedSize);
    }

    @DisplayName("id로 예약 시간을 조회한다.")
    @Test
    void findReservationTimeByIdTest() {
        //given
        String startAt = "10:00";
        ReservationTime reservationTime = new ReservationTime(startAt);
        ReservationTime target = reservationTimeRepository.save(reservationTime);

        //when
        ReservationTime result = reservationTimeRepository.findById(target.getId()).get();

        //then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(target.getId()),
                () -> assertThat(result.getStartAt()).isEqualTo(startAt)
        );
    }

    @DisplayName("id로 예약을 삭제한다.")
    @Test
    void deleteReservationTimeByIdTest() {
        //given
        String startAt = "10:00";
        ReservationTime reservationTime = new ReservationTime(startAt);
        ReservationTime target = reservationTimeRepository.save(reservationTime);
        int expectedSize = 0;

        //when
        reservationTimeRepository.deleteById(target.getId());

        //then
        assertThat(reservationTimeRepository.findAll().size()).isEqualTo(expectedSize);
    }

    @DisplayName("주어진 시간이 이미 존재한다.")
    @Test
    void existsTime() {
        //given
        String startAt = "10:00";
        ReservationTime reservationTime = new ReservationTime(startAt);
        reservationTimeRepository.save(reservationTime);

        //when
        boolean result = reservationTimeRepository.existsByTime(startAt);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("주어진 시간이 존재하지 않는다.")
    @Test
    void notExistsTime() {
        //given
        String startAt = "10:00";
        ReservationTime reservationTime = new ReservationTime(startAt);
        reservationTimeRepository.save(reservationTime);

        //when
        String newStartAt = "10:01";
        boolean result = reservationTimeRepository.existsByTime(newStartAt);

        //then
        assertThat(result).isFalse();
    }

    @DisplayName("예약이 가능한 시간이 없다.")
    @Test
    void findNoAvailableTimesByThemeAndDate() {
        //given
        ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime("10:00"));
        Theme theme = themeRepository.save(new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        Reservation reservation = new Reservation("브라운", "2222-05-01", reservationTime, theme);
        reservationRepository.save(reservation);

        //when
        List<ReservationTime> result = reservationTimeRepository.findByDateAndTheme(
                reservation.getDate(), theme.getId());

        //then
        assertThat(result).hasSize(0);
    }

    @DisplayName("예약이 가능한 시간이 있다.")
    @Test
    void findAvailableTimesByThemeAndDate() {
        //given
        reservationTimeRepository.save(new ReservationTime("10:00"));

        //when
        List<ReservationTime> result = reservationTimeRepository.findByDateAndTheme("2222-05-01", 0);

        //then
        assertThat(result).hasSize(1);
    }
}
