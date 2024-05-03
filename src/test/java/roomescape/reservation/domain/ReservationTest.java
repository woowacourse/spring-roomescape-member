package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.fixture.ThemeFixture.getTheme1;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("예약 도메인 테스트")
class ReservationTest {
    @DisplayName("동일한 id는 같은 예약이다.")
    @Test
    void equals() {
        //given
        long id = 1L;
        String name1 = "choco";
        LocalDate date1 = LocalDate.now().plusYears(1);
        long timeId = 1;
        LocalTime localTime = LocalTime.of(12, 23, 0);
        ReservationTime time1 = new ReservationTime(timeId, localTime);

        String name2 = "pororo";
        LocalDate date2 = LocalDate.now().plusYears(1);
        LocalTime time2 = LocalTime.of(11, 23, 0);

        //when
        Reservation reservation1 = new Reservation(id, name1, date1, time1, getTheme1());
        Reservation reservation2 = new Reservation(id, name2, date2, time1, getTheme1());

        //then
        assertThat(reservation1).isEqualTo(reservation2);
    }

    @DisplayName("현재보다 이전 시각의 예약을 생성하면 예외가 발생한다.")
    @Test
    void createIllegalException() {
        //given
        LocalDate localDate = LocalDate.of(2017, 12, 30);
        long timeId = 1;
        LocalTime localTime = LocalTime.of(12, 23, 0);
        ReservationTime time = new ReservationTime(timeId, localTime);

        //when & then
        assertThatThrownBy(() -> new Reservation(1L, "테스트", localDate, time, getTheme1()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("한글, 영어 이외의 이름에 대해 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "$#@%!"})
    void invalidName(String invalidName) {
        //given
        long id = 1L;
        LocalDate date1 = LocalDate.now().plusYears(1);
        long timeId = 1;
        LocalTime localTime = LocalTime.of(12, 23, 0);
        ReservationTime time1 = new ReservationTime(timeId, localTime);

        //when & then
        assertThatThrownBy(() -> new Reservation(id, invalidName, date1, time1, getTheme1()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
