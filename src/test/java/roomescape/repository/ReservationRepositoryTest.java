package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.util.TestDataInitializer;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TestDataInitializer dataInitializer;

    private Theme theme;
    private ReservationTime ten;
    private ReservationTime eleven;
    private ReservationTime noon;

    @BeforeEach
    void setUp() {
        theme = dataInitializer.createTheme("테마", "설명", "/images/themes/theme.webp");
        ten = dataInitializer.createReservationTime(LocalTime.of(10, 0));
        eleven = dataInitializer.createReservationTime(LocalTime.of(11, 0));
        noon = dataInitializer.createReservationTime(LocalTime.of(12, 0));
    }

    @Test
    void 예약_목록을_페이징_조회한다() {
        createReservation("사용자일", ten);
        Reservation second = createReservation("사용자이", eleven);
        Reservation third = createReservation("사용자삼", noon);

        List<Reservation> reservations = reservationRepository.findAll(2, 1);

        assertThat(reservations).extracting(Reservation::getId)
                .containsExactly(second.getId(), third.getId());
    }

    @Test
    void 이름으로_예약_목록을_조회한다() {
        Reservation first = createReservation("고래", ten);
        createReservation("상어", eleven);
        Reservation second = createReservation("고래", noon);

        List<Reservation> reservations = reservationRepository.findByName("고래");

        assertThat(reservations).extracting(Reservation::getId)
                .containsExactly(first.getId(), second.getId());
    }

    @Test
    void 이름에_해당하는_예약이_없으면_빈_목록을_반환한다() {
        createReservation("고래", ten);

        List<Reservation> reservations = reservationRepository.findByName("상어");

        assertThat(reservations).isEmpty();
    }

    private Reservation createReservation(String name, ReservationTime time) {
        return dataInitializer.createReservation(name, LocalDate.of(2026, 5, 20), time.getId(), theme.getId());
    }
}
