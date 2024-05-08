package roomescape.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalTime;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationName;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.ThemeName;
import roomescape.service.dto.validator.DateFormatConstraint;

public class ReservationRequestDto {

    @NotBlank(message = "이름은 반드시 입력되어야 합니다.")
    private final String name;

    @NotNull(message = "테마 아이디는 반드시 입력되어야 합니다.")
    @Positive(message = "테마 아이디는 자연수여야 합니다. ${validatedValue}은 사용할 수 없습니다.")
    private final Long themeId;

    @DateFormatConstraint
    private final String date;

    @NotNull(message = "예약 시간 아이디는 반드시 입력되어야 합니다.")
    @Positive(message = "예약 시간 아이디는 자연수여야 합니다. ${validatedValue}은 사용할 수 없습니다.")
    private final Long timeId;

    public ReservationRequestDto(String name, Long themeId, String date, Long timeId) {
        this.name = name;
        this.themeId = themeId;
        this.date = date;
        this.timeId = timeId;
    }

    public Reservation toReservation() {
        return new Reservation(
                null,
                new ReservationName(name),
                new Theme(themeId, (ThemeName) null, null, null),
                new ReservationDate(date),
                new ReservationTime(timeId, (LocalTime) null)
        );
    }

    public String getName() {
        return name;
    }

    public Long getThemeId() {
        return themeId;
    }

    public String getDate() {
        return date;
    }

    public long getTimeId() {
        return timeId;
    }
}
