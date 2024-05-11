package roomescape.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationDAOTest {

    @Autowired
    ReservationDAO reservationDAO;
    @Autowired
    ReservationTimeDAO reservationTimeDAO;
    @Autowired
    ThemeDAO themeDAO;

    Reservation reservation;

    @BeforeEach
    void setUp() {
        ReservationTime savedReservationTime = reservationTimeDAO.insert(new ReservationTime(LocalTime.now()));
        Theme theme = themeDAO.insert(new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        Member member = new Member("잉크", "asdf@a.com", "1234");

        reservation = new Reservation(member, LocalDate.now(), savedReservationTime, theme);
    }

    @Test
    @DisplayName("예약을 추가한다.")
    void insert() {
        Reservation savedReservation = reservationDAO.insert(reservation);

        assertThat(savedReservation.getMember().getName()).isEqualTo("잉크");
    }

    @Test
    @Disabled
    @DisplayName("전체 예약을 조회한다.")
    void selectAll() {
        reservationDAO.insert(reservation);

        List<Reservation> reservations = reservationDAO.selectAll();

        assertThat(reservations).hasSize(1);
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void deleteReservation() {
        reservationDAO.insert(reservation);

        reservationDAO.deleteById(1L);
        List<Reservation> reservations = reservationDAO.selectAll();

        assertThat(reservations).hasSize(0);
    }
}
