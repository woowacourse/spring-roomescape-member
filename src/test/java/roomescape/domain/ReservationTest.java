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

    @DisplayName("비어있는 멤버로는 예약을 생성할 수 없다")
    @Test
    void cannotCreateReservationWithNullMember() {
        // when & then
        assertThatThrownBy(
                () -> new Reservation(1L, null, LocalDate.now(), EXAMPLE_RESERVATION_TIME, EXAMPLE_THEME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 비어있는 멤버로는 예약을 생성할 수 없습니다.");
    }

    @DisplayName("비어있는 예약날짜로는 예약을 생성할 수 없다")
    @Test
    void cannotCreateReservationWithNullDate() {
        // given
        Member member = new Member(1L, "회원", "test@test.com", "qweqw123!", MemberRole.GENERAL);
        LocalDate nullDate = null;

        // when & then
        assertThatThrownBy(() -> new Reservation(1L, member, nullDate, EXAMPLE_RESERVATION_TIME, EXAMPLE_THEME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 비어있는 예약날짜로 예약을 생성할 수 없습니다.");
    }

    @DisplayName("비어있는 예약시간으로는 예약을 생성할 수 없다")
    @Test
    void cannotCreateReservationWithNullTime() {
        // given
        Member member = new Member(1L, "회원", "test@test.com", "qweqw123!", MemberRole.GENERAL);
        ReservationTime nullTime = null;

        // when & then
        assertThatThrownBy(() -> new Reservation(1L, member, LocalDate.now(), nullTime, EXAMPLE_THEME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 비어있는 예약시간으로는 예약을 생성할 수 없습니다.");
    }

    @DisplayName("비어있는 테마로는 예약을 생성할 수 없다")
    @Test
    void cannotCreateReservationWithNullTheme() {
        // given
        Member member = new Member(1L, "회원", "test@test.com", "qweqw123!", MemberRole.GENERAL);
        Theme nullTheme = null;

        // when & then
        assertThatThrownBy(
                () -> new Reservation(1L, member, LocalDate.now(), EXAMPLE_RESERVATION_TIME, nullTheme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 비어있는 테마로는 예약을 생성할 수 없습니다.");
    }

    @DisplayName("과거 예약인지 검증할 수 있다")
    @Test
    void canValidateFieldPastReservation() {
        // given
        Member member = new Member(1L, "회원", "test@test.com", "qweqw123!", MemberRole.GENERAL);
        ReservationTime pastReservationTime = new ReservationTime(1L, LocalTime.now().minusSeconds(1));
        Reservation pastReservation = new Reservation(1L, member, LocalDate.now(), pastReservationTime, EXAMPLE_THEME);

        // when & then
        assertThatThrownBy(pastReservation::validatePastDateTime)
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 이미 과거의 날짜와 시간입니다.");
    }
}
