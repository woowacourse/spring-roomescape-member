package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {
    @DisplayName("정상 생성 테스트")
    @Test
    void validCreate() {
        assertDoesNotThrow(() -> new Reservation(1L, new Name("aa"), LocalDate.parse("9999-12-31"),
                new ReservationTime(LocalTime.parse("10:00")),
                new RoomTheme("레벨 2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")));
    }

    @DisplayName("현재 시간보다 이전의 시간을 입력하면 예외를 발생시킨다.")
    @Test
    void validateDateAndTime() { //TODO 현재 시간 입력받게 변경?
        assertThatThrownBy(() -> new Reservation(1L, new Name("브라운"), LocalDate.parse("2024-05-01"),
                new ReservationTime(LocalTime.parse("10:00")),
                new RoomTheme("레벨 2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약할 수 없는 날짜입니다.");
    }
}
