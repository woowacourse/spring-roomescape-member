package roomescape.dto;

import java.time.LocalDate;

public class BookableTimesRequest {

    private LocalDate date;
    private Long themeId;

    public BookableTimesRequest(LocalDate date, Long themeId) {
        this.date = date;
        this.themeId = themeId;
    }

    public BookableTimesRequest() {
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getThemeId() {
        return themeId;
    }
}
