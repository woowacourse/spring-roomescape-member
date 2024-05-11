package roomescape.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.domain.MemberInfo;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.dao.ReservationThemeDao;
import roomescape.reservation.dao.ReservationTimeDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationFactory;
import roomescape.reservation.domain.ReservationTheme;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.dto.reservation.ReservationRequest;
import roomescape.reservation.dto.reservation.ReservationResponse;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ReservationThemeDao reservationThemeDao;
    private final ReservationFactory reservationFactory;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao,
                              ReservationThemeDao reservationThemeDao, ReservationFactory reservationFactory) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.reservationThemeDao = reservationThemeDao;
        this.reservationFactory = reservationFactory;
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationDao.findAll().stream()
                .map(ReservationResponse::new)
                .toList();
    }

    @Transactional
    public ReservationResponse insertReservation(ReservationRequest request, MemberInfo member) {
        Reservation reservation = getReservation(request, member);
        Reservation inserted = reservationDao.insert(reservation, member);

        return new ReservationResponse(inserted);
    }

    private Reservation getReservation(ReservationRequest request, MemberInfo member) {
        validateDuplicate(request);
        ReservationTime time = reservationTimeDao.findById(request.timeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));
        ReservationTheme theme = reservationThemeDao.findById(request.themeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        return reservationFactory.createForAdd(request.name(), request.date(), time, theme, member);
    }

    private void validateDuplicate(ReservationRequest request) {
        if (reservationDao.hasSameReservation(request.date().toString(), request.timeId(), request.themeId())) {
            throw new IllegalArgumentException("이미 해당 시간에 예약이 존재합니다.");
        }
    }

    public void deleteReservation(Long id) {
        reservationDao.deleteById(id);
    }
}
