package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Reservation;
import roomescape.dto.AdminReservationRequestDto;
import roomescape.dto.FilterConditionDto;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"/test_schema.sql", "/test_data.sql"})
public class ReservationServiceTest {

    @Autowired
    private final ReservationService reservationService;

    @Autowired
    public ReservationServiceTest(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @DisplayName("모든 예약을 조회한다.")
    @Test
    void getAllReservationsTest() {
        List<Reservation> reservations = reservationService.getAllReservations();

        assertThat(reservations.size()).isGreaterThan(0);
    }

    @DisplayName("예약을 추가한다.")
    @Test
    void insertReservationTest() {
        ZoneId kst = ZoneId.of("Asia/Seoul");
        int size = reservationService.getAllReservations().size();

        AdminReservationRequestDto adminReservationRequestDto = new AdminReservationRequestDto(LocalDate.now(kst).plusDays(1), 1L, 1L, 1L);
        Reservation reservation = reservationService.insertReservation(adminReservationRequestDto);

        assertThat(reservation.getId()).isEqualTo(size + 1L);
        assertThat(reservation.getMember().getName()).isEqualTo("name");
        assertThat(reservation.getDate()).isEqualTo(adminReservationRequestDto.date().toString());
    }

    @DisplayName("예약을 삭제한다.")
    @Test
    void deleteReservationTest() {
        int sizeBeforeDelete = reservationService.getAllReservations().size();
        assertThatCode(() -> reservationService.deleteReservation(1L)).doesNotThrowAnyException();
        assertThat(reservationService.getAllReservations().size()).isEqualTo(sizeBeforeDelete - 1);
    }

    @DisplayName("현재 시간 이전의 시간으로 예약할 수 없다.")
    @Test
    void invalidDateTimeTest() {
        ZoneId kst = ZoneId.of("Asia/Seoul");
        LocalDate localDate = LocalDate.now(kst).minusDays(2);
        AdminReservationRequestDto adminReservationRequestDto = new AdminReservationRequestDto(localDate, 1L, 1L, 1L);

        assertThatThrownBy(() -> reservationService.insertReservation(adminReservationRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지나간 날짜와 시간에 대한 예약 생성은 불가능합니다.");
    }

    @DisplayName("같은 날짜, 시간, 테마에 대한 중복 예약은 불가능하다.")
    @Test
    void duplicatedReservationTest() {
        ZoneId kst = ZoneId.of("Asia/Seoul");
        AdminReservationRequestDto adminReservationRequestDto = new AdminReservationRequestDto(LocalDate.now(kst).plusDays(2), 1L, 1L, 1L);

        reservationService.insertReservation(adminReservationRequestDto);

        assertThatThrownBy(() -> reservationService.insertReservation(adminReservationRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 해당 시간에 예약이 존재합니다.");
    }

    @DisplayName("조건에 맞는 예약을 반환한다.")
    @Test
    void getFilteredReservationsTest() {
        ZoneId kst = ZoneId.of("Asia/Seoul");

        LocalDate dateFrom = LocalDate.now(kst).minusWeeks(1);
        LocalDate dateTo = LocalDate.now(kst).minusDays(1);

        FilterConditionDto filterConditionDto = new FilterConditionDto(1L, 1L, dateFrom, dateTo);

        List<Reservation> reservations = reservationService.getFilteredReservations(filterConditionDto);

        assertThat(reservations.size()).isEqualTo(2);
    }
}
