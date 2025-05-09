package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @DisplayName("같은 시간 여부를 검증한다.")
    @Test
    void isSameTime() {
        //given
        ReservationTime time = new ReservationTime("10:30");
        ReservationTime sameTime = new ReservationTime("10:30");
        ReservationTime anotherTime = new ReservationTime("10:31");

        //when
        boolean isSameTime = time.isSameTime(sameTime);
        boolean isNotSameTime = sameTime.isSameTime(anotherTime);

        //then
        assertThat(isSameTime).isTrue();
        assertThat(isNotSameTime).isFalse();
    }

    @DisplayName("시간 문자열 포맷이 올바르지 않을 시 에러를 발생한다.")
    @Test
    void validateTimeFormat() {
        // given
        String invalidTimeFormatWithColon = "10;30";
        String invalidTimeFormatWithNumber = "10:302";

        // when & then
        assertThatThrownBy(() -> new ReservationTime(invalidTimeFormatWithColon))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 시간 형식을 HH:mm 으로 수정해 주세요");
        assertThatThrownBy(() -> new ReservationTime(invalidTimeFormatWithNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 시간 형식을 HH:mm 으로 수정해 주세요");
    }
}
