package roomescape.domain.reservation.mapper;

import java.time.LocalDate;
import roomescape.domain.reservation.dto.response.ReservationByNameResponseDto;
import roomescape.domain.reservation.dto.response.ReservationCancelResponseDto;
import roomescape.domain.reservation.dto.response.ReservationCreateResponseDto;
import roomescape.domain.reservation.dto.response.ReservationResponseDto;
import roomescape.domain.reservation.dto.response.ReservationStatus;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.theme.mapper.ThemeMapper;
import roomescape.domain.time.mapper.TimeMapper;

public final class ReservationMapper {

    private ReservationMapper() {

    }

    public static ReservationResponseDto toResponseDto(Reservation reservation) {
        return new ReservationResponseDto(reservation.getId(), reservation.getName(), reservation.getDate(),
            TimeMapper.toResponseDto(reservation.getTime()), ThemeMapper.toResponseDto(reservation.getTheme()));
    }

    public static ReservationByNameResponseDto toByNameResponseDto(Reservation reservation) {
        ReservationStatus status = getStatus(reservation);
        return new ReservationByNameResponseDto(reservation.getId(), reservation.getName(), reservation.getDate(),
            TimeMapper.toReservationResponseDto(reservation.getTime()),
            ThemeMapper.toReservationResponseDto(reservation.getTheme()), status, status.getMessage());
    }

    private static ReservationStatus getStatus(Reservation reservation) {
        if (reservation.getCanceledAt() != null) {
            return ReservationStatus.CANCELED;
        }

        if (reservation.getDate().isBefore(LocalDate.now())) {
            return ReservationStatus.LOCKED;
        }

        if (reservation.getTime().getDeletedAt() != null || reservation.getTheme().getDeletedAt() != null) {
            return ReservationStatus.EDIT_RECOMMENDED;
        }

        return ReservationStatus.EDITABLE;
    }

    public static ReservationCreateResponseDto toCreateResponseDto(Reservation reservation) {
        return new ReservationCreateResponseDto(reservation.getId(), reservation.getName(), reservation.getDate(),
            reservation.getTime().getId(), reservation.getTheme().getId());
    }

    public static ReservationCancelResponseDto toCancelResponseDto(Reservation reservation) {
        return new ReservationCancelResponseDto(reservation.getId(), reservation.getName(), reservation.getDate(),
            reservation.getTime().getId(), reservation.getTheme().getId(), reservation.getCanceledAt());
    }
}
