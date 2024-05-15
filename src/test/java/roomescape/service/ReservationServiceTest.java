package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.domain.ReservationTime;
import roomescape.infrastructure.persistence.ReservationTimeRepository;
import roomescape.service.request.ReservationRequest;
import roomescape.support.IntegrationTestSupport;

class ReservationServiceTest extends IntegrationTestSupport {

    @Autowired
    private ReservationService target;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    @DisplayName("신규 예약을 생성할 수 있다.")
    void createReservation() {
        LocalDate date = nextDate();
        ReservationRequest request = new ReservationRequest(
                date.toString(),
                멤버_1번_어드민_ID,
                예약_시간_1번_ID,
                테마_1번_ID
        );

        assertThatCode(() -> target.createReservation(request))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("중복된 예약은 생성할 수 없다.")
    void duplicated() {
        LocalDate date = nextDate();
        ReservationRequest request = new ReservationRequest(
                date.toString(),
                멤버_1번_어드민_ID,
                예약_시간_1번_ID,
                테마_1번_ID
        );

        target.createReservation(request);

        assertThatThrownBy(() -> target.createReservation(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("중복된 예약이 존재합니다.");
    }

    @Test
    @DisplayName("존재하지 않는 사용자에 대한 예약은 생성할 수 없다.")
    void withUnknownMember() {
        LocalDate date = nextDate();
        Long unknownMemberId = 0L;
        ReservationRequest request = new ReservationRequest(
                date.toString(),
                unknownMemberId,
                예약_시간_1번_ID,
                테마_1번_ID
        );

        assertThatThrownBy(() -> target.createReservation(request))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당되는 사용자가 없습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 테마에 대한 예약은 생성할 수 없다.")
    void withUnknownTheme() {
        LocalDate date = nextDate();
        Long unknownThemeId = 0L;
        ReservationRequest request = new ReservationRequest(
                date.toString(),
                멤버_1번_어드민_ID,
                예약_시간_1번_ID,
                unknownThemeId
        );

        assertThatThrownBy(() -> target.createReservation(request))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당되는 테마가 없습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 시간에 대한 예약은 생성할 수 없다.")
    void withUnknownTime() {
        LocalDate date = nextDate();
        Long unknownTimeId = 0L;
        ReservationRequest request = new ReservationRequest(
                date.toString(),
                멤버_1번_어드민_ID,
                unknownTimeId,
                테마_1번_ID
        );

        assertThatThrownBy(() -> target.createReservation(request))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당되는 예약 시간이 없습니다.");
    }

    @Test
    @DisplayName("지나간 날짜에 대한 예약은 생성할 수 없다.")
    void withPreviousDate() {
        LocalDate previousDate = previousDate();
        ReservationRequest request = new ReservationRequest(
                previousDate.toString(),
                멤버_1번_어드민_ID,
                예약_시간_1번_ID,
                테마_1번_ID
        );

        assertThatThrownBy(() -> target.createReservation(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 지나간 시간에 대한 예약을 할 수 없습니다.");
    }

    @Test
    @DisplayName("당일이지만, 이전 시간이면 예약을 생성할 수 없다.")
    void withPreviousTime() {
        ReservationTime newTime = new ReservationTime(previousTime());
        ReservationTime previousTime = reservationTimeRepository.save(newTime);
        LocalDate date = today();
        ReservationRequest request = new ReservationRequest(
                date.toString(),
                멤버_1번_어드민_ID,
                previousTime.getId(),
                테마_1번_ID
        );

        assertThatThrownBy(() -> target.createReservation(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 지나간 시간에 대한 예약을 할 수 없습니다.");
    }
}
