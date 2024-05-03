package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationTest {

    @DisplayName("생성 테스트")
    @Test
    void create() {
        assertThatCode(() -> new Reservation(1L, "wiib", new ReservationDate("2040-04-01"),
            new ReservationTime(1L, LocalTime.now().toString()),
            new Theme("방탈출", "방탈출하는 게임", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")))
            .doesNotThrowAnyException();
        assertThatCode(
            () -> new Reservation("wiib", new ReservationDate("2040-04-01"),
                new ReservationTime(1L, LocalTime.now().toString()),
                new Theme("방탈출", "방탈출하는 게임",
                    "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")))
            .doesNotThrowAnyException();

    }

    @DisplayName("예약자명이 1글자 이상 10글자 이하가 아니면 예외가 발생한다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "01234567890"})
    void create_WithBlankName(String name) {
        assertThatThrownBy(
            () -> new Reservation(name, new ReservationDate("2040-04-01"),
                new ReservationTime(LocalTime.MAX.toString()),
                new Theme("방탈출", "방탈출하는 게임",
                    "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("날짜를 null로 생성하면 예외가 발생한다.")
    @Test
    void create_WithNullDate() {
        assertThatThrownBy(
            () -> new Reservation("브리", null,
                new ReservationTime(LocalTime.MAX.toString()),
                new Theme("방탈출", "방탈출하는 게임",
                    "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("시간을 null로 생성하면 예외가 발생한다.")
    @Test
    void create_WithNullTime() {
        assertThatThrownBy(
            () -> new Reservation("브리", new ReservationDate("2040-04-01"),
                null,
                new Theme("방탈출", "방탈출하는 게임",
                    "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테마를 null로 생성하면 예외가 발생한다.")
    @Test
    void create_WithNullTheme() {
        assertThatThrownBy(
            () -> new Reservation("브리", new ReservationDate("2040-04-01"),
                new ReservationTime(LocalTime.MAX.toString()),
                null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("과거의 예약인지를 판단한다.")
    @Test
    void isPast() {
        Reservation reservation = new Reservation("name", new ReservationDate("1900-01-01"),
            new ReservationTime("10:00"), new Theme("theme", "desc", "https://"));

        assertThat(reservation.isPast()).isTrue();
    }
}
