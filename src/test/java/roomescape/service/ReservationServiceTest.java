package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.Fixtures.JUNK_THEME;
import static roomescape.Fixtures.JUNK_TIME_SLOT;
import static roomescape.Fixtures.getReservationOfYesterday;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.Fixtures;
import roomescape.controller.reservation.dto.AddReservationRequest;
import roomescape.controller.reservation.dto.ReservationResponse;
import roomescape.repository.fake.ReservationFakeRepository;
import roomescape.repository.fake.ThemeFakeRepository;
import roomescape.repository.fake.TimeSlotFakeRepository;

public class ReservationServiceTest {

    private ReservationService service;

    @BeforeEach
    void setUp() {
        var reservationRepository = new ReservationFakeRepository();
        var timeSlotRepository = new TimeSlotFakeRepository();
        var themeRepository = new ThemeFakeRepository();
        timeSlotRepository.save(JUNK_TIME_SLOT);
        themeRepository.save(JUNK_THEME);

        service = new ReservationService(reservationRepository, timeSlotRepository, themeRepository);
    }

    @Test
    @DisplayName("예약을 추가할 수 있다.")
    void add() {
        // given
        var request = Fixtures.getAddReservationRequestOfTomorrow();

        // when
        ReservationResponse response = service.add(request);

        // then
        var reservations = service.findAll();
        assertThat(reservations).contains(response);
    }

    @Test
    @DisplayName("예약을 삭제할 수 있다.")
    void deleteReservation() {
        // given
        var request = Fixtures.getAddReservationRequestOfTomorrow();
        var response = service.add(request);

        // when
        boolean isRemoved = service.removeById(response.id());

        // then
        var reservations = service.findAll();
        assertAll(
            () -> assertThat(isRemoved).isTrue(),
            () -> assertThat(reservations).doesNotContain(response)
        );
    }

    @Test
    @DisplayName("지나가지 않은 날짜와 시간에 대한 예약 생성은 가능하다.")
    void canAddFutureDateTime() {
        // given
        var request = Fixtures.getAddReservationRequestOfTomorrow();

        // when & then
        assertThatCode(() -> service.add(request))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("이미 예약된 날짜와 시간에 대한 예약 생성은 불가능하다.")
    void cannotAddIdenticalDateTimeMultipleTimes() {
        // given
        var request = Fixtures.getAddReservationRequestOfTomorrow();

        // when
        service.add(request);

        // then
        assertThatThrownBy(() -> service.add(request))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
