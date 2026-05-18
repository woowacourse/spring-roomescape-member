package roomescape.domain;

import roomescape.exception.PastReservationTimeException;
import roomescape.exception.ReservationNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final Long timeId;
    private final Long themeId;

    private Reservation(Long id, String name, LocalDate date, Long timeId, Long themeId) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
    }

    public static Reservation from(Long id, String name, LocalDate date, Long timeId, Long themeId) {
        return new Reservation(id, name, date, timeId, themeId);
    }

    public static Reservation from(String name, LocalDate date, Long timeId, Long themeId) {
        return new Reservation(null, name, date, timeId, themeId);
    }

    public static void validateDeletion(int deleteCount) {
        if (deleteCount == 0) {
            throw new ReservationNotFoundException("해당 예약을 찾을 수 없습니다.");
        }
    }

    public void validateNotPast(LocalDateTime dateTime) {
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new PastReservationTimeException("이전 날짜는 선택하실 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getTimeId() {
        return timeId;
    }

    public Long getThemeId() {
        return themeId;
    }
}
