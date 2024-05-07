package roomescape.controller.request;

import roomescape.exception.BadRequestException;

import java.time.LocalDate;

public class ReservationRequest {

    private final String name;
    private final LocalDate date;
    private final long timeId;
    private final long themeId;

    public ReservationRequest(String name, LocalDate date, Long timeId, Long themeId) {
        validate(name, date, timeId, themeId);
        this.name = name;
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
    }

    private void validate(String name, LocalDate date, Long timeId, Long themeId) {
        validateName(name);
        validateNull(date);
        validateNull(timeId);
        validateNull(themeId);
    }

    private void validateNull(Object value) {
        if (value == null) {
            throw new BadRequestException("[ERROR] 요청된 데이터에 null 혹은 비어있는 값이 존재합니다.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("[ERROR] 요청된 데이터에 null 혹은 비어있는 값이 존재합니다.");
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

    public long getThemeId() {
        return themeId;
    }
}
