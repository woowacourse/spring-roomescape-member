package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    @Test
    @DisplayName("올바른 입력시 객체를 생성한다.")
    void validReservation() {
        assertThat(new Reservation(LocalDate.now(), new Member("뽀로로", "email@email.com", "1234"), new ReservationTime(LocalTime.now()), new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"))).isNotNull();
    }

    @Test
    @DisplayName("날짜에 대한 입력이 올바르지 않으면 예외가 발생한다.")
    void invalidDate() {
        assertThatThrownBy(() -> new Reservation(null, new Member("뽀로로", "email@email.com", "1234"), new ReservationTime(LocalTime.now()), new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("예약시간이 올바르지 않으면 예외가 발생한다.")
    void invalidTime() {
        assertThatThrownBy(() -> new Reservation(LocalDate.now(), new Member("뽀로로", "email@email.com", "1234"), null, new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
