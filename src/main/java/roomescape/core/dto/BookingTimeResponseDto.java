package roomescape.core.dto;

public class BookingTimeResponseDto {
    private Long id;
    private String startAt;
    private boolean alreadyBooked;


    public BookingTimeResponseDto() {
    }

    public BookingTimeResponseDto(final Long id, final String startAt, final boolean alreadyBooked) {
        this.id = id;
        this.startAt = startAt;
        this.alreadyBooked = alreadyBooked;
    }

    public Long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }

    public boolean isAlreadyBooked() {
        return alreadyBooked;
    }
}
