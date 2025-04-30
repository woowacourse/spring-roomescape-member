package roomescape.reservation.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class ReservationRequest {

//    @FutureOrPresent //todo: 여기서 검증하는 것이 맞을까?
    @NotNull
    private final LocalDate date;

    @NotBlank
    @Size(max = 10)
    private final String name;

    @NotNull
    private final Long themeId;

    @NotNull
    private final Long timeId;

    public ReservationRequest(final LocalDate date, final String name, final Long themeId, final Long timeId) {
        this.date = date;
        this.name = name;
        this.themeId = themeId;
        this.timeId = timeId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public Long getTimeId() {
        return timeId;
    }

    public Long getThemeId() {
        return themeId;
    }
}
