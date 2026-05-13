package roomescape.domain.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.InvalidRequestValueException;

class ReservationCommandTest {
    @Test
    @DisplayName("정상 생성 테스트 (String 입력)")
    void initTest() {
        String name = "브라운";
        String dateStr = LocalDate.now().toString();
        long timeId = 1L;
        long themeId = 1L;

        ReservationCommand command = new ReservationCommand(name, dateStr, timeId, themeId);

        assertAll(
                () -> assertThat(command.name()).isEqualTo(name),
                () -> assertThat(command.date()).isEqualTo(LocalDate.parse(dateStr)),
                () -> assertThat(command.timeId()).isEqualTo(timeId)
        );
    }

    @Test
    @DisplayName("정상 생성 테스트 (LocalDate 직접 입력)")
    void initLocalDateTest() {
        LocalDate now = LocalDate.now();
        ReservationCommand command = new ReservationCommand("브라운", now, 1L, 1L);

        assertThat(command.date()).isEqualTo(now);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    @DisplayName("이름이 비어있거나 공백인 경우 예외 테스트")
    void NameBlankTest(String invalidName) {
        assertThatThrownBy(() -> new ReservationCommand(invalidName, LocalDate.now(), 1L, 1L))
                .isInstanceOf(InvalidRequestValueException.class)
                .hasMessage("이름은 필수입니다.");
    }

    @Test
    @DisplayName("이름이 20자를 초과한 경우 예외 테스트")
    void NameLengthTest() {
        String longName = "a".repeat(21);

        assertThatThrownBy(() -> new ReservationCommand(longName, LocalDate.now(), 1L, 1L))
                .isInstanceOf(InvalidRequestValueException.class)
                .hasMessage("이름은 20자를 초과할 수 없습니다.");
    }

    @Test
    @DisplayName("날짜가 null인 경우 예외 테스트")
    void NullDateTest() {
        assertThatThrownBy(() -> new ReservationCommand("브라운", (LocalDate) null, 1L, 1L))
                .isInstanceOf(InvalidRequestValueException.class)
                .hasMessage("날짜는 필수입니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"2024-02-30", "2024-13-01", "not-a-date", "24-05-01"})
    @DisplayName("잘못된 형식의 문자열 날짜인 경우 예외 테스트")
    void InvalidDateFormatTest(String invalidDate) {
        assertThatThrownBy(() -> new ReservationCommand("브라운", invalidDate, 1L, 1L))
                .isInstanceOf(InvalidRequestValueException.class)
                .hasMessage("유효하지 않은 날짜 형식입니다.");
    }

    @Test
    @DisplayName("LocalDate를 직접 전달해도 과거 날짜면 예외를 던진다")
    void PastLocalDateTest() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        assertThatThrownBy(() -> new ReservationCommand("브라운", yesterday, 1L, 1L))
                .isInstanceOf(InvalidRequestValueException.class)
                .hasMessage("예약일은 현재 날짜보다 이전일 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1, -100})
    @DisplayName("시간 ID가 0 이하인 경우 예외 테스트")
    void NotPositiveTimeIdTest(long invalidTimeId) {
        assertThatThrownBy(() -> new ReservationCommand("브라운", LocalDate.now(), invalidTimeId, 1L))
                .isInstanceOf(InvalidRequestValueException.class)
                .hasMessage("시간 id는 0보다 커야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1, -100})
    @DisplayName("테마 ID가 0 이하인 경우 예외 테스트")
    void NotPositiveThemeIdTest(long invalidThemeId) {
        assertThatThrownBy(() -> new ReservationCommand("브라운", LocalDate.now(), 1L, invalidThemeId))
                .isInstanceOf(InvalidRequestValueException.class)
                .hasMessage("테마 id 0보다 커야 합니다.");
    }
}