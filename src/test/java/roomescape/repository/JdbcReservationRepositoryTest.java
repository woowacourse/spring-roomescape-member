package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static roomescape.TestSetting.createReservation;
import static roomescape.TestSetting.createReservationTime;
import static roomescape.TestSetting.isEqualsReservation;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JdbcReservationRepositoryTest {

    @Autowired
    JdbcReservationRepository jdbcReservationRepository;

    @Autowired
    JdbcReservationTimeRepository jdbcReservationTimeRepository;

    @BeforeEach
    void beforeEach() {
        ReservationTime reservationTime = createReservationTime();
        jdbcReservationTimeRepository.save(reservationTime);
    }

    @Test
    void 예약_저장() {
        //given
        Reservation reservation = createReservation();

        //when
        Long savedId = jdbcReservationRepository.save(reservation);

        //then
        Reservation savedReservation = jdbcReservationRepository.findById(savedId);
        assertThat(isEqualsReservation(reservation, savedReservation)).isTrue();
    }

    @Test
    void 전체_예약_조회() {
        //given
        Reservation reservation1 = createReservation();
        Reservation reservation2 = createReservation();
        jdbcReservationRepository.save(reservation1);
        jdbcReservationRepository.save(reservation2);

        //when
        List<Reservation> reservations = jdbcReservationRepository.findAll();

        //then
        assertThat(reservations).hasSize(2);
        assertTrue(reservations.stream()
                .anyMatch(reservation -> isEqualsReservation(reservation, reservation1)));
        assertTrue(reservations.stream()
                .anyMatch(reservation -> isEqualsReservation(reservation, reservation2)));
    }

    @Test
    void 예약_삭제() {
        //given
        Reservation reservationToDelete = createReservation();
        Long savedId = jdbcReservationRepository.save(reservationToDelete);

        //when
        jdbcReservationRepository.delete(savedId);

        //then
        List<Reservation> reservations = jdbcReservationRepository.findAll();
        assertTrue(reservations.stream()
                .noneMatch(reservation -> isEqualsReservation(reservation, reservationToDelete)));
    }
}
