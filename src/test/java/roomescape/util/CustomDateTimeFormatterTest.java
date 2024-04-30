package roomescape.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CustomDateTimeFormatterTest {

    @DisplayName("LocalDate 객체로부터 날짜 문자열 값을 반환한다.")
    @Test
    void getFormattedDateTest() {
        String formattedDate = CustomDateTimeFormatter.getFormattedDate(LocalDate.of(2023, 8, 5));
        assertThat(formattedDate).isEqualTo("2023-08-05");
    }

    @DisplayName("LocalTime 객체로부터 시간 문자열 값을 반환한다.")
    @Test
    void getFormattedTimeTest() {
        String formattedTime = CustomDateTimeFormatter.getFormattedTime(LocalTime.of(15, 40));
        assertThat(formattedTime).isEqualTo("15:40");
    }
}
