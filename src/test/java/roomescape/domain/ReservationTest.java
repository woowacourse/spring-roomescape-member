package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @Test
    @DisplayName("생성 테스트")
    void create() {
        LocalDate date = LocalDate.now();
        Member member = new Member(
                new MemberName("호돌"),
                new Email("email"),
                new Password("******"),
                MemberRole.NORMAL
        );
        ReservationTime time = new ReservationTime(LocalTime.now());
        Theme theme = new Theme(
                new ThemeName("레벨 1 방탈출"),
                new Description("description"),
                new Thumbnail("thumbnail")
        );

        assertThatCode(() -> new Reservation(date, member, time, theme))
                .doesNotThrowAnyException();
    }
}
