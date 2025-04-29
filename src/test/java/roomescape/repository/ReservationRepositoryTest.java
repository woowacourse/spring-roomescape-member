package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReservationRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "12:00");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ? )", "Lemon", "2025-04-22",
                1);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)", "DDinghwa", "2025-06-03",
                2);
    }

    @Test
    @DisplayName("아이디를 통해 예약을 가져온다.")
    void findReservationByIdTest() {
        //given
        long id = 1L;

        //when
        Reservation reservation = reservationRepository.findById(id);

        //then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(reservation.getId()).isEqualTo(1);
            softAssertions.assertThat(reservation.getName()).isEqualTo("Lemon");
            softAssertions.assertThat(reservation.getDate()).isEqualTo(LocalDate.of(2025, 4, 22));
            softAssertions.assertThat(reservation.getTime().getId()).isEqualTo(1);
            softAssertions.assertThat(reservation.getTime().getStartAt()).isEqualTo(LocalTime.of(10, 0));
        });
    }

    @Test
    @DisplayName("모든 에약을 가져온다.")
    void findALLReservationsTest() {
        //given
        //when
        List<Reservation> reservations = reservationRepository.findAll();

        //then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(reservations).hasSize(2);
            softAssertions.assertThat(reservations.getFirst().getName()).isEqualTo("Lemon");
            softAssertions.assertThat(reservations.getFirst().getDate()).isEqualTo(LocalDate.of(2025, 4, 22));
            softAssertions.assertThat(reservations.getFirst().getTime().getId()).isEqualTo(1);
            softAssertions.assertThat(reservations.getFirst().getTime().getStartAt()).isEqualTo(LocalTime.of(10, 0));

            softAssertions.assertThat(reservations.getLast().getName()).isEqualTo("DDinghwa");
            softAssertions.assertThat(reservations.getLast().getDate()).isEqualTo(LocalDate.of(2025, 6, 3));
            softAssertions.assertThat(reservations.getLast().getTime().getId()).isEqualTo(2);
            softAssertions.assertThat(reservations.getLast().getTime().getStartAt()).isEqualTo(LocalTime.of(12, 0));

        });
    }

    @Test
    @DisplayName("예약을 저장한다.")
    void saveReservationTest() {
        // given
        Reservation reservation = new Reservation(
                "myeonghwa",
                LocalDate.of(1998, 6, 3),
                new ReservationTime(1L));

        // when
        Reservation savedReservation = reservationRepository.save(reservation);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(savedReservation.getId()).isEqualTo(3);
            softAssertions.assertThat(savedReservation.getName()).isEqualTo("myeonghwa");
            softAssertions.assertThat(savedReservation.getDate()).isEqualTo(LocalDate.of(1998, 6, 3));
            softAssertions.assertThat(savedReservation.getTime().getId()).isEqualTo(1);
        });
    }

    @Test
    @DisplayName("아이디를 통해 예약을 삭제한다.")
    void deleteReservationByIdTest() {
        // given
        long id = 1;

        // when
        int row = reservationRepository.deleteById(id);

        // then
        Assertions.assertThat(row).isEqualTo(1);
    }

    @Test
    @DisplayName("날짜와 시간을 선택한다.")
    void selectDateAndTimeTest() {
        // given
        LocalDate duplicatedDate = LocalDate.of(2025, 4, 22);
        Long duplicatedTimeId = 1L;
        LocalDate date = LocalDate.of(2025, 2, 2);
        Long unduplicatedTimeId = 1L;

        // when
        boolean isDuplicatedDateAndTime = reservationRepository.isDuplicateDateAndTime(duplicatedDate,
                duplicatedTimeId);
        boolean unDuplicatedDateAndTime = reservationRepository.isDuplicateDateAndTime(date, unduplicatedTimeId);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(unDuplicatedDateAndTime).isFalse();
            softAssertions.assertThat(isDuplicatedDateAndTime).isTrue();
        });
    }


    @AfterEach
    void tearDown() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
    }
}
