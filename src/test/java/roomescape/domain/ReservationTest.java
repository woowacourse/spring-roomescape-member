//package roomescape.domain;
//
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import roomescape.exception.EmptyParameterException;
//
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static roomescape.Fixtures.themeFixture;
//
//@DisplayName("예약")
//class ReservationTest {
//
//    @DisplayName("예약자명이 공백인 경우 예외가 발생한다.")
//    @ValueSource(strings = {"", " ", "    ", "\n", "\r", "\t"})
//    @ParameterizedTest
//    @Disabled
//    void validateName(String blankName) {
//        ReservationTime time = new ReservationTime(LocalTime.MAX);
//        assertThatThrownBy(() -> new Reservation(blankName, LocalDate.MAX, time, themeFixture))
//                .isInstanceOf(EmptyParameterException.class)
//                .hasMessage("이름은 공백일 수 없습니다.");
//    }
//}
