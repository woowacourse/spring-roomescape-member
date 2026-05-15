package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.DomainException;

class ReservationTest {

    private static final ReservationTime TIME = new ReservationTime(1L, LocalTime.of(10, 0));
    private static final Theme THEME = new Theme(1L, "테마", "설명", "https://example.com/image.jpg");

    @Test
    @DisplayName("예약자 이름이 공백이면 생성에 실패한다.")
    void failCreate_WhenNameIsBlank() {
        assertThatThrownBy(() -> new Reservation(null, "", LocalDate.now(), TIME, THEME))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("예약자 이름이 30자를 초과하면 생성에 실패한다.")
    void failCreate_WhenNameIsLongerThan30() {
        assertThatThrownBy(() -> new Reservation(null, "a".repeat(31), LocalDate.now(), TIME, THEME))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("예약 날짜가 null이면 생성에 실패한다.")
    void failCreate_WhenDateIsNull() {
        assertThatThrownBy(() -> new Reservation(null, "브라운", null, TIME, THEME))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("예약 시간이 null이면 생성에 실패한다.")
    void failCreate_WhenTimeIsNull() {
        assertThatThrownBy(() -> new Reservation(null, "브라운", LocalDate.now(), null, THEME))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("테마가 null이면 생성에 실패한다.")
    void failCreate_WhenThemeIsNull() {
        assertThatThrownBy(() -> new Reservation(null, "브라운", LocalDate.now(), TIME, null))
                .isInstanceOf(DomainException.class);
    }
}
