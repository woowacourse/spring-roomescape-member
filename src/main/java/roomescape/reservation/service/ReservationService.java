package roomescape.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.MemberInfo;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.dao.ReservationThemeDao;
import roomescape.reservation.dao.ReservationTimeDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationFactory;
import roomescape.reservation.domain.ReservationTheme;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.dto.reservation.AdminReservationRequest;
import roomescape.reservation.dto.reservation.ReservationRequest;
import roomescape.reservation.dto.reservation.ReservationResponse;
import roomescape.reservation.dto.reservation.ReservationSearchRequest;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ReservationThemeDao reservationThemeDao;
    private final MemberDao memberDao;
    private final ReservationFactory reservationFactory;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao,
                              ReservationThemeDao reservationThemeDao, MemberDao memberDao, ReservationFactory reservationFactory) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.reservationThemeDao = reservationThemeDao;
        this.memberDao = memberDao;
        this.reservationFactory = reservationFactory;
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationDao.findAll().stream()
                .map(ReservationResponse::new)
                .toList();
    }

    @Transactional
    public ReservationResponse insertAdminReservation(AdminReservationRequest request) {
        Reservation reservation = getAdminReservation(request);

        return new ReservationResponse(insert(reservation));
    }

    @Transactional
    public ReservationResponse insertReservation(ReservationRequest request, MemberInfo member) {
        Reservation reservation = getMemberReservation(request, member);

        return new ReservationResponse(insert(reservation));
    }

    private Reservation insert(Reservation reservation) {
        return reservationDao.insert(reservation);
    }

    private Reservation getMemberReservation(ReservationRequest request, MemberInfo member) {
        validateDuplicate(request.date().toString(), request.timeId(), request.themeId());
        ReservationTime time = reservationTimeDao.findById(request.timeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));
        ReservationTheme theme = reservationThemeDao.findById(request.themeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        return reservationFactory.createForAdd(request.date(), time, theme, member);
    }

    private Reservation getAdminReservation(AdminReservationRequest request) {
        validateDuplicate(request.date().toString(), request.timeId(), request.themeId());
        ReservationTime time = reservationTimeDao.findById(request.timeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));
        ReservationTheme theme = reservationThemeDao.findById(request.themeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
        MemberInfo member = memberDao.findById(request.memberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        return reservationFactory.createForAdd(request.date(), time, theme, member);
    }

    private void validateDuplicate(String date, Long timeId, Long themeId) {
        if (reservationDao.hasSameReservation(date, timeId, themeId)) {
            throw new IllegalArgumentException("이미 해당 시간에 예약이 존재합니다.");
        }
    }

    public void deleteReservation(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> searchReservation(ReservationSearchRequest request) {
        if (request.dateTo().isBefore(request.dateFrom())) {
            throw new IllegalArgumentException("종료 날짜는 시작 날짜 이전일 수 없습니다.");
        }
        return reservationDao.searchReservation(request.themeId(), request.memberId(), request.dateFrom(), request.dateTo()).stream()
                .map(ReservationResponse::new)
                .toList();
    }
}
