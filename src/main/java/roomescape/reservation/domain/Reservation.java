package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import roomescape.reservation.domain.exception.InvalidReservationException;

@Getter
@EqualsAndHashCode(of = {"name", "date", "themeId", "timeId"})
public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final Long themeId;
    private final Long timeId;

    @Builder
    public Reservation(Long id, String name, LocalDate date, Long themeId, Long timeId) {
        this.id = id;
        this.name = requireName(name);
        this.date = requireDate(date);
        this.themeId = requireTheme(themeId);
        this.timeId = requireTime(timeId);
    }

    public Reservation withId(Long generatedId) {
        return Reservation.builder()
                .id(generatedId)
                .name(this.name)
                .date(this.date)
                .themeId(this.themeId)
                .timeId(this.timeId)
                .build();
    }

    public void validateNotPast(LocalTime startAt, LocalDateTime currentDateTime) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, startAt);

        if (reservationDateTime.isBefore(currentDateTime)) {
            throw new InvalidReservationException("현재 시간보다 이전 시간으로 예약을 할 수 없습니다.");
        }
    }

    private static String requireName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidReservationException("이름은 비어있을 수 없습니다.");
        }
        return name;
    }

    private static LocalDate requireDate(LocalDate date) {
        if (date == null) {
            throw new InvalidReservationException("날짜는 비어있을 수 없습니다.");
        }
        return date;
    }

    private static Long requireTheme(Long themeId) {
        if (themeId == null || themeId <= 0) {
            throw new InvalidReservationException("테마ID는 올바른 값이어야 합니다.");
        }
        return themeId;
    }

    private static Long requireTime(Long timeId) {
        if (timeId == null || timeId <= 0) {
            throw new InvalidReservationException("시간ID는 올바른 값이어야 합니다.");
        }
        return timeId;
    }
}
