package roomescape.dto;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;

public class ReservationResponse {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTimeResponse time;

    private ReservationResponse(Long id, String name, LocalDate date, ReservationTimeResponse time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()));
    }

    public static List<ReservationResponse> from(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTimeResponse getTime() {
        return time;
    }
}
