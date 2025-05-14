package roomescape.admin.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.admin.domain.dto.AdminReservationRequestDto;
import roomescape.admin.domain.dto.SearchReservationRequestDto;
import roomescape.member.exception.UnauthorizedUserRoleException;
import roomescape.reservation.domain.dto.ReservationRequestDto;
import roomescape.reservation.domain.dto.ReservationResponseDto;
import roomescape.reservation.service.ReservationService;
import roomescape.user.domain.User;
import roomescape.user.service.UserService;

@Service
public class AdminService {

    private final ReservationService reservationService;
    private final UserService userService;

    public AdminService(ReservationService reservationService, UserService userService) {
        this.reservationService = reservationService;
        this.userService = userService;
    }

    public ReservationResponseDto createReservation(AdminReservationRequestDto requestDto) {
        User member = getUser(requestDto.memberId());
        ReservationRequestDto reservationRequestDto = convertAdminReservationRequestDtoToReservationRequestDto(
                requestDto);
        return reservationService.add(reservationRequestDto, member);
    }

    public List<ReservationResponseDto> searchReservations(SearchReservationRequestDto requestDto) {
        return reservationService.findReservationsByUserAndThemeAndFromAndTo(requestDto);
    }

    private User getUser(Long memberId) {
        User member = userService.findById(memberId);
        if (!member.isMember()) {
            throw new UnauthorizedUserRoleException();
        }
        return member;
    }

    private static ReservationRequestDto convertAdminReservationRequestDtoToReservationRequestDto(
            AdminReservationRequestDto adminReservationRequestDto) {
        return new ReservationRequestDto(adminReservationRequestDto.date(),
                adminReservationRequestDto.timeId(),
                adminReservationRequestDto.themeId());
    }
}
