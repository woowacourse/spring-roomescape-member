package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.exception.ConflictException;
import roomescape.exception.ExceptionCause;
import roomescape.exception.UnauthorizedException;
import roomescape.member.domain.Visitor;
import roomescape.member.dto.MemberResponse;
import roomescape.member.service.MemberService;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.AdminReservationRequest;
import roomescape.reservation.dto.ReservationCreateResponse;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservationtime.dto.ReservationTimeResponse;
import roomescape.reservationtime.service.ReservationTimeService;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.service.ThemeService;

@Service
public class AdminReservationService {

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
        if (!visitor.isAdmin()) {
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

    public List<ReservationResponse> findByFilter(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
        //TODO DB 레벨에서 거르기
        return reservationDao.findAll().stream()
                .filter(reservation -> themeId == null || reservation.getTheme().getId().equals(themeId))
                .filter(reservation -> memberId == null || reservation.getMember().getId().equals(memberId))
                .filter(reservation -> {
                    LocalDate date = reservation.getDate();
                    return (dateFrom == null || !date.isBefore(dateFrom)) &&
                            (dateTo == null || !date.isAfter(dateTo));
                })
                .map(reservation -> new ReservationResponse(
                        reservation.getId(),
                        reservation.getDate(),
                        ReservationTimeResponse.from(reservation.getTime()),
                        ThemeResponse.from(reservation.getTheme()),
                        MemberResponse.from(reservation.getMember())))
                .toList();
    }
}
