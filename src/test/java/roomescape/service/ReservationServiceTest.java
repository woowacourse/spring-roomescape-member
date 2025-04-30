package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.Fixtures.JUNK_THEME;
import static roomescape.Fixtures.JUNK_TIME_SLOT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.Fixtures;
import roomescape.model.Reservation;
import roomescape.repository.ReservationFakeRepository;
import roomescape.repository.ThemeFakeRepository;
import roomescape.repository.TimeSlotFakeRepository;

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
    void reserve() {
        // given
        var name = "포포";
        var date = Fixtures.ofTomorrow();
        var timeSlotId = JUNK_TIME_SLOT.id();
        var themeId = JUNK_THEME.id();

        // when
        Reservation reserved = service.reserve(name, date, timeSlotId, themeId);

        // then
        var reservations = service.allReservations();
        assertThat(reservations).contains(reserved);
    }

    @Test
    @DisplayName("예약을 삭제할 수 있다.")
    void deleteReservation() {
        // given
        var name = "포포";
        var date = Fixtures.ofTomorrow();
        var timeSlotId = JUNK_TIME_SLOT.id();
        var themeId = JUNK_THEME.id();
        var reserved = service.reserve(name, date, timeSlotId, themeId);

        // when
        boolean isRemoved = service.removeById(reserved.id());

        // then
        var reservations = service.allReservations();
        assertAll(
            () -> assertThat(isRemoved).isTrue(),
            () -> assertThat(reservations).doesNotContain(reserved)
        );
    }

    @Test
    @DisplayName("지나간 날짜와 시간에 대한 예약 생성은 불가능하다.")
    void cannotReservePastDateTime() {
        // given
        var name = "포포";
        var date = Fixtures.ofYesterday();
        var timeSlotId = JUNK_TIME_SLOT.id();
        var themeId = JUNK_THEME.id();

        // when & then
        assertThatThrownBy(() -> service.reserve(name, date, timeSlotId, themeId))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("지나가지 않은 날짜와 시간에 대한 예약 생성은 가능하다.")
    void canReserveFutureDateTime() {
        // given
        var name = "포포";
        var date = Fixtures.ofTomorrow();
        var timeSlotId = JUNK_TIME_SLOT.id();
        var themeId = JUNK_THEME.id();

        // when & then
        assertThatCode(() -> service.reserve(name, date, timeSlotId, themeId))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("이미 예약된 날짜와 시간에 대한 예약 생성은 불가능하다.")
    void cannotReserveIdenticalDateTimeMultipleTimes() {
        // given
        var name = "포포";
        var date = Fixtures.ofTomorrow();
        var timeSlotId = JUNK_TIME_SLOT.id();
        var themeId = JUNK_THEME.id();

        // when
        service.reserve(name, date, timeSlotId, themeId);

        // then
        assertThatThrownBy(() -> service.reserve(name, date, timeSlotId, themeId))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
