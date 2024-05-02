package roomescape.repository.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.InitialDataFixture;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import static org.assertj.core.api.Assertions.*;
import static roomescape.InitialDataFixture.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("/initial_test_data.sql")
class ReservationH2RepositoryTest {

    @Autowired
    private ReservationH2Repository reservationH2Repository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("Reservation을 저장하면 id가 포함된 Reservation이 반환된다.")
    void save() {
        Reservation reservation = new Reservation(
                new Name("네오"),
                RESERVATION_2.getDate(),
                RESERVATION_2.getTime(),
                THEME_2
        );

        Reservation save = reservationH2Repository.save(reservation);

        assertThat(save.getId()).isNotNull();
    }

    @Test
    @DisplayName("과거 시간을 예약하려는 경우 예외를 발생시킨다.")
    void savePastGetTime() {
        Reservation reservation = new Reservation(
                new Name("네오"),
                RESERVATION_1.getDate(),
                RESERVATION_1.getTime(),
                THEME_2
        );
        assertThatThrownBy(() -> reservationH2Repository.save(reservation))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("같은 날짜, 시간, 테마에 예약을 하는 경우 예외를 발생시킨다.")
    void saveSameReservation() {
        Reservation reservation = new Reservation(
                new Name("네오"),
                RESERVATION_2.getDate(),
                RESERVATION_2.getTime(),
                RESERVATION_2.getTheme()
        );
        assertThatThrownBy(() -> reservationH2Repository.save(reservation))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("시간과 날짜만 같고 테마가 다른 경우 예약에 성공한다.")
    void saveOnlySameGetDateGetTime() {
        Reservation reservation = new Reservation(
                new Name("네오"),
                RESERVATION_2.getDate(),
                RESERVATION_2.getTime(),
                THEME_2
        );
        assertThatNoException().isThrownBy(() -> reservationH2Repository.save(reservation));
    }

    @Test
    @DisplayName("테마가 같고 날짜가 다른 경우 예약에 성공한다.")
    void saveOnlySameTheme() {
        Reservation reservation = new Reservation(
                new Name("네오"),
                RESERVATION_2.getDate().plusDays(1),
                RESERVATION_2.getTime(),
                RESERVATION_2.getTheme()
        );
        assertThatNoException().isThrownBy(() -> reservationH2Repository.save(reservation));
    }

    @Test
    @DisplayName("Reservation을 제거한다.")
    void delete() {
        reservationH2Repository.delete(RESERVATION_1.getId());

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation", Integer.class);

        assertThat(count).isOne();
    }

    @Test
    @DisplayName("저장된 모든 Reservation을 반환한다.")
    void findAll() {
        List<Reservation> found = reservationH2Repository.findAll();

        assertThat(found).hasSize(2);
    }
}
