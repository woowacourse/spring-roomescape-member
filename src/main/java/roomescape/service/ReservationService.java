package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.IdNotFoundException;

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

    public ReservationResponse find(String name) {
        Reservation response = reservationDao.findByName(name);
        return ReservationResponse.from(response);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll()
            .stream()
            .map(ReservationResponse::from)
            .collect(Collectors.toList());
    }

    public ReservationResponse save(ReservationRequest request) {
        ReservationTime time = reservationTimeDao.findTimeById(request.timeId());

        if (time == null) {
            throw new IdNotFoundException("요청하신 시간 ID가 존재하지 않습니다.");
        }

        Theme theme = themeDao.findThemeById(request.themeId());

        if (theme == null) {
            throw new IdNotFoundException("요청하신 테마 ID가 존재하지 않습니다.");
        }

        if (time.getStartAt().isBefore(LocalTime.now()) && request.date().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("이미 지난 시간/날짜는 예약할 수 없습니다.");
        }

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
