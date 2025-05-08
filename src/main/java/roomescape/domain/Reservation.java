package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Reservation {

    private static final String KOREAN_WORDS_REGEX = "^[가-힣]+$";
    private static final int NAME_MAX_SIZE = 5;

    @EqualsAndHashCode.Include
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final ReservationTheme theme;

    public Reservation(final Long id, final String name, final LocalDate date, final ReservationTime time,
                       final ReservationTheme theme) {
        validateNameKoreanWords(name);
        validateNameSize(name);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(final String name, final LocalDate date, final ReservationTime time,
                       final ReservationTheme theme) {
        this(null, name, date, time, theme);
    }

    public Reservation assignId(final Long id) {
        return new Reservation(id, name, date, time, theme);
    }

    public boolean isDuplicateReservation(Reservation reservation) {
        return this.toDateTime().equals(reservation.toDateTime());
    }

    public LocalDateTime toDateTime() {
        return LocalDateTime.of(date, time.getStartAt());
    }

    private void validateNameKoreanWords(String name) {
        if (!name.matches(KOREAN_WORDS_REGEX)) {
            throw new IllegalArgumentException("[ERROR] 이름은 한국어로만 입력해 주세요.");
        }
    }

    private void validateNameSize(String name) {
        if (name.length() > NAME_MAX_SIZE) {
            throw new IllegalArgumentException(
                    "[ERROR] 이름은 " + NAME_MAX_SIZE + "글자 이내로 입력해 주세요. 현재 길이는 " + name.length() + "글자 입니다.");
        }
    }
}
