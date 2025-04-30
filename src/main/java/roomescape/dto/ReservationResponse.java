package roomescape.dto;

import java.time.LocalDate;
import java.util.List;
import roomescape.entity.ReservationEntity;

public class ReservationResponse {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTimeResponse time;
    private final ThemeResponse theme;

    private ReservationResponse(Long id, String name, LocalDate date, ReservationTimeResponse time,
                                ThemeResponse theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static ReservationResponse from(ReservationEntity reservationEntity) {
        return new ReservationResponse(reservationEntity.id(), reservationEntity.name(), reservationEntity.date(),
                ReservationTimeResponse.from(reservationEntity.time()), ThemeResponse.from(reservationEntity.theme()));
    }

    public static List<ReservationResponse> from(List<ReservationEntity> reservations) {
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

    public ThemeResponse getTheme() {
        return theme;
    }
}
