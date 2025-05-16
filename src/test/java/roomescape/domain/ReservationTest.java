package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.BadRequestException;

class ReservationTest {

    private static final ReservationTime EXAMPLE_RESERVATION_TIME = new ReservationTime(1L, LocalTime.of(10, 0));
    private static final Theme EXAMPLE_THEME = new Theme(1L, "이름", "설명", "썸네일");
    private static final Member EXAMPLE_MEMBER = new Member(1L, "이름", "이메일", "비번", "ADMIN");

    @DisplayName("NULL 또는 비어있는 이름으로 예약을 생성할 수 없다")
    @Test
    void cannotCreateReservationWithBlankMember() {
        assertThatThrownBy(
                () -> new Reservation(1L, null, LocalDate.now(), EXAMPLE_RESERVATION_TIME, EXAMPLE_THEME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 비어있는 멤버로 예약을 생성할 수 없습니다.");
    }

//    @DisplayName("최대 길이를 넘는 이름으로는 예약을 생성할 수 없다")
//    @Test
//    void cannotCreateReservationWithTooLongName() {
//        String tooLongName = "i".repeat(256);
//        assertThatThrownBy(
//                () -> new Reservation(1L, tooLongName, LocalDate.now(), EXAMPLE_RESERVATION_TIME, EXAMPLE_THEME))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("[ERROR] 이름으로 입력된 문자열의 길이가 최대값(255자)을 초과했습니다.");
//    }

    @DisplayName("비어있는 ID로는 예약을 생성할 수 없다")
    @Test
    void cannotCreateReservationWithNullId() {
        Long nullId = null;
        assertThatThrownBy(
                () -> new Reservation(nullId, EXAMPLE_MEMBER, LocalDate.now(), EXAMPLE_RESERVATION_TIME, EXAMPLE_THEME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 비어있는 ID로 예약을 생성할 수 없습니다.");
    }

    @DisplayName("비어있는 예약날짜로는 예약을 생성할 수 없다")
    @Test
    void cannotCreateReservationWithNullDate() {
        LocalDate nullDate = null;
        assertThatThrownBy(() -> new Reservation(1L, EXAMPLE_MEMBER, nullDate, EXAMPLE_RESERVATION_TIME, EXAMPLE_THEME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 비어있는 예약날짜로 예약을 생성할 수 없습니다.");
    }

    @DisplayName("비어있는 예약시간으로는 예약을 생성할 수 없다")
    @Test
    void cannotCreateReservationWithNullTime() {
        ReservationTime nullTime = null;
        assertThatThrownBy(() -> new Reservation(1L, EXAMPLE_MEMBER, LocalDate.now(), nullTime, EXAMPLE_THEME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 비어있는 예약시간으로는 예약을 생성할 수 없습니다.");
    }

    @DisplayName("비어있는 테마로는 예약을 생성할 수 없다")
    @Test
    void cannotCreateReservationWithNullTheme() {
        Theme nullTheme = null;
        assertThatThrownBy(
                () -> new Reservation(1L, EXAMPLE_MEMBER, LocalDate.now(), EXAMPLE_RESERVATION_TIME, nullTheme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 비어있는 테마로는 예약을 생성할 수 없습니다.");
    }

    @DisplayName("과거 예약인지 검증할 수 있다")
    @Test
    void canValidateFieldPastReservation() {
        ReservationTime pastReservationTime = new ReservationTime(1L, LocalTime.now().minusSeconds(1));
        Reservation pastReservation = new Reservation(1L, EXAMPLE_MEMBER, LocalDate.now(), pastReservationTime,
                EXAMPLE_THEME);

        assertThatThrownBy(pastReservation::validatePastDateTime)
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 이미 과거의 날짜와 시간입니다.");
    }
}
