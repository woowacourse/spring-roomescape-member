package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;
import roomescape.reservationTime.domain.ReservationTime;

public class Reservation {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Long themeId;

    private static final int MAX_NAME_LENGTH = 20;

    public Reservation(String name, LocalDate date, ReservationTime time, Long themeId) {
        this(null, name, date, time, themeId);
    }

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Long themeId) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.themeId = themeId;
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

    public ReservationTime getTime() {
        return time;
    }

    public Long getThemeId() {
        return themeId;
    }

    public boolean isPast(LocalDateTime now){
        LocalDate nowDate = now.toLocalDate();
        LocalTime nowTime = now.toLocalTime();

        if (date.isBefore(nowDate)) return true;
        if (date.equals(nowDate) && time.isBefore(nowTime)) return true;
        return false;
    }

    public boolean isOwnerBy(String name) {
        return name.equals(this.name);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()){
            throw new BusinessException(ErrorCode.INVALID_RESERVATION_NAME);
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new BusinessException(ErrorCode.INVALID_RESERVATION_NAME);
        }
    }
}
