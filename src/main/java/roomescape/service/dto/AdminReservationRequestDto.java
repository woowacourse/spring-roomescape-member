package roomescape.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import roomescape.service.dto.validator.DateFormatConstraint;

public class AdminReservationRequestDto {

    @NotNull(message = "회원 아이디는 반드시 입력되어야 합니다.")
    @Positive(message = "회원 아이디는 자연수여야 합니다. ${validatedValue}은 사용할 수 없습니다.")
    private final Long memberId;

    @NotNull(message = "테마 아이디는 반드시 입력되어야 합니다.")
    @Positive(message = "테마 아이디는 자연수여야 합니다. ${validatedValue}은 사용할 수 없습니다.")
    private final Long themeId;

    @DateFormatConstraint
    private final String date;

    @NotNull(message = "예약 시간 아이디는 반드시 입력되어야 합니다.")
    @Positive(message = "예약 시간 아이디는 자연수여야 합니다. ${validatedValue}은 사용할 수 없습니다.")
    private final Long timeId;

    public AdminReservationRequestDto(Long memberId, Long themeId, String date, Long timeId) {
        this.memberId = memberId;
        this.themeId = themeId;
        this.date = date;
        this.timeId = timeId;
    }

    public CreateReservationDto toCreateReservation() {
        return new CreateReservationDto(memberId, themeId, date, timeId);
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getThemeId() {
        return themeId;
    }

    public String getDate() {
        return date;
    }

    public Long getTimeId() {
        return timeId;
    }
}
