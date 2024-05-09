package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.exception.ExceptionType.EMPTY_DATE;
import static roomescape.exception.ExceptionType.EMPTY_THEME;
import static roomescape.exception.ExceptionType.EMPTY_TIME;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.RoomescapeException;

class ReservationTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.now();
    private static final ReservationTime DEFAULT_TIME = new ReservationTime(1L, LocalTime.now());
    private static final Theme DEFAULT_THEME = new Theme(1L, "이름", "설명", "http://썸네일");

    @DisplayName("생성 테스트")
    @Test
    void constructTest() {
        Member member = new Member(1L, "name", "email@email.com", new Sha256Encryptor().encrypt("1234"));
        assertAll(
                () -> assertThatThrownBy(() -> new Reservation(member, null, DEFAULT_TIME, DEFAULT_THEME))
                        .isInstanceOf(RoomescapeException.class)
                        .hasMessage(EMPTY_DATE.getMessage()),

                () -> assertThatThrownBy(() -> new Reservation(member, DEFAULT_DATE, null, DEFAULT_THEME))
                        .isInstanceOf(RoomescapeException.class)
                        .hasMessage(EMPTY_TIME.getMessage()),

                () -> assertThatThrownBy(() -> new Reservation(member, DEFAULT_DATE, DEFAULT_TIME, null))
                        .isInstanceOf(RoomescapeException.class)
                        .hasMessage(EMPTY_THEME.getMessage())
        );

    }

    @Test
    @DisplayName("날짜를 기준으로 비교를 잘 하는지 확인.")
    void compareTo() {
        Member robin = new Member(1L, "name", "email@email.com", new Sha256Encryptor().encrypt("1234"));
        Member polla = new Member(2L, "name", "email@email.com", new Sha256Encryptor().encrypt("1234"));

        Reservation first = new Reservation(1L, robin, LocalDate.of(1999, 12, 1), new ReservationTime(
                LocalTime.of(16, 30)), DEFAULT_THEME);
        Reservation second = new Reservation(2L, polla, LocalDate.of(1998, 1, 8), new ReservationTime(
                LocalTime.of(16, 30)), DEFAULT_THEME);
        int compareTo = first.compareTo(second);
        Assertions.assertThat(compareTo)
                .isGreaterThan(0);
    }
}
