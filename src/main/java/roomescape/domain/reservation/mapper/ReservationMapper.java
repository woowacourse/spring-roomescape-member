package roomescape.domain.reservation.mapper;

import java.time.LocalDate;
import roomescape.domain.reservation.dto.response.ReservationByNameResponseDto;
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
            ThemeMapper.toReservationResponseDto(reservation.getTheme()), status, getMessage(status));
    }

    private static ReservationStatus getStatus(Reservation reservation) {
        if (reservation.getDate().isBefore(LocalDate.now())) {
            return ReservationStatus.LOCKED;
        }

        if (reservation.getTime().getDeletedAt() != null || reservation.getTheme().getDeletedAt() != null) {
            return ReservationStatus.EDIT_RECOMMENDED;
        }

        return ReservationStatus.EDITABLE;
    }

    private static String getMessage(ReservationStatus status) {
        if (status == ReservationStatus.EDITABLE) {
            return "";
        }

        if (status == ReservationStatus.EDIT_RECOMMENDED) {
            return "현재 예약의 시간 또는 테마가 더 이상 제공되지 않습니다. 다른 예약 정보로 수정해주세요.";
        }

        return "지난 예약은 수정하거나 삭제할 수 없습니다.";
    }

    public static ReservationCreateResponseDto toCreateResponseDto(Reservation reservation) {
        return new ReservationCreateResponseDto(reservation.getId(), reservation.getName(), reservation.getDate(),
            reservation.getTime().getId(), reservation.getTheme().getId());
    }
}
