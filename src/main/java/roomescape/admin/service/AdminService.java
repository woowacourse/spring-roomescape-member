package roomescape.admin.service;

import org.springframework.stereotype.Service;
import roomescape.admin.domain.dto.AdminReservationRequestDto;
import roomescape.admin.domain.dto.AdminReservationResponseDto;
import roomescape.member.exception.UnauthorizedUserRoleException;
import roomescape.reservation.domain.dto.ReservationRequestDto;
import roomescape.reservation.domain.dto.ReservationResponseDto;
import roomescape.reservation.service.ReservationService;
import roomescape.user.domain.User;
import roomescape.user.domain.dto.UserResponseDto;
import roomescape.user.service.UserService;

@Service
public class AdminService {

    private final ReservationService reservationService;
    private final UserService userService;

    public AdminService(ReservationService reservationService, UserService userService) {
        this.reservationService = reservationService;
        this.userService = userService;
    }

    public AdminReservationResponseDto createReservation(AdminReservationRequestDto adminReservationRequestDto,
                                                         User admin) {
        User member = userService.findById(adminReservationRequestDto.memberId());
        if (!member.isMember()) {
            throw new UnauthorizedUserRoleException();
        }
        ReservationRequestDto reservationRequestDto = convertAdminReservationRequestDtoToReservationRequestDto(
                adminReservationRequestDto);
        ReservationResponseDto reservationResponseDto = reservationService.add(reservationRequestDto, admin);
        return convertReservationResponseDtoAndUserToAdminReservationResponseDto(reservationResponseDto, member);
    }

    private static ReservationRequestDto convertAdminReservationRequestDtoToReservationRequestDto(
            AdminReservationRequestDto adminReservationRequestDto) {
        return new ReservationRequestDto(adminReservationRequestDto.date(),
                adminReservationRequestDto.timeId(),
                adminReservationRequestDto.themeId());
    }

    private static AdminReservationResponseDto convertReservationResponseDtoAndUserToAdminReservationResponseDto(
            ReservationResponseDto reservationResponseDto, User user) {
        return AdminReservationResponseDto.from(reservationResponseDto, UserResponseDto.of(user));
    }
}
