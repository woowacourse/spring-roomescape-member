package roomescape.admin;

import org.springframework.stereotype.Service;
import roomescape.exception.ConflictException;
import roomescape.exception.ExceptionCause;
import roomescape.exception.UnauthorizedException;
import roomescape.member.domain.Visitor;
import roomescape.member.domain.Role;
import roomescape.member.service.MemberService;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationCreateResponse;
import roomescape.reservationtime.service.ReservationTimeService;
import roomescape.theme.service.ThemeService;

@Service
public class AdminReservationService {

    private static final Role ADMIN_ROLE = new Role(2L, "admin");
    private final ReservationDao reservationDao;
    private final MemberService memberService;
    private final ThemeService themeService;
    private final ReservationTimeService reservationTimeService;

    public AdminReservationService(ReservationDao reservationDao, MemberService memberService,
                                   ThemeService themeService, ReservationTimeService reservationTimeService) {
        this.reservationDao = reservationDao;
        this.memberService = memberService;
        this.themeService = themeService;
        this.reservationTimeService = reservationTimeService;
    }


    public ReservationCreateResponse create(Visitor visitor, AdminReservationRequest adminReservationRequest) {
        if(!visitor.isAdmin()) {
            throw new UnauthorizedException(ExceptionCause.MEMBER_UNAUTHORIZED);
        }
        Reservation reservationWithoutId = Reservation.create(
                adminReservationRequest.date(),
                memberService.findById(adminReservationRequest.memberId()),
                reservationTimeService.findById(adminReservationRequest.timeId()),
                themeService.findById(adminReservationRequest.themeId()));
        if (reservationDao.findByThemeAndDateAndTime(reservationWithoutId).isPresent()) {
            throw new ConflictException(ExceptionCause.RESERVATION_DUPLICATE);
        }
        Reservation reservation = reservationDao.create(reservationWithoutId);
        return ReservationCreateResponse.from(reservation);
    }
}
