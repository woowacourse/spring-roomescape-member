package roomescape.reservation.controller.dto;

import java.time.LocalDate;

import roomescape.reservation.domain.Reservation;
import roomescape.theme.controller.dto.ThemeResponseDto;

public class ReservationResponseDto {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTimeResponseDto time;
    private final ThemeResponseDto theme;

    public ReservationResponseDto(Long id, String name, LocalDate date, ReservationTimeResponseDto time,
                                  ThemeResponseDto theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static ReservationResponseDto from(Reservation reservation) {
        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResponseDto.from(reservation.getTime()),
                ThemeResponseDto.from(reservation.getTheme())
        );
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

    public ReservationTimeResponseDto getTime() {
        return time;
    }

    public ThemeResponseDto getTheme() {
        return theme;
    }
}
