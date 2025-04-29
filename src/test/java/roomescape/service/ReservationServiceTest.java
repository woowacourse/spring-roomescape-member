package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.dao.FakeReservationDao;
import roomescape.dto.ReservationRequestDto;
import roomescape.dto.ReservationResponseDto;

class ReservationServiceTest {

    private ReservationService reservationService;
    private FakeReservationDao fakeReservationDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        fakeReservationDao = new FakeReservationDao(jdbcTemplate);
        reservationService = new ReservationService(fakeReservationDao);
    }

    @DisplayName("예약을 저장한다")
    @Test
    void test() {
        // given
        ReservationRequestDto request = new ReservationRequestDto("다로", LocalDate.now().plusDays(1), 1L);

        // when
        ReservationResponseDto response = reservationService.saveReservation(request);

        // then
        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo("다로");
        assertThat(response.date()).isEqualTo(LocalDate.now().plusDays(1));
        assertThat(response.time().id()).isEqualTo(1L);
    }

    @DisplayName("모든 예약을 조회한다")
    @Test
    void test1() {
        // given
        ReservationRequestDto request1 = new ReservationRequestDto("다로", LocalDate.now().plusDays(1), 1L);
        ReservationRequestDto request2 = new ReservationRequestDto("에러", LocalDate.now().plusDays(2), 2L);
        reservationService.saveReservation(request1);
        reservationService.saveReservation(request2);

        // when
        List<ReservationResponseDto> reservations = reservationService.getAllReservations();

        // then
        assertThat(reservations).hasSize(2);
        assertThat(reservations).extracting("name")
                .containsExactlyInAnyOrder("다로", "에러");
    }

    @DisplayName("예약을 취소한다")
    @Test
    void test2() {
        // given
        ReservationRequestDto request = new ReservationRequestDto("다로", LocalDate.now().plusDays(1), 1L);
        ReservationResponseDto saved = reservationService.saveReservation(request);

        // when
        reservationService.cancelReservation(saved.id());

        // then
        List<ReservationResponseDto> reservations = reservationService.getAllReservations();
        assertThat(reservations).isEmpty();
    }
}