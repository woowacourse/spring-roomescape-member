package roomescape.domain.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ReservationUpdateRequest {

    @NotBlank(message = "이름은 필수입니다.")
    private final String name;

    @NotNull(message = "예약 날짜는 필수입니다.")
    private final LocalDate date;

    @NotNull(message = "예약 시간은 필수입니다.")
    private final Long timeId;

    @NotNull(message = "테마는 필수입니다.")
    private final Long themeId;

    public ReservationUpdateRequest(String name, LocalDate date, Long timeId, Long themeId) {
        this.name = name;
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
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
