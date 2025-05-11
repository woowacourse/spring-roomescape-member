package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dto.response.ReservationPostResponse;
import roomescape.entity.Reservation;

@Service
public class ReservationQueryService {

    private final ReservationDao reservationDao;

    public ReservationQueryService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public List<ReservationPostResponse> findAllReservations() {
        return reservationDao.findAll().stream()
                .map(ReservationPostResponse::new)
                .toList();
    }

    public List<ReservationPostResponse> searchReservations(Long themeId, Long memberId, LocalDate dateFrom,
                                                            LocalDate dateTo) {
        List<Reservation> reservations = reservationDao.findByThemeAndMemberAndDate(themeId, memberId,
                dateFrom, dateTo);
        return reservations.stream()
                .map(ReservationPostResponse::new)
                .toList();
    }
}
