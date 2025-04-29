package roomescape.dto;

import java.time.LocalDate;

public final class ReservationCreationRequest {

    private final String name;
    private final LocalDate date;
    private final Long timeId;

    public ReservationCreationRequest(String name, LocalDate date, Long timeId) {
        validate(name, date, timeId);
        this.name = name;
        this.date = date;
        this.timeId = timeId;
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

    private void validate(String name, LocalDate date, Long time) {
        validateName(name);
        validateDate(date);
        validateTime(time);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 이름은 빈 값이나 공백값을 허용하지 않습니다.");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("[ERROR] 날짜는 빈 값을 허용하지 않습니다.");
        }
    }

    private void validateTime(Long time) {
        if (time == null) {
            throw new IllegalArgumentException("[ERROR] 시간은 빈 값을 허용하지 않습니다.");
        }
    }
}
