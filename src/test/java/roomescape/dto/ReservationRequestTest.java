package roomescape.dto;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ReservationRequestTest {

    @DisplayName("빈 값 입력 검증")
    @Test
    void validateNotNullFail() {
        assertAll(
                () -> assertThatCode(() -> new ReservationRequest(null, LocalDate.now(), 1L))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("빈 값은 입력할 수 없다."),
                () -> assertThatCode(() -> new ReservationRequest("알파카", null, 1L))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("빈 값은 입력할 수 없다."),
                () -> assertThatCode(() -> new ReservationRequest("산초", LocalDate.now(), null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("빈 값은 입력할 수 없다.")
        );
    }
}
