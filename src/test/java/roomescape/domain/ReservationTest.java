package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class ReservationTest {

    private final ReservationTime time;
    private final Theme theme;
    private final LocalDate createdAt = LocalDate.of(2023, 8, 1);

    public ReservationTest() {
        this.time = new ReservationTime(1L, LocalTime.of(15, 40));
        this.theme = new Theme(1L, "공포의 저택", "버려진 저택에서 탈출하라! 어둠 속에 숨겨진 비밀을 밝혀야 살 수 있다.",
                "https://picsum.photos/seed/haunted/400/250");
    }

    @Test
    void 예약_생성() {
        Reservation reservation = Reservation.restore(1L, "브라운", LocalDate.of(2023, 8, 5), createdAt, time, theme);

        assertThat(reservation.getId()).isEqualTo(1L);
        assertThat(reservation.getName()).isEqualTo("브라운");
        assertThat(reservation.getDate()).isEqualTo(LocalDate.of(2023, 8, 5));
        assertThat(reservation.getTime()).isEqualTo(time);
    }

    @Test
    void 이름이_null이면_예외() {
        assertThatThrownBy(() -> Reservation.restore(1L, null, LocalDate.of(2023, 8, 5), createdAt, time, theme))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이름이_공백이면_예외() {
        assertThatThrownBy(() -> Reservation.restore(1L, "   ", LocalDate.of(2023, 8, 5), createdAt, time, theme))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 날짜가_null이면_예외() {
        assertThatThrownBy(() -> Reservation.restore(1L, "브라운", null, createdAt, time, theme))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 시간이_null이면_예외() {
        assertThatThrownBy(() -> Reservation.restore(1L, "브라운", LocalDate.of(2023, 8, 5), createdAt, null, theme))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 과거_날짜로_예약하면_예외() {
        LocalDate today = LocalDate.of(2026, 5, 10);
        LocalDate yesterday = today.minusDays(1);

        assertThatThrownBy(() -> Reservation.create("브라운", yesterday, today, time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("과거 날짜로는 예약할 수 없습니다.");
    }

    @Test
    void restore는_과거_날짜_검증_스킵() {
        assertThat(Reservation.restore(1L, "브라운", LocalDate.of(2023, 8, 5), LocalDate.now(), time, theme))
                .isNotNull();
    }

    @Test
    void id가_같으면_같은_예약() {
        Reservation a = Reservation.restore(1L, "브라운", LocalDate.of(2023, 8, 5), createdAt, time, theme);
        Reservation b = Reservation.restore(1L, "다른이름", LocalDate.of(2024, 1, 1), createdAt, time, theme);

        assertThat(a).isEqualTo(b);
    }

    @Test
    void id가_없으면_필드로_비교() {
        LocalDate date = LocalDate.of(2023, 8, 5);
        Reservation a = Reservation.restore(null, "브라운", date, createdAt, time, theme);
        Reservation b = Reservation.restore(null, "브라운", date, createdAt, time, theme);

        assertThat(a).isEqualTo(b);
    }

    @Test
    void id가_없고_필드가_다르면_다른_예약() {
        Reservation a = Reservation.restore(null, "브라운", LocalDate.of(2023, 8, 5), createdAt, time, theme);
        Reservation b = Reservation.restore(null, "다른이름", LocalDate.of(2023, 8, 5), createdAt, time, theme);

        assertThat(a).isNotEqualTo(b);
    }
}