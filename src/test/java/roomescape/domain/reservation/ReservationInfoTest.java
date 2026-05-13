package roomescape.domain.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.exception.InvalidRequestValueException;

class ReservationInfoTest {
    private ReservationTime testTime;
    private Theme testTheme;

    @BeforeEach
    void setUp() {
        testTime = new ReservationTime(1L, LocalTime.of(10, 0));
        testTheme = new Theme(1L, "우테코 방탈출", "재밌어요", "https://image.com");
    }

    @Test
    @DisplayName("정상 생성 테스트")
    void initTest() {
        long id = 1L;
        String name = "브라운";
        LocalDate date = LocalDate.now();

        ReservationInfo info = new ReservationInfo(id, name, date, testTime, testTheme);

        assertAll(
                () -> assertThat(info.id()).isEqualTo(id),
                () -> assertThat(info.name()).isEqualTo(name),
                () -> assertThat(info.date()).isEqualTo(date),
                () -> assertThat(info.time()).isEqualTo(testTime),
                () -> assertThat(info.reservationTheme()).isEqualTo(testTheme)
        );
    }

    @Test
    @DisplayName("String 형식의 날짜 정상 생성 테스트")
    void initByStringDateTest() {
        String dateStr = "2026-05-13";
        ReservationInfo info = new ReservationInfo(1L, "브라운", dateStr, testTime, testTheme);

        assertThat(info.date()).isEqualTo(LocalDate.parse(dateStr));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("이름이 비어있거나 공백이면 예외가 발생한다")
    void invalidNameTest(String invalidName) {
        assertThatThrownBy(() -> new ReservationInfo(1L, invalidName, LocalDate.now(), testTime, testTheme))
                .isInstanceOf(InvalidRequestValueException.class)
                .hasMessage("이름은 필수입니다.");
    }

    @Test
    @DisplayName("날짜가 null이면 예외가 발생한다")
    void nullDateTest() {
        assertThatThrownBy(() -> new ReservationInfo(1L, "브라운", (LocalDate) null, testTime, testTheme))
                .isInstanceOf(InvalidRequestValueException.class)
                .hasMessage("날짜는 필수입니다.");
    }

    @Test
    @DisplayName("잘못된 형식의 날짜 문자열은 예외가 발생한다")
    void invalidStringDateTest() {
        assertThatThrownBy(() -> new ReservationInfo(1L, "브라운", "2026/05/13", testTime, testTheme))
                .isInstanceOf(InvalidRequestValueException.class)
                .hasMessage("유효하지 않은 날짜입니다.");
    }

    @Test
    @DisplayName("ReservationTime이 null이면 예외가 발생한다")
    void nullTimeTest() {
        assertThatThrownBy(() -> new ReservationInfo(1L, "브라운", LocalDate.now(), null, testTheme))
                .isInstanceOf(InvalidRequestValueException.class)
                .hasMessage("시간 정보는 필수입니다.");
    }

    @Test
    @DisplayName("Theme이 null이면 예외가 발생한다")
    void nullThemeTest() {
        assertThatThrownBy(() -> new ReservationInfo(1L, "브라운", LocalDate.now(), testTime, null))
                .isInstanceOf(InvalidRequestValueException.class)
                .hasMessage("테마 정보는 필수입니다.");
    }
}