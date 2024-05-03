package roomescape.core.dto;

public class ReservationTimeRequestDto {
    private String startAt;

    public ReservationTimeRequestDto() {
    }

    public ReservationTimeRequestDto(final String startAt) {
        this.startAt = startAt;
    }

    public String getStartAt() {
        return startAt;
    }
}
