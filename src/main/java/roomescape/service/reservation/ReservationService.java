package roomescape.service.reservation;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.member.MemberDao;
import roomescape.dao.reservation.ReservationDao;
import roomescape.dao.reservation.ReservationThemeDao;
import roomescape.dao.reservation.ReservationTimeDao;
import roomescape.domain.member.MemberInfo;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationFactory;
import roomescape.domain.reservation.ReservationTheme;
import roomescape.domain.reservation.ReservationTime;
import roomescape.dto.reservation.AdminReservationRequest;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.dto.reservation.ReservationSearchRequest;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ReservationThemeDao reservationThemeDao;
    private final MemberDao memberDao;
    private final ReservationFactory reservationFactory;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao,
                              ReservationThemeDao reservationThemeDao, MemberDao memberDao,
                              ReservationFactory reservationFactory) {
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
        MemberInfo member = memberDao.findById(request.memberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        return getResponseAfterInsert(request.date(), request.timeId(), request.themeId(), member);
    }

    @Transactional
    public ReservationResponse insertUserReservation(ReservationRequest request, MemberInfo member) {
        return getResponseAfterInsert(request.date(), request.timeId(), request.themeId(), member);
    }

    private ReservationResponse getResponseAfterInsert(LocalDate date, Long timeId, Long themeId, MemberInfo member) {
        Reservation reservation = getReservation(date, timeId, themeId, member);
        Reservation inserted = reservationDao.insert(reservation);

        return new ReservationResponse(inserted);
    }

    private Reservation getReservation(LocalDate date, Long timeId, Long themeId, MemberInfo member) {
        validateDuplicate(date.toString(), timeId, themeId);
        ReservationTime time = reservationTimeDao.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));
        ReservationTheme theme = reservationThemeDao.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        return reservationFactory.createForAdd(date, time, theme, member);
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
        return reservationDao.searchReservation(request.themeId(), request.memberId(), request.dateFrom(),
                        request.dateTo()).stream()
                .map(ReservationResponse::new)
                .toList();
    }
}
