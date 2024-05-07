package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Sql(scripts = "/truncate.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class JdbcReservationRepositoryImplTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    private final String name = "tre";
    private final LocalDate date1 = LocalDate.parse("2060-01-01");
    private final LocalDate date2 = LocalDate.parse("2070-01-01");

    private ReservationTime reservationTime;
    private Theme theme;
    private Reservation reservation1;
    private Reservation reservation2;

    @BeforeEach
    void addInitialData() {
        reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.of(5, 30)));
        theme = themeRepository.save(new Theme(
            "방탈출",
            "방탈출하는 게임",
            "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        ));
        reservation1 = new Reservation(name, date1, reservationTime, theme);
        reservation2 = new Reservation(name, date2, reservationTime, theme);
    }

    @DisplayName("예약 정보를 DB에 저장한다.")
    @Test
    void save() {
        Reservation savedReservation = reservationRepository.save(reservation1);

        assertThat(savedReservation)
            .isEqualTo(new Reservation(savedReservation.getId(), name, date1, reservationTime, theme));
    }

    @DisplayName("모든 예약 정보를 DB에서 조회한다.")
    @Test
    void findAll() {
        Reservation saved1 = reservationRepository.save(reservation1);
        Reservation saved2 = reservationRepository.save(reservation2);

        assertThat(reservationRepository.findAll()).containsExactly(saved1, saved2);
    }

    @DisplayName("id값을 통해 예약 정보를 DB에서 삭제한다.")
    @Test
    void deleteById() {
        Reservation saved1 = reservationRepository.save(reservation1);
        Reservation saved2 = reservationRepository.save(reservation2);

        reservationRepository.deleteById(saved1.getId());

        assertThat(reservationRepository.findAll()).containsExactly(saved2);
    }

    @DisplayName("time_id값을 통해 예약이 존재하는지를 구한다.")
    @Test
    void isExistsHavingTimeId() {
        Reservation savedReservation = reservationRepository.save(reservation1);
        ReservationTime savedReservationTime = reservationTimeRepository.save(
            new ReservationTime(LocalTime.parse("11:11")));

        Long existingTimeId = savedReservation.getTime().getId();
        Long nonExistingTimeId = savedReservationTime.getId();

        assertAll(
            () -> assertThat(reservationRepository.isTimeIdExists(existingTimeId)).isTrue(),
            () -> assertThat(reservationRepository.isTimeIdExists(nonExistingTimeId)).isFalse()
        );
    }

    @DisplayName("date, time_id, theme_id로 중복 예약이 존재하는지를 구한다.")
    @Test
    void isDuplication() {
        reservationRepository.save(reservation1);

        assertThat(reservationRepository.isDuplicated(date1, reservationTime.getId(), theme.getId()))
            .isTrue();
    }
}
