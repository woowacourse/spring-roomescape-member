package roomescape.model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ReservationTest {

    @Test
    @DisplayName("예약 생성 시 사용자, 시간, 테마가 null이면 예외가 발생한다")
    void createWithNullArguments() {
        // given
        ReservationDateTime dateTime = new ReservationDateTime(
                LocalDate.now().plusDays(1),
                new ReservationTime(1L, LocalTime.of(10, 0))
        );
        Theme theme = new Theme(2L, "테마", "desc", "thumb");
        Member member = new Member(3L, Role.valueOf("USER"), new MemberName("vector"), "abc", "def");
        // when & then
        assertThatThrownBy(() -> Reservation.create(null, null, dateTime, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 생성 시 사용자, 시간, 테마는 필수입니다.");
        assertThatThrownBy(() -> Reservation.create(null, member, null, theme))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Reservation.create(null, member, dateTime, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("예약 생성 시 과거 날짜면 예외가 발생한다")
    void createWithPastDate() {
        // given
        ReservationDateTime pastDateTime = new ReservationDateTime(
                LocalDate.now().minusDays(1),
                new ReservationTime(1L, LocalTime.of(10, 0))
        );
        Member member = new Member(3L, Role.valueOf("USER"), new MemberName("vector"), "abc", "def");
        Theme theme = new Theme(2L, "테마", "desc", "thumb");

        // when & then
        assertThatThrownBy(() -> Reservation.create(null, member, pastDateTime, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("과거 예약은 불가능합니다.");
    }

    @Test
    @DisplayName("예약 생성 시 정상 데이터면 예외가 발생하지 않는다")
    void createWithValidArguments() {
        // given
        ReservationDateTime futureDateTime = new ReservationDateTime(
                LocalDate.now().plusDays(1),
                new ReservationTime(1L, LocalTime.of(10, 0))
        );
        Member member = new Member(1L, Role.valueOf("USER"), new MemberName("vector"), "abc", "def");
        Theme theme = new Theme(2L, "테마", "desc", "thumb");

        // when & then
        assertThatCode(() -> Reservation.create(null, member, futureDateTime, theme))
                .doesNotThrowAnyException();
    }
}