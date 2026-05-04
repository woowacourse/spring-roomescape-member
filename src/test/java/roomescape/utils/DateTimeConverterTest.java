package roomescape.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.format.DateTimeParseException;

class DateTimeConverterTest {

    @ParameterizedTest
    @CsvSource(value = {
            "2026-13-28",
            "2026-12-32",
            "2026-02-31"
    })
    public void 날짜_형식이_잘못_되었을_경우에는_예외가_터진다(String wrongDate) {
        Assertions.assertThatThrownBy(() -> {
            DateTimeConverter.dateConverter(wrongDate);
        }).isExactlyInstanceOf(DateTimeParseException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"2026-12-28", "2026-04-28"})
    public void 정확한_날짜_형식일_때는_예외가_발생하지_않는다(String rightDate) {
        Assertions.assertThatCode(() -> {
            DateTimeConverter.dateConverter(rightDate);
        }).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @CsvSource(value = {
            "14:61",
            "22-00",
            "25:07"
    })
    public void 시간_형식이_잘못_되었을_경우에는_예외가_터진다(String wrongTime) {
        Assertions.assertThatThrownBy(() -> {
            DateTimeConverter.timeConverter(wrongTime);
        }).isExactlyInstanceOf(DateTimeParseException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"14:45", "08:23"})
    public void 정확한_시간_형식일_때는_예외가_발생하지_않는다(String rightTime) {
        Assertions.assertThatCode(() -> {
            DateTimeConverter.timeConverter(rightTime);
        }).doesNotThrowAnyException();
    }
}
