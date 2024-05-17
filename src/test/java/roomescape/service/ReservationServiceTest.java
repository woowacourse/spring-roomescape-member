package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.domain.*;
import roomescape.exceptions.InvalidReservationTimeException;
import roomescape.dto.request.ReservationRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationServiceTest {

    @Autowired
    ReservationService reservationService;

    @Autowired
    ReservationTimeRepository reservationTimeRepository;

    @Autowired
    ThemeRepository themeRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        reservationTimeRepository.insert(new ReservationTime(LocalTime.now().plusHours(1)));
        themeRepository.insert(new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        member = new Member("잉크", "asdf@a.com", "1234", Role.ADMIN);
    }

    @Test
    @DisplayName("예약을 저장할 수 있다.")
    void save() {
        Reservation savedReservation = reservationService.save(member, new ReservationRequest(LocalDate.now(), 1L, 1L));

        assertThat(savedReservation).isNotNull();
    }

    @Test
    @Disabled
    @DisplayName("전체 예약을 조회할 수 있다.")
    void findAll() {
        reservationService.save(member, new ReservationRequest(LocalDate.now(), 1L, 1L));

        List<Reservation> reservations = reservationService.findAll();

        assertThat(reservations).hasSize(1);
    }

    @Test
    @DisplayName("예약을 삭제할 수 있다.")
    void delete() {
        reservationService.save(member, new ReservationRequest(LocalDate.now(), 1L, 1L));

        reservationService.delete(1L);
        List<Reservation> reservations = reservationService.findAll();

        assertThat(reservations).hasSize(0);
    }

    @Test
    @Disabled
    @DisplayName("예약 날짜와 예약 시간이 중복되면 예외가 발생한다.")
    void invalidSave() {
        LocalDate date = LocalDate.now();
        reservationService.save(member, new ReservationRequest(LocalDate.now(), 1L, 1L));

        assertThatThrownBy(() -> reservationService.save(member, new ReservationRequest(LocalDate.now(), 1L, 1L)))
                .isInstanceOf(InvalidReservationTimeException.class);
    }

    @Test
    @DisplayName("예약 날짜가 현재 날짜보다 이전이라면 예외가 발생한다.")
    void invalidDate() {
        assertThatThrownBy(() -> reservationService.save(member, new ReservationRequest(LocalDate.now().minusDays(1), 1L, 1L)))
                .isInstanceOf(InvalidReservationTimeException.class);
    }
}
