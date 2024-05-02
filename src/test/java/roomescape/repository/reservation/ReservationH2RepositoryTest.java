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
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import static org.assertj.core.api.Assertions.*;

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
                LocalDate.now().plusDays(1),
                new ReservationTime(2L, LocalTime.of(10, 0)),
                new Theme(1L, new Name("레벨2 탈출"), "우테코 레벨2를 탈출하는 내용입니다.", "아무 내용 없음")
        );

        Reservation save = reservationH2Repository.save(reservation);

        assertThat(save.getId()).isNotNull();
    }

    @Test
    @DisplayName("과거 시간을 예약하려는 경우 예외를 발생시킨다.")
    void savePastGetTime() {
        Reservation reservation = new Reservation(
                new Name("네오"),
                LocalDate.of(2023, 4, 24),
                new ReservationTime(2L, LocalTime.of(10, 0)),
                new Theme(1L, new Name("레벨2 탈출"), "우테코 레벨2를 탈출하는 내용입니다.", "아무 내용 없음")
        );
        assertThatThrownBy(() -> reservationH2Repository.save(reservation))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("같은 날짜, 시간, 테마에 예약을 하는 경우 예외를 발생시킨다.")
    void saveSameReservation() {
        Reservation reservation = new Reservation(
                new Name("네오"),
                LocalDate.of(2099, 5, 1), // TODO 더 좋은 방식이 있는지 고민
                new ReservationTime(1L, LocalTime.of(9, 0)),
                new Theme(1L, new Name("레벨2 탈출"), "우테코 레벨2를 탈출하는 내용입니다.", "아무 내용 없음")
        );
        assertThatThrownBy(() -> reservationH2Repository.save(reservation))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("시간과 날짜만 같고 테마가 다른 경우 예약에 성공한다.")
    void saveOnlySameGetDateGetTime() {
        Reservation reservation = new Reservation(
                new Name("네오"),
                LocalDate.of(2099, 5, 1), // TODO 더 좋은 방식이 있는지 고민
                new ReservationTime(3L, LocalTime.of(11, 0)),
                new Theme(2L, new Name("레벨3 탈출"), "우테코 레벨3를 탈출하는 내용입니다.", "아무 내용 없음")
        );
        assertThatNoException().isThrownBy(() -> reservationH2Repository.save(reservation));
    }

    @Test
    @DisplayName("테마가 같고 시간과 날짜가 다른 경우 예약에 성공한다.")
    void saveOnlySameTheme() {
        Reservation reservation = new Reservation(
                new Name("네오"),
                LocalDate.of(2099, 5, 2), // TODO 더 좋은 방식이 있는지 고민
                new ReservationTime(2L, LocalTime.of(10, 0)),
                new Theme(1L, new Name("레벨2 탈출"), "우테코 레벨2를 탈출하는 내용입니다.", "아무 내용 없음")
        );
        assertThatNoException().isThrownBy(() -> reservationH2Repository.save(reservation));
    }

    @Test
    @DisplayName("Reservation을 제거한다.")
    void delete() {
        reservationH2Repository.delete(1L);

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
