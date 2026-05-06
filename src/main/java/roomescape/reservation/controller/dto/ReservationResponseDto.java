package roomescape.reservation.controller.dto;

import java.time.LocalDate;

import roomescape.reservation.domain.Reservation;

public class ReservationResponseDto {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTimeResponseDto time;

    public ReservationResponseDto(Long id, String name, LocalDate date, ReservationTimeResponseDto time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public static ReservationResponseDto from(Reservation reservation) {
        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResponseDto.from(reservation.getTime())
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
}
