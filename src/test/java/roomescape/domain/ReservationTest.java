package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class ReservationTest {

    private final ReservationTime time;
    private final Theme theme;

    public ReservationTest() {
        this.time = new ReservationTime(1L, LocalTime.of(15, 40));
        this.theme = new Theme(1L, "공포의 저택", "버려진 저택에서 탈출하라! 어둠 속에 숨겨진 비밀을 밝혀야 살 수 있다.",
                "https://picsum.photos/seed/haunted/400/250");
    }

    @Test
    void 예약_생성() {
        Reservation reservation = new Reservation(1L, "브라운", LocalDate.of(2023, 8, 5), null, time, theme);

        assertThat(reservation.getId()).isEqualTo(1L);
        assertThat(reservation.getName()).isEqualTo("브라운");
        assertThat(reservation.getDate()).isEqualTo(LocalDate.of(2023, 8, 5));
        assertThat(reservation.getTime()).isEqualTo(time);
    }

    @Test
    void 이름이_null이면_예외() {
        assertThatThrownBy(() -> new Reservation(1L, null, LocalDate.of(2023, 8, 5), null, time, theme))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이름이_공백이면_예외() {
        assertThatThrownBy(() -> new Reservation(1L, "   ", LocalDate.of(2023, 8, 5), null, time, theme))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 날짜가_null이면_예외() {
        assertThatThrownBy(() -> new Reservation(1L, "브라운", null, null, time, theme))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 시간이_null이면_예외() {
        assertThatThrownBy(() -> new Reservation(1L, "브라운", LocalDate.of(2023, 8, 5), null, null, theme))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 과거_날짜로_예약하면_예외() {
        LocalDate today = LocalDate.of(2026, 5, 10);
        LocalDate yesterday = today.minusDays(1);

        assertThatThrownBy(() -> new Reservation(null, "브라운", yesterday, today, time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("과거 날짜로는 예약할 수 없습니다.");
    }

    @Test
    void createdAt이_null이면_과거_날짜_검증_스킵() {
        assertThat(new Reservation(1L, "브라운", LocalDate.of(2023, 8, 5), null, time, theme))
                .isNotNull();
    }

    @Test
    void id가_같으면_같은_예약() {
        Reservation a = new Reservation(1L, "브라운", LocalDate.of(2023, 8, 5), null, time, theme);
        Reservation b = new Reservation(1L, "다른이름", LocalDate.of(2024, 1, 1), null, time, theme);

        assertThat(a).isEqualTo(b);
    }

    @Test
    void id가_없으면_필드로_비교() {
        LocalDate date = LocalDate.of(2023, 8, 5);
        Reservation a = new Reservation(null, "브라운", date, null, time, theme);
        Reservation b = new Reservation(null, "브라운", date, null, time, theme);

        assertThat(a).isEqualTo(b);
    }

    @Test
    void id가_없고_필드가_다르면_다른_예약() {
        Reservation a = new Reservation(null, "브라운", LocalDate.of(2023, 8, 5), null, time, theme);
        Reservation b = new Reservation(null, "다른이름", LocalDate.of(2023, 8, 5), null, time, theme);

        assertThat(a).isNotEqualTo(b);
    }
}