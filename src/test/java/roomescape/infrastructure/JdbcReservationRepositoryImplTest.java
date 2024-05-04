package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;

@JdbcTest
class JdbcReservationRepositoryImplTest {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;


    private ReservationDate reservationDate = new ReservationDate("2040-01-01");
    private ReservationTime reservationTime;
    private Theme theme;

    @Autowired
    JdbcReservationRepositoryImplTest(JdbcTemplate jdbcTemplate) {
        this.reservationRepository = new JdbcReservationRepositoryImpl(jdbcTemplate);
        this.reservationTimeRepository = new JdbcReservationTimeRepositoryImpl(jdbcTemplate);
        this.themeRepository = new JdbcThemeRepositoryImpl(jdbcTemplate);
    }

    @BeforeEach
    void setUp() {
        reservationTime = reservationTimeRepository.save(new ReservationTime("05:30"));
        theme = themeRepository.save(
            new Theme("방탈출", "방탈출하는 게임", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
    }

    @DisplayName("예약 정보를 DB에 저장한다.")
    @Test
    void save() {
        Reservation reservation = new Reservation("브리", reservationDate, reservationTime, theme);

        Reservation actual = reservationRepository.save(reservation);
        Reservation expected = new Reservation(
            actual.getId(),
            reservation.getName(),
            reservation.getReservationDate(),
            reservation.getReservationTime(),
            reservation.getTheme());

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("모든 예약 정보를 DB에서 조회한다.")
    @Test
    void findAll() {
        Reservation save1 = reservationRepository.save(
            new Reservation("브리", reservationDate, reservationTime, theme));
        Reservation save2 = reservationRepository.save(
            new Reservation("솔라", reservationDate, reservationTime, theme));

        List<Reservation> actual = reservationRepository.findAll();
        List<Reservation> expected = List.of(save1, save2);

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @DisplayName("id값을 통해 예약 정보를 DB에서 삭제한다.")
    @Test
    void deleteById() {
        Reservation reservation = new Reservation("브리", reservationDate, reservationTime, theme);
        Reservation save = reservationRepository.save(reservation);

        reservationRepository.deleteById(save.getId());

        assertThat(reservationRepository.findAll().isEmpty()).isTrue();
    }

    @DisplayName("time_id값을 통해 예약이 존재하는지를 구한다.")
    @Test
    void isTimeIdExists() {
        Reservation reservation1 = new Reservation("brown1", new ReservationDate("2040-01-01"), reservationTime, theme);
        Reservation reservation2 = new Reservation("brown2", new ReservationDate("2050-02-02"), reservationTime, theme);
        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);

        boolean actual = reservationRepository.isTimeIdExists(reservationTime.getId());

        assertThat(actual).isTrue();
    }

    @DisplayName("date, time_id, theme_id로 중복 예약이 존재하는지를 구한다.")
    @Test
    void isDuplication() {
        Reservation reservation = new Reservation("brown1", reservationDate, reservationTime, theme);
        reservationRepository.save(reservation);
        boolean actual = reservationRepository.isDuplicated(reservationDate.getDate(), reservationTime.getId(),
            theme.getId());

        assertThat(actual).isTrue();
    }
}
