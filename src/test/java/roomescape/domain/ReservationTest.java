package roomescape.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.vo.Description;
import roomescape.domain.vo.Name;
import roomescape.domain.vo.ThumbnailUrl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ReservationTest {
    private final Name name = new Name("브라운");
    private final ThumbnailUrl url = new ThumbnailUrl("https://test1.com");
    private final Description description = new Description("설명을 테스트 해야 하는 테스트 설명");
    private final Theme theme = new Theme(1L, new Name("테마입니다"), url, description);
    private final Time time = new Time(1L, LocalTime.of(14, 0));
    private final LocalDateTime fixedNow = LocalDateTime.of(2026, 5, 10, 12, 0);


    @Nested
    @DisplayName("예약 생성 시")
    class Create{
        @Test
        void 과거_날짜는_예약_불가() {
            LocalDate past = LocalDate.of(2026, 5, 9);

            assertThatThrownBy(() ->
                    Reservation.create(name, past, time, theme, fixedNow))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 정확히_14일_뒤는_가능() {
            LocalDate exact14 = LocalDate.of(2026, 5, 24);

            assertThatNoException().isThrownBy(() ->
                    Reservation.create(name, exact14, time, theme, fixedNow));
        }

        @Test
        void _15일_뒤는_불가() {
            LocalDate day15 = LocalDate.of(2026, 5, 25);

            assertThatThrownBy(() ->
                    Reservation.create(name, day15, time, theme, fixedNow))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 오늘_이미_지난_시각은_불가() {
            LocalDate tomorrow = LocalDate.of(2026, 5, 11);
            Reservation created = Reservation.create(name, tomorrow, time, theme, fixedNow);

            LocalDate today = LocalDate.of(2026, 5, 10);
            Time pastTime = new Time(1L, LocalTime.of(11, 0));

            assertThatThrownBy(() ->
                    created.update(null, today, pastTime, theme, fixedNow))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 오늘_안_지난_시각은_가능() {
            LocalDate today = LocalDate.of(2026, 5, 10);
            Time eveningTime = new Time(1L, LocalTime.of(20, 0));

            assertThatNoException().isThrownBy(() ->
                    Reservation.create(name, today, eveningTime, theme, fixedNow));
        }
    }

    @Nested
    @DisplayName("예약 변경 후")
    class Update {

        @Test
        void 과거_날짜는_예약_불가() {
            LocalDate tomorrow = LocalDate.of(2026, 5, 11);
            Reservation created = Reservation.create(name, tomorrow, time, theme, fixedNow);

            Name newName = new Name("뉴브라운");
            LocalDate dayAfterTomorrow = LocalDate.of(2026, 5, 9);
            assertThatThrownBy(() -> created.update(newName, dayAfterTomorrow, time, theme, fixedNow))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 정확히_14일_뒤는_가능() {
            LocalDate tomorrow = LocalDate.of(2026, 5, 11);
            Reservation created = Reservation.create(name, tomorrow, time, theme, fixedNow);

            Name newName = new Name("뉴브라운");
            LocalDate exact14 = LocalDate.of(2026, 5, 24);

            assertThatNoException().isThrownBy(() ->
                    created.update(newName, exact14, time, theme, fixedNow));
        }

        @Test
        void _15일_뒤는_불가() {
            LocalDate tomorrow = LocalDate.of(2026, 5, 11);
            Reservation created = Reservation.create(name, tomorrow, time, theme, fixedNow);

            Name newName = new Name("뉴브라운");
            LocalDate day15 = LocalDate.of(2026, 5, 25);

            assertThatThrownBy(() ->
                    created.update(name, day15, time, theme, fixedNow))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 오늘_이미_지난_시각은_불가() {
            LocalDate yesterday = LocalDate.of(2026, 5, 10);
            LocalDate today = LocalDate.of(2026, 5, 10);
            Time noonTime = new Time(1L, LocalTime.of(11, 0));

            assertThatThrownBy(() ->
                    Reservation.create(name, today, noonTime, theme, fixedNow))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 오늘_안_지난_시각은_가능() {
            LocalDate today = LocalDate.of(2026, 5, 10);
            Time eveningTime = new Time(1L, LocalTime.of(20, 0));

            assertThatNoException().isThrownBy(() ->
                    Reservation.create(name, today, eveningTime, theme, fixedNow));
        }

        @Test
        void 이름이_없다면_원래_이름을_사용한다(){
            LocalDate tomorrow = LocalDate.of(2026, 5, 11);
            Reservation created = Reservation.create(name, tomorrow, time, theme, fixedNow);

            LocalDate dayAfterTomorrow = LocalDate.of(2026, 5, 12);
            Reservation updated = created.update(null, dayAfterTomorrow, time, theme, fixedNow);

            assertThat(updated.getName().value()).isEqualTo(created.getName().value());
            assertThat(updated.getDate()).isNotEqualTo(created.getDate());
        }

        @Test
        void 날짜가_없다면_원래_날짜를_사용한다(){
            LocalDate tomorrow = LocalDate.of(2026, 5, 11);
            Reservation created = Reservation.create(name, tomorrow, time, theme, fixedNow);

            Name newName = new Name("뉴브라운");
            Reservation updated = created.update(newName, null, time, theme, fixedNow);

            assertThat(updated.getName().value()).isNotEqualTo(created.getName().value());
            assertThat(updated.getDate()).isEqualTo(created.getDate());
        }

        @Test
        void 예약_날짜_둘_다_변경이_가능히다() {
            LocalDate tomorrow = LocalDate.of(2026, 5, 11);
            Reservation created = Reservation.create(name, tomorrow, time, theme, fixedNow);

            Name newName = new Name("뉴브라운");
            LocalDate dayAfterTomorrow = LocalDate.of(2026, 5, 12);
            Reservation updated = created.update(newName, dayAfterTomorrow, time, theme, fixedNow);

            assertThat(updated.getName()).isEqualTo(newName);
            assertThat(updated.getDate()).isEqualTo(dayAfterTomorrow);
        }
    }
}