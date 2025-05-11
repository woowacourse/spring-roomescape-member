package roomescape.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.exception.ConflictException;
import roomescape.exception.ExceptionCause;
import roomescape.exception.NotFoundException;
import roomescape.member.domain.Member;
import roomescape.member.dto.MemberResponse;
import roomescape.member.service.JwtUtil;
import roomescape.member.service.MemberService;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationCreateRequest;
import roomescape.reservation.dto.ReservationCreateResponse;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.dto.ReservationTimeResponse;
import roomescape.reservationtime.service.ReservationTimeService;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.service.ThemeService;

@Service
public class ReservationService {

    private final JwtUtil jwtUtil;
    private final ReservationDao reservationDao;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;
    private final MemberService memberService;

    public ReservationService(JwtUtil jwtUtil, final ReservationDao reservationDao,
                              final ReservationTimeService reservationTimeService, final ThemeService themeService,
                              MemberService memberService) {
        this.jwtUtil = jwtUtil;
        this.reservationDao = reservationDao;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
        this.memberService = memberService;
    }

    public ReservationCreateResponse create(String token, ReservationCreateRequest reservationCreateRequest) {
        Reservation reservation = createReservationWithoutId(token, reservationCreateRequest);
        if (reservationDao.findByThemeAndDateAndTime(reservation).isPresent()) {
            throw new ConflictException(ExceptionCause.RESERVATION_DUPLICATE);
        }
        Reservation savedReservation = reservationDao.create(reservation);
        return ReservationCreateResponse.from(savedReservation);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(reservation -> new ReservationResponse(
                        reservation.getId(),
                        reservation.getDate(),
                        ReservationTimeResponse.from(reservation.getTime()),
                        ThemeResponse.from(reservation.getTheme()),
                        MemberResponse.from(reservation.getMember())
                ))
                .toList();
    }

    private Reservation createReservationWithoutId(String token, ReservationCreateRequest reservationCreateRequest) {
        ReservationTime time = reservationTimeService.findById(reservationCreateRequest.timeId());
        Theme theme = themeService.findById(reservationCreateRequest.themeId());
        Member member = memberService.findById(jwtUtil.getMemberIdFromToken(token));
        return Reservation.create(
                reservationCreateRequest.date(),
                member,
                time,
                theme);
    }

    public void delete(final Long id) {
        if (!reservationDao.existById(id)) {
            throw new NotFoundException(ExceptionCause.RESERVATION_NOTFOUND);
        }
        reservationDao.delete(id);
    }
}
