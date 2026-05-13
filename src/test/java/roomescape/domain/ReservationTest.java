package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.UnprocessableException;

class ReservationTest {
    private static final LocalDate TEST_DATE = LocalDate.parse("2027-05-05");
    private static final LocalTime START_AT = LocalTime.parse("10:00");
    private static final LocalDateTime PAST_DATE_TIME = LocalDateTime.of(2000, 1, 1, 0, 0);
    private static final LocalDateTime FUTURE_DATE_TIME = LocalDateTime.of(2099, 1, 1, 0, 0);

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    void 이름이_null_또는_blank이면_예외(String name) {
        // given
        ReservationTime time = new ReservationTime(1L, START_AT);
        Theme theme = new Theme("테마 이름", "테마 설명", "썸네일");

        // when & then
        assertThatThrownBy(() -> new Reservation(name, TEST_DATE, time, theme, FUTURE_DATE_TIME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 비어 있을 수 없습니다.");
    }

    @Test
    void 이름이_255자를_초과하면_예외() {
        // given
        String name = "a".repeat(256);
        ReservationTime time = new ReservationTime(1L, START_AT);
        Theme theme = new Theme("테마 이름", "테마 설명", "썸네일");

        // when & then
        assertThatThrownBy(() -> new Reservation(name, TEST_DATE, time, theme, FUTURE_DATE_TIME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 255자를 넘을 수 없습니다.");
    }

    @Test
    void 날짜_null로_예약_생성시_예외() {
        // given
        ReservationTime time = new ReservationTime(1L, START_AT);
        Theme theme = new Theme("테마 이름", "테마 설명", "썸네일");

        // when & then
        assertThatThrownBy(() -> new Reservation("구구", null, time, theme, FUTURE_DATE_TIME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("날짜는 비어 있을 수 없습니다.");
        ;
    }

    @Test
    void 예약_시간이_null이면_예약_생성시_예외() {
        // given
        Theme theme = new Theme("테마 이름", "테마 설명", "썸네일");

        // when & then
        assertThatThrownBy(() -> new Reservation("홍길동", TEST_DATE, null, theme, FUTURE_DATE_TIME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 시간은 비어있을 수 없습니다.");
    }

    @Test
    void 테마가_null이면_예약_생성시_예외() {
        // given
        ReservationTime time = new ReservationTime(1L, START_AT);

        // when & then
        assertThatThrownBy(() -> new Reservation("홍길동", TEST_DATE, time, null, FUTURE_DATE_TIME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마는 비어있을 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 255})
    void 예약_생성_성공_테스트(int count) {
        // given
        String name = "a".repeat(count);
        ReservationTime time = new ReservationTime(1L, START_AT);
        Theme theme = new Theme("테마 이름", "테마 설명", "썸네일");

        // when
        Reservation result = new Reservation(name, TEST_DATE, time, theme, FUTURE_DATE_TIME);

        // then
        assertThat(result.getName()).isEqualTo(name);
    }

    @Test
    void 예약_변경_성공_테스트() {
        // given
        ReservationTime time = new ReservationTime(1L, START_AT);
        Theme theme = new Theme("테마 이름", "테마 설명", "썸네일");
        Reservation before = new Reservation(1L, "브라운", TEST_DATE, time, theme);

        // when
        LocalDate updateDate = LocalDate.parse("2030-01-01");
        ReservationTime updateTime = new ReservationTime(2L, START_AT);
        Reservation updateReservation = before.update(updateDate, updateTime, PAST_DATE_TIME);

        // then
        assertThat(updateReservation.getDate()).isEqualTo(updateDate);
        assertThat(updateReservation.getTime().getId()).isEqualTo(updateTime.getId());
        assertThat(updateReservation.getId()).isEqualTo(before.getId());
    }

    @Test
    void 지난_날짜의_예약_변경_예외_발생_테스트() {
        // given
        ReservationTime time = new ReservationTime(1L, START_AT);
        Theme theme = new Theme("테마 이름", "테마 설명", "썸네일");
        Reservation before = new Reservation(1L, "브라운", LocalDate.from(FUTURE_DATE_TIME.minusDays(1L)), time, theme);

        // when
        LocalDate updateDate = LocalDate.from(FUTURE_DATE_TIME.plusDays(1L));
        ReservationTime updateTime = new ReservationTime(2L, START_AT);

        // then
        assertThatThrownBy(() -> before.update(updateDate, updateTime, FUTURE_DATE_TIME))
                .isInstanceOf(UnprocessableException.class)
                .hasMessage("지난 날짜의 예약은 변경할 수 없습니다.");
    }

    @Test
    void 지난_날짜로의_예약_변경_예외_발생_테스트() {
        // given
        ReservationTime time = new ReservationTime(1L, START_AT);
        Theme theme = new Theme("테마 이름", "테마 설명", "썸네일");
        Reservation before = new Reservation(1L, "브라운", TEST_DATE, time, theme);

        // when
        LocalDate updateDate = LocalDate.from(PAST_DATE_TIME.minusDays(1L));
        ReservationTime updateTime = new ReservationTime(2L, START_AT);

        // then
        assertThatThrownBy(() -> before.update(updateDate, updateTime, PAST_DATE_TIME))
                .isInstanceOf(UnprocessableException.class)
                .hasMessage("지난 날짜로 예약을 변경할 수 없습니다.");
    }

    @Test
    void 지난_예약_삭제_시_예외_발생_테스트() {
        // given
        ReservationTime time = new ReservationTime(1L, START_AT);
        Theme theme = new Theme("테마 이름", "테마 설명", "썸네일");
        Reservation reservation = new Reservation(1L, "브라운", LocalDate.from(PAST_DATE_TIME), time, theme);

        // when & then
        assertThatThrownBy(() -> reservation.validateCancelable(FUTURE_DATE_TIME))
                .isInstanceOf(UnprocessableException.class)
                .hasMessage("지난 예약은 취소할 수 없습니다.");
    }

    @Test
    void 지난_날짜로_예약_시_예외_발생_테스트() {
        // given
        ReservationTime time = new ReservationTime(1L, START_AT);
        Theme theme = new Theme("테마 이름", "테마 설명", "썸네일");

        // when & then
        assertThatThrownBy(() -> new Reservation("브라운", LocalDate.from(PAST_DATE_TIME), time, theme, FUTURE_DATE_TIME))
                .isInstanceOf(UnprocessableException.class)
                .hasMessage("지난 시간으로는 예약할 수 없습니다.");

    }
}
