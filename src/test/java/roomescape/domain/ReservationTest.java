package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.InitialDataFixture.USER_1;
import static roomescape.InitialDataFixture.ADMIN_1;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @Test
    @DisplayName("입력받은 formatter에 맞게 날짜를 String으로 변환한다.")
    void formatGetDate() {
        Reservation reservation = new Reservation(
                1L,
                USER_1,
                LocalDate.of(2024, 4, 24),
                null,
                new Theme(1L, new Name("레벨2 탈출"), "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")
        );

        String formatted = reservation.getDate(DateTimeFormatter.ISO_DATE);

        assertThat(formatted).isEqualTo("2024-04-24");
    }

    @Test
    @DisplayName("Reservation 객체의 동등성을 따질 때는 id만 확인한다.")
    void testEquals() {
        Reservation reservation1 = new Reservation(
                1L,
                USER_1,
                LocalDate.of(2024, 4, 24),
                null,
                new Theme(1L, new Name("레벨2 탈출"), "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")
        );
        Reservation reservation2 = new Reservation(
                1L,
                ADMIN_1,
                LocalDate.of(2024, 3, 22),
                null,
                new Theme(1L, new Name("레벨2 탈출"), "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")
        );

        assertThat(reservation1).isEqualTo(reservation2);
    }
}
