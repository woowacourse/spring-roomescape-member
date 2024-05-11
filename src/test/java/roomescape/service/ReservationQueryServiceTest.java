package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.iterable.ThrowingExtractor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.service.request.ReservationQueryRequest;
import roomescape.service.response.ReservationResponse;
import roomescape.support.IntegrationTestSupport;

class ReservationQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private ReservationQueryService target;

    @Test
    @DisplayName("전체 예약을 불러온다.")
    void findAll() {
        ReservationQueryRequest request =
                new ReservationQueryRequest(null, null, null, null);

        List<ReservationResponse> result = target.getReservations(request);

        assertThat(result).hasSize(3);
    }

    @Test
    @DisplayName("특정 memberId의 예약을 불러온다.")
    void findAllByMemberId() {
        ReservationQueryRequest request =
                new ReservationQueryRequest(null, 1L, null, null);

        List<ReservationResponse> result = target.getReservations(request);

        assertThat(result)
                .extracting(reservationResponse -> reservationResponse.member().id())
                .allMatch(id -> id == 1L);
    }

    @Test
    @DisplayName("특정 themeId의 예약을 불러온다.")
    void findAllByThemeId() {
        ReservationQueryRequest request =
                new ReservationQueryRequest(1L, null, null, null);

        List<ReservationResponse> result = target.getReservations(request);

        assertThat(result)
                .extracting(reservationResponse -> reservationResponse.theme().id())
                .allMatch(id -> id == 1L);
    }

    @Test
    @DisplayName("시작 조건 날짜가 종료 조건 날짜보다 미래일 수 없다.")
    void checkPeriod() {
        String fromDate = "2023-05-04";
        String endDate = "2023-05-01";
        ReservationQueryRequest request =
                new ReservationQueryRequest(null, null, fromDate, endDate);

        assertThatThrownBy(() -> target.getReservations(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시작 조건 날짜는 종료 조건 날짜보다 미래일 수 없습니다.");
    }

    @Test
    @DisplayName("특정 기간의 예약을 불러온다.")
    void findAllByPeriod() {
        String fromDateString = "2023-05-01";
        String endDateString = "2023-05-04";
        ReservationQueryRequest request =
                new ReservationQueryRequest(null, null, fromDateString, endDateString);

        List<ReservationResponse> result = target.getReservations(request);

        ThrowingExtractor<String, Boolean, RuntimeException> mapper = (dateString) -> {
            LocalDate date = LocalDate.parse(dateString);
            LocalDate fromDate = LocalDate.parse(fromDateString);
            LocalDate endDate = LocalDate.parse(endDateString);

            return isDateInRange(date, fromDate, endDate);
        };
        assertThat(result)
                .hasSize(2)
                .extracting(ReservationResponse::date)
                .map(mapper)
                .containsExactly(true, true);
    }

    private boolean isDateInRange(LocalDate date, LocalDate fromDate, LocalDate endDate) {
        return date.isAfter(fromDate) && (date.isBefore(endDate) || date.isEqual(endDate));
    }
}
