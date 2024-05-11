package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import roomescape.exception.RoomEscapeException;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

class ReservationTest {
    private final Member member = new Member(1L, "hotea", "hotea@hotea.com", Role.USER);
    private final Theme theme = new Theme(1L, "정글 모험", "열대 정글의 심연을 탐험하세요.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
    private final ReservationTime reservationTime = new ReservationTime(1L, "15:46");

    @DisplayName("존재하지 않는 날짜를 선택했을 경우 예외가 발생한다")
    @Test
    void validateDateAndTimeExist() {
        ReservationTime reservationTime = new ReservationTime(1L, "15:46");

        assertAll(
                () -> assertThatThrownBy(() -> new Reservation(1L, member, new ReservationDate(null), reservationTime, theme))
                        .isInstanceOf(RoomEscapeException.class)
                        .hasMessage("형식에 맞지 않은 날짜입니다."),
                () -> assertThatThrownBy(() -> new Reservation(1L, member, new ReservationDate("2024-14-30"), reservationTime, theme))
                        .isInstanceOf(RoomEscapeException.class)
                        .hasMessage("형식에 맞지 않은 날짜입니다."),
                () -> assertThatThrownBy(() -> new Reservation(1L, member, new ReservationDate("2024-04-50"), reservationTime, theme))
                        .isInstanceOf(RoomEscapeException.class)
                        .hasMessage("형식에 맞지 않은 날짜입니다.")
        );
    }
}
