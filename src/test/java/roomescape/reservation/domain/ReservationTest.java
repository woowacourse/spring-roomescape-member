package roomescape.reservation.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.BusinessRuleViolationException;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    private final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(15, 0));
    private final Theme theme = new Theme(1L, "테마", "설명", "url");
    private final LocalDateTime today = LocalDateTime.now();
    private final LocalDate futureDate = LocalDate.now().plusDays(1);

    @Nested
    @DisplayName("생성 및 이름 검증 테스트")
    class CreationTest {

        @Test
        @DisplayName("정상적인 데이터로 예약 객체를 생성한다.")
        void createSuccess() {
            assertThatCode(() -> new Reservation(1L, "브라운", futureDate, reservationTime, theme, ReservationStatus.RESERVED))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("이름이 비어있으면 예외가 발생한다.")
        void nullCheck() {
            assertThatThrownBy(() -> new Reservation(1L, null, futureDate, reservationTime, theme, ReservationStatus.RESERVED))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("예약자 이름은 반드시 입력해야 합니다.");
        }

        @ParameterizedTest
        @ValueSource(strings = {" ", "  "})
        @DisplayName("이름이 비어있거나 공백이면 예외가 발생한다.")
        void failWhenNameIsBlank(String name) {
            assertThatThrownBy(() -> new Reservation(1L, name, futureDate, reservationTime, theme, ReservationStatus.RESERVED))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("예약자 이름은 반드시 입력해야 합니다.");
        }

        @Test
        @DisplayName("이름이 10자를 초과하면 예외가 발생한다.")
        void failWhenNameIsTooLong() {
            String longName = "열한글자이름입니다용ㅇ";
            assertThatThrownBy(() -> new Reservation(1L, longName, futureDate, reservationTime, theme, ReservationStatus.RESERVED))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasMessageContaining("이름은 10글자 이하여야 합니다.");
        }

        @Test
        @DisplayName("필수 데이터(날짜, 시간, 테마)가 누락되면 예외가 발생한다.")
        void failWhenRequiredFieldIsNull() {
            assertThatThrownBy(() -> new Reservation(1L, "브라운", null, reservationTime, theme, ReservationStatus.RESERVED))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("create 팩토리는")
    class CreateFactoryTest {

        @Test
        @DisplayName("미래 일시면 정상 생성한다.")
        void createSuccessWhenFuture() {
            assertThatCode(() -> Reservation.create("브라운", futureDate, reservationTime, theme, today))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("과거 일시로 생성하면 invariant 위반으로 BusinessRuleViolationException 이 발생한다.")
        void createFailWhenPast() {
            // given
            LocalDate pastDate = today.toLocalDate().minusDays(1);

            // when & then
            assertThatThrownBy(() -> Reservation.create("브라운", pastDate, reservationTime, theme, today))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasMessageContaining("과거 시각으로는 예약할 수 없습니다.");
        }

        @Test
        @DisplayName("오늘 날짜라도 예약 시간이 현재 시간보다 이전이면 예외가 발생한다.")
        void failWhenTodayButPastTime() {
            // given
            LocalDate date = today.toLocalDate();
            LocalTime time = today.toLocalTime().minusMinutes(1);

            // when & then
            assertThatThrownBy(() -> Reservation.create("브라운", date, new ReservationTime(1L, time), theme, today))
                    .isInstanceOf(BusinessRuleViolationException.class);
        }

        @Test
        @DisplayName("이름이 비어있으면 invariant 검증 이전에 NullPointerException 이 발생한다.")
        void createFailWhenNameBlank() {
            assertThatThrownBy(() -> Reservation.create(" ", futureDate, reservationTime, theme, today))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("예약자 이름은 반드시 입력해야 합니다.");
        }
    }

    @Nested
    class CancelTest {

        @Test
        @DisplayName("미래 시점의 예약은 취소 검증을 통과한다.")
        void validateCancelSuccess() {
            // given
            Reservation reservation = new Reservation(1L, "브라운", futureDate, reservationTime, theme, ReservationStatus.RESERVED);

            // when & then
            assertThatCode(reservation::validateCanCancel)
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("이미 취소된 예약은 통과할 수 없다.")
        void alreadyCanceled() {
            // given
            Reservation reservation = new Reservation(1L, "브라운", futureDate, reservationTime, theme, ReservationStatus.CANCELED);

            // when & then
            assertThatThrownBy(reservation::validateCanCancel)
                    .isInstanceOf(BusinessRuleViolationException.class);
        }

        @Test
        @DisplayName("이미 완료된 예약은 당연히 통과할 수 없다.")
        void alreadyCompleted() {
            // given
            Reservation reservation = new Reservation(1L, "브라운", futureDate, reservationTime, theme, ReservationStatus.COMPLETED);

            // when & then
            assertThatThrownBy(reservation::validateCanCancel)
                    .isInstanceOf(BusinessRuleViolationException.class);
        }
    }

    @Nested
    class UpdateTest {

        @Test
        @DisplayName("미래 시점의 예약은 변경할 수 있다.")
        void validateCancelSuccess() {
            // given
            Reservation reservation = new Reservation(1L, "브라운", futureDate, reservationTime, theme, ReservationStatus.RESERVED);

            // when & then
            assertThatCode(() -> reservation.update("브라운", futureDate, reservationTime, theme, today))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("이미 취소된 예약은 변경할 수 없다.")
        void alreadyCanceled() {
            // given
            Reservation reservation = new Reservation(1L, "브라운", futureDate, reservationTime, theme, ReservationStatus.CANCELED);

            // when & then
            assertThatThrownBy(() -> reservation.update("브라운", futureDate, reservationTime, theme, today))
                    .isInstanceOf(BusinessRuleViolationException.class);
        }

        @Test
        @DisplayName("이미 완료된 예약은 변경할 수 없다.")
        void alreadyCompleted() {
            // given
            Reservation reservation = new Reservation(1L, "브라운", futureDate, reservationTime, theme, ReservationStatus.COMPLETED);

            // when & then
            assertThatThrownBy(() -> reservation.update("브라운", futureDate, reservationTime, theme, today))
                    .isInstanceOf(BusinessRuleViolationException.class);
        }
    }
}
