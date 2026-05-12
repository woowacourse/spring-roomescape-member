package roomescape.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;

@Service
public class ReservationService {
    private static final String LOCATION = "Asia/Seoul";

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public List<ReservationResponse> findAll() {
        List<Reservation> reservations = reservationDao.findAll();

        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse save(ReservationRequest request, Instant clockInstant) {
        ReservationTime time = reservationTimeDao.findTimeById(request.timeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));

        LocalDateTime now = LocalDateTime.ofInstant(clockInstant, ZoneId.of(LOCATION));
        LocalDateTime requestDateTime = LocalDateTime.of(request.date(), time.getStartAt());
        if (requestDateTime.isBefore(now)) {
            throw new IllegalArgumentException("이미 지난 시간입니다.");
        }

        Theme theme = themeDao.findThemeById(request.themeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        if (reservationDao.existsBy(request.date(), theme, time)) {
            throw new IllegalArgumentException("이미 존재하는 예약 건입니다.");
        }

        Reservation reservation = new Reservation(
                request.name(),
                request.date(),
                time,
                theme
        );

        Reservation saved = reservationDao.save(reservation);

        return ReservationResponse.from(saved);
    }

    public void delete(Long id) {
        reservationDao.delete(id);
    }
}
