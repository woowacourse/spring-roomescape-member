package roomescape.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.reservation.ReservationDao;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationAdminCreateRequest;
import roomescape.dto.request.ReservationCreateRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.dto.response.ReservationCreateResponse;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.ThemeResponse;
import roomescape.exception.ReservationDuplicateException;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;
    private final MemberService memberService;

    public ReservationService(final ReservationDao reservationDao,
                              final ReservationTimeService reservationTimeService, final ThemeService themeService,
                              final MemberService memberService) {
        this.reservationDao = reservationDao;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
        this.memberService = memberService;
    }

    @Transactional
    public ReservationCreateResponse create(final ReservationCreateRequest reservationCreateRequest,
                                            final Member member) {
        final ReservationTime time = reservationTimeService.findById(reservationCreateRequest.timeId());
        final Theme theme = themeService.findById(reservationCreateRequest.themeId());
        final Reservation reservation = Reservation.create(
                reservationCreateRequest.date(),
                time,
                theme,
                member);

        if (reservationDao.findByThemeAndDateAndTime(reservation).isPresent()) {
            throw new ReservationDuplicateException("이미 존재하는 예약입니다.");
        }
        return ReservationCreateResponse.from(reservationDao.create(reservation));
    }

    @Transactional
    public ReservationCreateResponse create(final ReservationAdminCreateRequest reservationCreateRequest) {
        final ReservationTime time = reservationTimeService.findById(reservationCreateRequest.timeId());
        final Theme theme = themeService.findById(reservationCreateRequest.themeId());
        final Member member = memberService.findById(reservationCreateRequest.memberId());
        final Reservation reservation = Reservation.create(
                reservationCreateRequest.date(),
                time,
                theme,
                member);

        if (reservationDao.findByThemeAndDateAndTime(reservation).isPresent()) {
            throw new ReservationDuplicateException("이미 존재하는 예약입니다.");
        }
        return ReservationCreateResponse.from(reservationDao.create(reservation));
    }

    @Transactional(readOnly = true)
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

    @Transactional
    public void delete(final long id) {
        if (!reservationDao.existsById(id)) {
            throw new NoSuchElementException("예약이 존재하지 않습니다.");
        }
        reservationDao.delete(id);
    }
}
