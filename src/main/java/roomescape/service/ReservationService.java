package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.reservation.ReservationDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationCreateRequest;
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

    public ReservationService(final ReservationDao reservationDao,
                              final ReservationTimeService reservationTimeService, final ThemeService themeService) {
        this.reservationDao = reservationDao;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
    }

    public ReservationCreateResponse create(ReservationCreateRequest reservationCreateRequest) {
        ReservationTime time = reservationTimeService.findById(reservationCreateRequest.timeId());
        Theme theme = themeService.findById(reservationCreateRequest.themeId());
        Reservation reservation = Reservation.create(
                reservationCreateRequest.name(),
                reservationCreateRequest.getLocalDate(),
                time,
                theme);

        if (reservationDao.findByDateAndTime(reservation).isPresent()) {
            throw new ReservationDuplicateException("이미 존재하는 예약입니다.");
        }
        return ReservationCreateResponse.from(reservationDao.create(reservation));
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(reservation -> new ReservationResponse(
                        reservation.getId(),
                        reservation.getName(),
                        reservation.getDate(),
                        ReservationTimeResponse.from(reservation.getTime()),
                        ThemeResponse.from(reservation.getTheme())
                ))
                .toList();
    }

    public void delete(final Long id) {
        reservationDao.delete(id);
    }
}
