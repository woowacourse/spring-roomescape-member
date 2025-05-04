package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class JdbcReservationDaoTest {

    @Autowired
    ReservationDAO reservationDAO;

    @DisplayName("데이터 베이스에 예약을 추가하고 id 값을 반환한다")
    @Test
    void insertTest1() {
        // given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        RoomTheme roomTheme = new RoomTheme(1L, "예시 1", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        Reservation reservation = new Reservation("검프", LocalDate.now().plusDays(1), reservationTime, roomTheme);

        // when
        long result = reservationDAO.insert(reservation);

        // then
        assertThat(result).isEqualTo(2L);
    }

    @DisplayName("같은 예약이 존재하면 true를 반환한다")
    @Test
    void existsSameReservationTest1() {
        // when
        boolean result = reservationDAO.existSameReservation(LocalDate.of(2025, 4, 28), 1L, 1L);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("존재하는 모든 예약을 찾아 반환한다")
    @Test
    void findAllTest1() {
        // when
        List<Reservation> result = reservationDAO.findAll();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(1L);
        assertThat(result.getFirst().getDate()).isEqualTo(LocalDate.of(2025, 4, 28));
        assertThat(result.getFirst().getTime()).isEqualTo(new ReservationTime(1L, LocalTime.of(10, 0)));
    }

    @DisplayName("id에 해당하는 예약을 반환한다")
    @Test
    void findByIdTest1() {
        // given
        long id = 1L;

        // when
        Optional<Reservation> resultOptional = reservationDAO.findById(id);

        // then
        assertThat(resultOptional).isPresent();
        assertThat(resultOptional.get().getId()).isEqualTo(id);
    }

    @DisplayName("주어진 timeId를 활용하는 예약이 있으면 true를 반환한다")
    @Test
    void existsByTimeIdTest1() {
        // given
        long timeId = 1L;

        // when
        boolean result = reservationDAO.existsByTimeId(timeId);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("주어진 themeId를 활용하는 예약이 있으면 true를 반환한다")
    @Test
    void existsByThemeIdTest1() {
        // given
        long themeId = 1L;

        // when
        boolean result = reservationDAO.existsByThemeId(themeId);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("주어진 id에 해당하는 예약을 삭제한다")
    @Test
    void deleteByIdTest1() {
        // given
        long id = 1L;

        // when
        boolean result = reservationDAO.deleteById(id);

        // then
        assertThat(result).isTrue();
    }
}
