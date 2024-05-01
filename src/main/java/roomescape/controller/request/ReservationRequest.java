package roomescape.controller.request;

import roomescape.exception.BadRequestException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ReservationRequest {

    private LocalDate date;
    private String name;
    private long timeId;

    public ReservationRequest(String date, String name, long timeId) {
        validateName(name);
        validateDate(date);
        try {
            this.date = LocalDate.parse(date);
        } catch (DateTimeParseException exception) {
            throw new BadRequestException("[ERROR] 유효하지 않은 요청입니다.");
        }
        this.name = name;
        this.timeId = timeId;
    }

    private ReservationRequest() {
    }

    private void validateDate(String date) {
        if (date == null || date.isEmpty()) {
            throw new BadRequestException("[ERROR] 유효하지 않은 요청입니다.");
        }
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
