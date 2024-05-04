package roomescape.controller.request;

import java.time.LocalDate;

import roomescape.exception.BadRequestException;

public class ReservationRequest {

    private final String name;
    private final LocalDate date;
    private final long timeId;
    private final long themeId;

    public ReservationRequest(String name, LocalDate date, Long timeId, Long themeId) {
        validateName(name);
        validateNull(date);
        validateTimeIdNull(themeId);
        validateThemeIdNull(themeId);
        this.name = name;
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
    }

    private void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("이름(name)이 [%s]일 수 없습니다.".formatted(name));
        }
    }

    private void validateNull(LocalDate date) {
        if (date == null) {
            throw new BadRequestException("날짜(date)가 [%s]일 수 없습니다.".formatted(date));
        }
    }

    // todo numberFormatException 예외 처리
    private void validateTimeIdNull(Long timeId) {
        if (timeId == null) {
            throw new BadRequestException("아이디(timeId)가 [%s]일 수 없습니다.".formatted(timeId));
        }
    }

    private void validateThemeIdNull(Long themeId) {
        if (themeId == null) {
            throw new BadRequestException("아이디(themeId)가 [%s]일 수 없습니다.".formatted(themeId));
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
