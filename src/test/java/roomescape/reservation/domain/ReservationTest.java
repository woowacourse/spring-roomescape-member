package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import roomescape.member.domain.Member;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

public class ReservationTest {

    @Test
    @DisplayName("이름에 빈 값이 들어간 경우 예외가 발생한다.")
    void nameEmptyException() {
        assertThatThrownBy(() -> new Reservation(
                new Member(1L,"","pond@mail.com",false),
                LocalDate.now(),
                new ReservationTime(0, LocalTime.now()),
                new Theme(0, "theme", "description", "thumbnail")
                )
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Name cannot be empty");
    }

    @Test
    @DisplayName("이름이 10글자 초과일 경우 예외가 발생한다.")
    void nameLengthException() {
        assertThatThrownBy(() -> new Reservation(
                new Member(1L,"namelength12","pond@mail.com",false),
                LocalDate.now(),
                new ReservationTime(0, LocalTime.now()),
                new Theme(0, "theme", "description", "thumbnail"))
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Name cannot exceed 10 characters");
    }
}
