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
import roomescape.dto.request.ReservationSearchRequest;
import roomescape.dto.response.ReservationCreateResponse;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.InvalidReservationFilterException;
import roomescape.exception.ReservationDuplicateException;
import roomescape.support.auth.LoginMember;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;
    private final MemberService memberService;

    public ReservationService(final ReservationDao reservationDao, final ReservationTimeService reservationTimeService,
                              final ThemeService themeService, final MemberService memberService) {
        this.reservationDao = reservationDao;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
        this.memberService = memberService;
    }

    @Transactional
    public ReservationCreateResponse create(final ReservationCreateRequest reservationCreateRequest,
                                            final LoginMember loginMember) {
        final ReservationTime time = reservationTimeService.findById(reservationCreateRequest.timeId());
        final Theme theme = themeService.findById(reservationCreateRequest.themeId());
        final Member member = memberService.findById(loginMember.id());
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
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public void delete(final long id) {
        if (!reservationDao.existsById(id)) {
            throw new NoSuchElementException("예약이 존재하지 않습니다.");
        }
        reservationDao.delete(id);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findByThemeAndMemberAndDate(
            final ReservationSearchRequest reservationSearchRequest) {
        validateFilter(reservationSearchRequest);
        final List<Reservation> reservations = reservationDao.findByThemeAndMemberAndDate(
                reservationSearchRequest.themeId(),
                reservationSearchRequest.memberId(),
                reservationSearchRequest.dateFrom(),
                reservationSearchRequest.dateTo());

        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    private void validateFilter(final ReservationSearchRequest reservationSearchRequest) {
        if (reservationSearchRequest.dateFrom().isAfter(reservationSearchRequest.dateTo())) {
            throw new InvalidReservationFilterException("시작 날짜는 종료 날짜보다 이후일 수 없습니다.");
        }
        if (!memberService.existsById(reservationSearchRequest.memberId())) {
            throw new InvalidReservationFilterException("멤버가 존재하지 않습니다.");
        }
        if (!themeService.existsById(reservationSearchRequest.themeId())) {
            throw new InvalidReservationFilterException("테마가 존재하지 않습니다.");
        }
    }
}
