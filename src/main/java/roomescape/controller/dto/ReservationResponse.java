package roomescape.controller.dto;

import roomescape.domain.Reservation;

public class ReservationResponse {
    private final long id;
    private final String name;
    private final String date;
    private final ReservationTimeResponse time;

    public ReservationResponse(long id, String name, String date, ReservationTimeResponse time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public static ReservationResponse toDto(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getName(),
                reservation.getDate().getDate().toString(),
                ReservationTimeResponse.toDto(reservation.getTime()));
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public ReservationTimeResponse getTime() {
        return time;
    }
}
