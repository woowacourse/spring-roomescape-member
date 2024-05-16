package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dao.MemberDAO;
import roomescape.dao.ReservationTimeDAO;
import roomescape.dao.ThemeDAO;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationServiceTest {

    @Autowired
    ReservationService reservationService;
    @Autowired
    ReservationTimeDAO reservationTimeDAO;
    @Autowired
    ThemeDAO themeDAO;
    @Autowired
    MemberDAO memberDAO;

    @BeforeEach
    void setUp() {
        reservationTimeDAO.insert(new ReservationTime(LocalTime.now().plusHours(1)));
        themeDAO.insert(new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        memberDAO.insert(new Member("뽀로로", "email.com", "1234"));
    }

    @Test
    @DisplayName("예약을 저장할 수 있다.")
    void save() {
        final Reservation savedReservation = reservationService.save(new ReservationRequest(LocalDate.now().plusDays(1), 1L, 1L, 1L));

        assertThat(savedReservation).isNotNull();
    }

    @Test
    @DisplayName("전체 예약을 조회할 수 있다.")
    void findAll() {
        reservationService.save(new ReservationRequest(LocalDate.now().plusDays(1), 1L, 1L, 1L));

        final List<Reservation> reservations = reservationService.findAll();

        assertThat(reservations).hasSize(1);
    }

    @Test
    @DisplayName("예약을 삭제할 수 있다.")
    void delete() {
        reservationService.save(new ReservationRequest(LocalDate.now().plusDays(1), 1L, 1L, 1L));

        reservationService.delete(1L);
        final List<Reservation> reservations = reservationService.findAll();

        assertThat(reservations).hasSize(0);
    }

    @Test
    @DisplayName("예약 날짜와 예약 시간이 중복되면 예외가 발생한다.")
    void invalidSave() {
        LocalDate date = LocalDate.now().plusDays(1);
        reservationService.save(new ReservationRequest(date, 1L, 1L, 1L));

        assertThatThrownBy(() -> reservationService.save(new ReservationRequest(date, 1L, 1L, 1L)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("해당 날짜와 시간에 예약이 이미 존재합니다.");
    }

    @Test
    @DisplayName("예약 날짜가 현재 날짜보다 이전이라면 예외가 발생한다.")
    void invalidDate() {
        assertThatThrownBy(() -> reservationService.save(new ReservationRequest(LocalDate.of(2024, 3, 30), 1L, 1L, 1L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("지나간 날짜에 예약을 등록할 수 없습니다.");
    }

    @Test
    @DisplayName("예약 날짜는 같지만 시간이 현재 시간보다 이전이라면 예외가 발생한다.")
    void invalidTime() {
        final ReservationTime savedTime = reservationTimeDAO.insert(new ReservationTime(LocalTime.now().minusMinutes(1)));

        assertThatThrownBy(() -> reservationService.save(new ReservationRequest(LocalDate.now(), savedTime.getId(), 1L, 1L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("지나간 시간에 예약을 등록할 수 없습니다.");
    }
}
