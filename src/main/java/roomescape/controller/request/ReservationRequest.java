package roomescape.controller.request;

import roomescape.exception.BadRequestException;

import java.time.LocalDate;

public class ReservationRequest {

    private final LocalDate date;
    private final String name;
    private final long timeId;

    public ReservationRequest(LocalDate date, String name, long timeId) {
        validateName(name);
        this.date = date;
        this.name = name;
        this.timeId = timeId;
    }

    private void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("[ERROR] 유효하지 않은 요청입니다.");
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public long getTimeId() {
        return timeId;
    }
}
