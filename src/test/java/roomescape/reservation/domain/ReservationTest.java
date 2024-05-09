package roomescape.reservation.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.BadRequestException;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.Fixtures.themeFixture;

@DisplayName("예약")
class ReservationTest {

    @DisplayName("예약자명이 공백인 경우 예외가 발생한다.")
    @ValueSource(strings = {"", " ", "    ", "\n", "\r", "\t"})
    @ParameterizedTest
    void validateName(String blankName) {
        ReservationTime time = new ReservationTime(LocalTime.MAX);
        assertThatThrownBy(() -> new Reservation(
                    new Member(1L, "", "tester@gmail.com", "password"),
                    LocalDate.MAX, time, themeFixture)
                ).isInstanceOf(BadRequestException.class)
                .hasMessage("이름은 공백일 수 없습니다.");
    }
}
