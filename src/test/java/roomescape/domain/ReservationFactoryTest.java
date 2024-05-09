package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationFactoryTest {

    private final Clock clock = Clock.systemDefaultZone();

    @Test
    @DisplayName("생성 테스트")
    void create() {
        assertThatCode(() -> new ReservationFactory(clock))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("신규 예약을 생성할 수 있다.")
    void reserve() {
        ReservationFactory reservationFactory = new ReservationFactory(clock);
        String name = "아톰";
        String date = LocalDate.now().plusDays(1).toString();
        ReservationTime time = new ReservationTime(LocalTime.now());
        Theme theme = new Theme("name", "description", "thumbnail");

        assertThatCode(() -> reservationFactory.create(name, date, time, theme))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("예약 날짜가 과거라면 신규 예약을 생성할 수 없다.")
    void previousDate() {
        ReservationFactory reservationFactory = new ReservationFactory(clock);
        String name = "아톰";
        String previousDate = "1999-12-23";
        ReservationTime time = new ReservationTime(LocalTime.now());
        Theme theme = new Theme("name", "description", "thumbnail");

        assertThatThrownBy(() -> reservationFactory.create(name, previousDate, time, theme))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 지나간 시간에 대한 예약을 할 수 없습니다.");
    }

    @Test
    @DisplayName("같은 날짜이지만, 예약 시간이 현재 시간보다 이전인 경우, 예약을 생성할 수 없다.")
    void previousTime() {
        ReservationFactory reservationFactory = new ReservationFactory(clock);
        String name = "아톰";
        String todayDate = LocalDate.now().toString();
        ReservationTime previousTime = new ReservationTime(LocalTime.now().minusHours(1));
        Theme theme = new Theme("name", "description", "thumbnail");

        assertThatThrownBy(() -> reservationFactory.create(name, todayDate, previousTime, theme))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 지나간 시간에 대한 예약을 할 수 없습니다.");
    }
}
