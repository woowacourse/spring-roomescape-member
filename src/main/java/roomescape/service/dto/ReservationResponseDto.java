package roomescape.service.dto;

import roomescape.domain.Reservation;

public class ReservationResponseDto {

    private final long id;
    private final String name;
    private final ThemeResponseDto theme;
    private final String date;
    private final ReservationTimeResponseDto time;

    public ReservationResponseDto(long id, String name, ThemeResponseDto theme, String date,
                                  ReservationTimeResponseDto time) {
        this.id = id;
        this.name = name;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public ReservationResponseDto(Reservation reservation) {
        this(reservation.getId(),
                reservation.getName(),
                new ThemeResponseDto(reservation.getTheme()),
                reservation.getDate().toString(),
                new ReservationTimeResponseDto(reservation.getReservationTime()));
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

    public ReservationTimeResponseDto getTime() {
        return time;
    }

    public ThemeResponseDto getTheme() {
        return theme;
    }
}
