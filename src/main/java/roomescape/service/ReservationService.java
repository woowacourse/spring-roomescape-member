package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.ReservationsWithTotalPageResponse;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao,
                              ReservationTimeDao reservationTimeDao,
                              ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public ReservationResponse addReservation(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = reservationTimeDao.findById(reservationRequest.timeId())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 시간입니다."));
        Theme theme = themeDao.findById(reservationRequest.themeId())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 테마입니다."));
        Reservation reservation = reservationRequest.toEntityWithReservationTime(reservationTime, theme);
        LocalDate reservationDate = reservation.getDate();
        if (!reservationDate.isAfter(LocalDate.now())) {
            throw new IllegalStateException("하루 전 까지 예약 가능합니다.");
        }
        if (reservationDao.isExistByThemeIdAndTimeIdAndDate(
                reservationRequest.themeId(),
                reservationRequest.timeId(),
                reservationRequest.date())
        ) {
            throw new IllegalStateException("이미 해당 시간에 예약이 존재합니다.");
        }
        Reservation savedReservation = reservationDao.save(reservation);
        return ReservationResponse.fromEntity(savedReservation);
    }

    public void deleteReservation(Long id) {
        boolean isDeleted = reservationDao.deleteById(id);
        if (!isDeleted) {
            throw new IllegalArgumentException("해당하는 id가 없습니다");
        }
    }

    public ReservationsWithTotalPageResponse getReservationsByPage(int page) {
        int totalReservations = reservationDao.countTotalReservation();
        int totalPage = totalReservations % 10 == 0 ?
                totalReservations / 10 : (totalReservations / 10) + 1;
        if (page < 1 || page > totalPage) {
            throw new IllegalArgumentException("해당하는 페이지가 없습니다");
        }
        int start = (page - 1) * 10 + 1;
        int end = start + 10 - 1;
        List<ReservationResponse> reservationResponses = reservationDao.findReservationsWithPage(start, end)
                .stream()
                .map(ReservationResponse::fromEntity)
                .toList();
        return new ReservationsWithTotalPageResponse(totalPage, reservationResponses);
    }
}
