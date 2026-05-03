package roomescape.reservation.service;

public record ReservationCommand(String name, String date, Long timeId) {
}
