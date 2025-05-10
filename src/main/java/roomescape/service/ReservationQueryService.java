package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;
import roomescape.dao.MemberDao;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.dto.response.ReservationPostResponse;
import roomescape.entity.Reservation;

@Component
public class ReservationQueryService {

    private final ReservationDao reservationDao;
    private final MemberDao memberDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationQueryService(ReservationDao reservationDao, MemberDao memberDao,
                                   ReservationTimeDao reservationTimeDao,
                                   ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
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
