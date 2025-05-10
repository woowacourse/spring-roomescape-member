package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.map;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import roomescape.common.exception.DeleteReservationException;
import roomescape.common.exception.NotAbleDeleteException;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.*;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.search.ReservationTimeResponseWithBookedStatus;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ReservationTimeServiceTest {

    private final ReservationService reservationService;
    private final ThemeService themeService;
    private final MemberService memberService;
    private final ReservationTimeService reservationTimeService;

    @Autowired
    public ReservationTimeServiceTest(ReservationService reservationService, ThemeService themeService, MemberService memberService, ReservationTimeService reservationTimeService) {
        this.reservationService = reservationService;
        this.themeService = themeService;
        this.memberService = memberService;
        this.reservationTimeService = reservationTimeService;
    }

    @Test
    void createReservationTimeTest() {
        // given
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(LocalTime.of(10,0));

        // when
        reservationTimeService.createReservationTime(request);

        // then
        List<ReservationTimeResponse> reservationTimes = reservationTimeService.findAll();
        assertAll(
                () -> assertThat(reservationTimes).hasSize(1),
                () -> assertThat(reservationTimes.get(0).startAt()).isEqualTo("10:00")
        );
    }

    @Test
    void findAllTest() {
        // given
        ReservationTimeCreateRequest request1 = new ReservationTimeCreateRequest(LocalTime.of(10,0));
        ReservationTimeCreateRequest request2 = new ReservationTimeCreateRequest(LocalTime.of(11,0));
        reservationTimeService.createReservationTime(request1);
        reservationTimeService.createReservationTime(request2);

        // when
        List<ReservationTimeResponse> reservationTimes = reservationTimeService.findAll();

        // then
        assertAll(
                () -> assertThat(reservationTimes).hasSize(2),
                () -> assertThat(reservationTimes.get(0).startAt()).isEqualTo("10:00"),
                () -> assertThat(reservationTimes.get(1).startAt()).isEqualTo("11:00")
        );
    }

    @Test
    void deleteReservationTimeByIdTest() {
        // given
        ReservationTimeCreateRequest reservationTimeCreateRequest = new ReservationTimeCreateRequest(LocalTime.of(10,0));
        reservationTimeService.createReservationTime(reservationTimeCreateRequest);
        List<ReservationTimeResponse> reservationTimesBeforeDelete = reservationTimeService.findAll();

        // when
        reservationTimeService.deleteReservationTimeById(reservationTimesBeforeDelete.get(0).id());
        List<ReservationTimeResponse> reservationTimesAfterDelete = reservationTimeService.findAll();

        // then
        assertAll(
                () -> assertThat(reservationTimesBeforeDelete).hasSize(1),
                () -> assertThat(reservationTimesAfterDelete).isEmpty()
        );
    }

    @Test
    void deleteReservationTimeWhenInUseTest() {
        // given
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(LocalTime.of(10,0));
        reservationTimeService.createReservationTime(request);
        ThemeCreateRequest themeCreateRequest = new ThemeCreateRequest("테마1","테마 설명", "테마 사진");
        themeService.createTheme(themeCreateRequest);
        memberService.signup(new MemberSignUpRequest("히포", "test@test.com", "1234"));
        reservationService.createReservation(new ReservationCreateRequest(LocalDate.now().plusDays(1),1L, 1L), 1L);
        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTimeById(1L)).isInstanceOf(DeleteReservationException.class);
    }

    @Test
    void findAvailableReservationTimesByDateAndThemeIdTest() {
        // given
        ReservationTimeCreateRequest request1 = new ReservationTimeCreateRequest(LocalTime.of(10,0));
        ReservationTimeCreateRequest request2 = new ReservationTimeCreateRequest(LocalTime.of(11,0));
        reservationTimeService.createReservationTime(request1);
        reservationTimeService.createReservationTime(request2);

        // when
        List<ReservationTimeResponseWithBookedStatus> availableTimes = reservationTimeService.findAvailableReservationTimesByDateAndThemeId(LocalDate.now(), 1L);

        // then
        assertAll(
                () -> assertThat(availableTimes).hasSize(2),
                () -> assertThat(availableTimes.get(0).booked()).isFalse(),
                () -> assertThat(availableTimes.get(1).booked()).isFalse()
        );
    }

}

