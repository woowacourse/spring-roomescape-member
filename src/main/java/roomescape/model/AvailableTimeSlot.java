package roomescape.model;

public record AvailableTimeSlot(
    TimeSlot timeSlot,
    boolean alreadyBooked
) {

}
