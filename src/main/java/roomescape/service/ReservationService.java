package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;
import roomescape.repository.ThemeDao;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public ReservationResponse save(ReservationRequest request) {
        ReservationTime time = reservationTimeDao.findById(request.timeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다."));
        Theme theme = themeDao.findById(request.themeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
        Reservation.validate(request.name(), request.date(), time);
        if (reservationDao.existsByDateAndTimeIdAndThemeId(request.date(), request.timeId(), request.themeId())) {
            throw new IllegalArgumentException("이미 예약된 시간입니다.");
        }
        Long id = reservationDao.save(request.name(), request.date(), request.timeId(), request.themeId());
        return ReservationResponse.from(new Reservation(id, request.name(), request.date(), time, theme), theme);
    }

    public void delete(Long id) {
        reservationDao.delete(id);
    }

    public List<ReservationResponse> findAllByName(String username) {
        return reservationDao.findByName(username).stream()
                .map(r -> ReservationResponse.from(r, r.getTheme()))
                .toList();
    }
}
