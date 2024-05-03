package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import roomescape.controller.reservation.ReservationRequest;
import roomescape.controller.reservation.ReservationResponse;
import roomescape.controller.theme.ReservationThemeResponse;
import roomescape.controller.time.TimeResponse;
import roomescape.repository.H2ReservationRepository;
import roomescape.repository.H2ReservationTimeRepository;
import roomescape.repository.H2ThemeRepository;
import roomescape.service.exception.DuplicateReservation;
import roomescape.service.exception.PreviousTimeException;
import roomescape.service.exception.TimeNotFoundException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Sql(scripts = {"/drop.sql", "/schema.sql", "/data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@JdbcTest
@Import({ReservationService.class, H2ReservationRepository.class, H2ReservationTimeRepository.class, H2ThemeRepository.class})
class ReservationServiceTest {

    final long LAST_ID = 12;
    final ReservationResponse exampleFirstResponse = new ReservationResponse(
            1L,
            "Seyang", LocalDate.now().minusDays(8).format(DateTimeFormatter.ISO_LOCAL_DATE),
            new TimeResponse(1L, "10:15", false),
            new ReservationThemeResponse("Spring"));

    @Autowired
    private ReservationService reservationService;

    @Test
    @DisplayName("예약 목록을 조회한다.")
    void getReservations() {
        // given & when
        List<ReservationResponse> actual = reservationService.getReservations();

        // then
        assertThat(actual).hasSize((int) LAST_ID);
        assertThat(actual.get(0)).isEqualTo(exampleFirstResponse);
    }

    @Test
    @DisplayName("예약을 추가한다.")
    void addReservation() {
        // given
        ReservationRequest request = new ReservationRequest(
                "cha",
                LocalDate.now().plusMonths(1).format(DateTimeFormatter.ISO_LOCAL_DATE),
                3L,
                2L
        );

        // when
        ReservationResponse actual = reservationService.addReservation(request);

        // then
        assertThat(actual.id()).isPositive();
    }

    @Test
    @DisplayName("존재하는 예약을 삭제한다.")
    void deleteReservationPresent() {
        // given & when & then
        assertThat(reservationService.deleteReservation(LAST_ID)).isOne();
    }

    @Test
    @DisplayName("존재하지 않는 예약을 삭제하면 아무일도 일어나지 않는다.")
    void deleteReservationNotPresent() {
        // given & when & then
        assertThat(reservationService.deleteReservation(LAST_ID + 1)).isZero();
    }

    @Test
    @DisplayName("존재하지 않는 시간(id)로 예약을 할 때 예외가 발생한다.")
    void addReservationNonExistTime() {
        ReservationRequest request = new ReservationRequest("redddy", "2024-06-21", 100L, 2L);
        assertThatThrownBy(() -> reservationService.addReservation(request))
                .isInstanceOf(TimeNotFoundException.class);
    }

    @Test
    @DisplayName("예약하려는 시간이 현재 시간보다 이전일 경우 예외가 발생한다.")
    void validateReservationTimeAfterThanNow() {
        assertThatThrownBy(() -> reservationService.addReservation(new ReservationRequest("cha", "2024-04-30", 1L, 1L)))
                .isInstanceOf(PreviousTimeException.class);
    }

    @Test
    @DisplayName("중복된 시간으로 예약을 할 때 예외가 발생한다.")
    void duplicateDateTimeReservation() {
        //given
        final ReservationRequest request = new ReservationRequest("레디", "2025-11-20", 1L, 1L);

        //when
        reservationService.addReservation(request);

        //then
        assertThatThrownBy(() -> reservationService.addReservation(request))
                .isInstanceOf(DuplicateReservation.class);
    }
}
