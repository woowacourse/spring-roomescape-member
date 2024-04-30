package roomescape.core.dto;

public class ReservationRequestDto {
    private String date;
    private String name;
    private Long timeId;

    public ReservationRequestDto() {
    }

    public ReservationRequestDto(final String date, final String name, final Long timeId) {
        this.date = date;
        this.name = name;
        this.timeId = timeId;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public Long getTimeId() {
        return timeId;
    }
}
