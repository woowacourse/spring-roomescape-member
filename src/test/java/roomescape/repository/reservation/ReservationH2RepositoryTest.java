package roomescape.repository.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static roomescape.InitialDataFixture.INITIAL_RESERVATION_COUNT;
import static roomescape.InitialDataFixture.NOT_RESERVATION_THEME;
import static roomescape.InitialDataFixture.NOT_RESERVATION_TIME;
import static roomescape.InitialDataFixture.NO_RESERVATION_DATE;
import static roomescape.InitialDataFixture.RESERVATION_1;
import static roomescape.InitialDataFixture.RESERVATION_2;
import static roomescape.InitialDataFixture.THEME_2;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Name;
import roomescape.domain.Reservation;

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

        assertThat(found).hasSize(INITIAL_RESERVATION_COUNT);
    }

    @Test
    @DisplayName("이미 존재하는 예약과 날짜, 시간, 테마가 같으면 중복되는 예약이다.")
    void isAlreadyBooked() {
        boolean alreadyBooked = reservationH2Repository.isAlreadyBooked(RESERVATION_1);

        assertThat(alreadyBooked).isEqualTo(true);
    }

    @Test
    @DisplayName("이미 존재하는 예약과 날짜가 다르면 중복되는 예약이 아니다.")
    void isNotBookedDate() {
        Reservation reservation = new Reservation(
                RESERVATION_1.getId(),
                RESERVATION_1.getName(),
                NO_RESERVATION_DATE,
                RESERVATION_1.getTime(),
                RESERVATION_1.getTheme()
        );
        boolean alreadyBooked = reservationH2Repository.isAlreadyBooked(reservation);

        assertThat(alreadyBooked).isEqualTo(false);
    }

    @Test
    @DisplayName("이미 존재하는 예약과 시간이 다르면 중복되는 예약이 아니다.")
    void isNotBookedTime() {
        Reservation reservation = new Reservation(
                RESERVATION_1.getId(),
                RESERVATION_1.getName(),
                RESERVATION_1.getDate(),
                NOT_RESERVATION_TIME,
                RESERVATION_1.getTheme()
        );
        boolean alreadyBooked = reservationH2Repository.isAlreadyBooked(reservation);

        assertThat(alreadyBooked).isEqualTo(false);
    }

    @Test
    @DisplayName("이미 존재하는 예약과 테마가 다르면 중복되는 예약이 아니다.")
    void isNotBookedTheme() {
        Reservation reservation = new Reservation(
                RESERVATION_1.getId(),
                RESERVATION_1.getName(),
                RESERVATION_1.getDate(),
                RESERVATION_1.getTime(),
                NOT_RESERVATION_THEME
        );
        boolean alreadyBooked = reservationH2Repository.isAlreadyBooked(reservation);

        assertThat(alreadyBooked).isEqualTo(false);
    }
}
