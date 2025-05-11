package roomescape.reservation.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class JdbcReservationDaoTest {

    @Autowired
    private ReservationDao reservationDAO;

    @DisplayName("데이터 베이스에 예약을 추가하고 id 값을 반환한다")
    @Test
    void insertTest() {
        // given
        final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        final Theme theme = new Theme(1L, "예시 1", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        final Member member = Member.of(2L, "USER", "사용자", "user@email.com", "password");
        final Reservation reservation = new Reservation(LocalDate.now().plusDays(1), reservationTime, theme, member);

        // when
        final long result = reservationDAO.insert(reservation);

        // then
        assertThat(result).isEqualTo(4L);
    }

    @DisplayName("같은 예약이 존재하면 true를 반환한다")
    @Test
    void existsSameReservationTest() {
        // given // when
        final boolean result = reservationDAO.existSameReservation(LocalDate.of(2025, 4, 28), 1L, 1L);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("존재하는 모든 예약을 찾아 반환한다")
    @Test
    void findAllTest() {
        // given // when
        final List<Reservation> results = reservationDAO.findAll();

        // then
        Assertions.assertNotNull(results);
        assertAll(
                () -> assertThat(results).hasSize(3),
                () -> assertThat(results.getFirst().getDate()).isEqualTo("2025-04-26"),
                () -> assertThat(results.get(1).getDate()).isEqualTo("2025-04-27"),
                () -> assertThat(results.getLast().getDate()).isEqualTo("2025-04-28")
        );
    }

    @DisplayName("id에 해당하는 예약을 반환한다")
    @Test
    void findByIdTest() {
        // given
        final long id = 1L;

        // when
        final Optional<Reservation> resultOptional = reservationDAO.findById(id);

        // then
        assertThat(resultOptional).isPresent();
        assertThat(resultOptional.get().getId()).isEqualTo(id);
    }

    @DisplayName("주어진 timeId를 활용하는 예약이 있으면 true를 반환한다")
    @Test
    void existsByTimeIdTest() {
        // given
        final long timeId = 1L;

        // when
        final boolean result = reservationDAO.existsByTimeId(timeId);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("주어진 themeId를 활용하는 예약이 있으면 true를 반환한다")
    @Test
    void existsByThemeIdTest() {
        // given
        final long themeId = 1L;

        // when
        final boolean result = reservationDAO.existsByThemeId(themeId);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("주어진 id에 해당하는 예약을 삭제한다")
    @Test
    void deleteByIdTest() {
        // given
        final long id = 1L;

        // when
        final boolean result = reservationDAO.deleteById(id);

        // then
        assertThat(result).isTrue();
    }
}
