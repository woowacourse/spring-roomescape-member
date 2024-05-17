package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.member.Member;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.ThemeResponse;

@Sql("/reservation-service-test-data.sql")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReservationServiceTest {

    @Autowired
    ReservationService reservationService;

    @Autowired
    ReservationTimeService reservationTimeService;

    @Autowired
    ThemeService themeService;


    @Test
    void 잘못된_예약_시간대_id로_예약을_추가할_경우_예외_발생() {
        //given
        List<ReservationTimeResponse> allReservationTimes = reservationTimeService.getAllReservationTimes();
        Long notExistTimeId = allReservationTimes.size() + 1L;

        ReservationRequest reservationRequest = new ReservationRequest(LocalDate.now(), notExistTimeId, 1L);

        //when, then
        assertThatThrownBy(() -> reservationService.addReservation(getMember(), reservationRequest)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    void 잘못된_테마_id로_예약을_추가할_경우_예외_발생() {
        //given
        List<ThemeResponse> allTheme = themeService.getAllTheme();
        Long notExistIdToFind = allTheme.size() + 1L;

        ReservationRequest reservationRequest = new ReservationRequest(LocalDate.now(), 1L, notExistIdToFind);

        //when, then
        assertThatThrownBy(() -> reservationService.addReservation(getMember(), reservationRequest)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    void 날짜와_시간대와_테마가_모두_동일한_예약을_추가할_경우_예외_발생() {
        //given
        ReservationRequest reservationRequest1 = new ReservationRequest(LocalDate.now().plusDays(1), 1L, 1L);
        reservationService.addReservation(getMember(), reservationRequest1);

        //when, then
        ReservationRequest reservationRequest2 = new ReservationRequest(LocalDate.now().plusDays(1), 1L, 1L);
        assertThatThrownBy(() -> reservationService.addReservation(getMember(), reservationRequest2)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    void 지나간_날짜로_예약을_추가할_경우_예외_발생() {
        //given
        ReservationRequest reservationRequest = new ReservationRequest(LocalDate.now().minusDays(1), 1L, 1L);

        //when, then
        assertThatThrownBy(() -> reservationService.addReservation(getMember(), reservationRequest)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_id로_조회할_경우_예외_발생() {
        //given
        List<ReservationResponse> allReservations = reservationService.getAllReservations();
        Long notExistIdToFind = allReservations.size() + 1L;

        //when, then
        assertThatThrownBy(() -> reservationService.getReservation(notExistIdToFind)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_id로_삭제할_경우_예외_발생() {
        //given
        List<ReservationResponse> allReservations = reservationService.getAllReservations();
        Long notExistIdToFind = allReservations.size() + 1L;

        //when, then
        assertThatThrownBy(() -> reservationService.deleteReservation(notExistIdToFind)).isInstanceOf(
                IllegalArgumentException.class);
    }

    private Member getMember() {
        return new Member(1L,"admin", "admin@admin.com", "adminadmin","ADMIN");
    }
}
