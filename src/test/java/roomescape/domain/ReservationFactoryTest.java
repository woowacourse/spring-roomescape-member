package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationFactoryTest {

    private static final Clock clock;

    static {
        Instant instant = Instant.parse("2024-05-01T00:30:00.00Z");
        clock = Clock.fixed(instant, ZoneId.of("UTC"));
    }

    @DisplayName("생성 테스트")
    void create() {
        assertThatCode(() -> new ReservationFactory(clock))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("신규 예약을 생성할 수 있다.")
    void reserve() {
        ReservationFactory reservationFactory = new ReservationFactory(clock);
        String date = "2024-05-02";
        Member member = new Member(
                new Name("아톰"),
                new Email("email"),
                new Password("******"), MemberRole.NORMAL
        );
        ReservationTime time = new ReservationTime(LocalTime.parse("12:00"));
        Theme theme = new Theme("name", "description", "thumbnail");

        assertThatCode(() -> reservationFactory.create(date, member, time, theme))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("예약 날짜가 과거라면 신규 예약을 생성할 수 없다.")
    void previousDate() {
        ReservationFactory reservationFactory = new ReservationFactory(clock);
        String previousDate = "2024-04-30";
        Member member = new Member(
                new Name("아톰"),
                new Email("email"),
                new Password("******"), MemberRole.NORMAL
        );
        ReservationTime time = new ReservationTime(LocalTime.parse("12:00"));
        Theme theme = new Theme("name", "description", "thumbnail");

        assertThatThrownBy(() -> reservationFactory.create(previousDate, member, time, theme))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 지나간 시간에 대한 예약을 할 수 없습니다.");
    }

    @Test
    @DisplayName("같은 날짜이지만, 예약 시간이 현재 시간보다 이전인 경우, 예약을 생성할 수 없다.")
    void previousTime() {
        ReservationFactory reservationFactory = new ReservationFactory(clock);
        String todayDate = "2024-05-01";
        Member member = new Member(
                new Name("아톰"),
                new Email("email"),
                new Password("******"), MemberRole.NORMAL
        );
        ReservationTime previousTime = new ReservationTime(LocalTime.parse("00:29"));
        Theme theme = new Theme("name", "description", "thumbnail");

        assertThatThrownBy(() -> reservationFactory.create(todayDate, member, previousTime, theme))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 지나간 시간에 대한 예약을 할 수 없습니다.");
    }
}
