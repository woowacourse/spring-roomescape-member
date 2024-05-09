package roomescape.domain.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.domain.reservation.dao.ReservationDao;
import roomescape.domain.reservation.dao.TimeDao;
import roomescape.domain.reservation.domain.Reservation;
import roomescape.domain.reservation.domain.ReservationTime;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservation.dto.ReservationSaveRequest;
import roomescape.domain.reservation.mapper.ReservationMapper;
import roomescape.domain.theme.dao.ThemeDao;
import roomescape.domain.theme.domain.Theme;
import roomescape.global.exception.RoomEscapeException;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationMapper reservationMapper = new ReservationMapper();
    private final ReservationDao reservationDao;
    private final TimeDao timeDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao, TimeDao timeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.timeDao = timeDao;
        this.themeDao = themeDao;
    }

    public List<ReservationResponse> findAllReservations() {
        List<Reservation> reservations = reservationDao.findAll();
        return reservations.stream()
                .map(reservationMapper::mapToResponse)
                .toList();
    }

    public ReservationResponse saveReservation(ReservationSaveRequest request, String name) {
        ReservationTime time = timeDao.findById(request.timeId())
                .orElseThrow(() -> new RoomEscapeException("[ERROR] 예약 시간을 찾을 수 없습니다"));

        if (checkPastTime(request, time)) {
            throw new RoomEscapeException("[ERROR] 이미 지난 시간입니다.");
        }

        if (reservationDao.existByDateTimeTheme(request.date(), time.getStartAt(), request.themeId())) {
            throw new RoomEscapeException("[ERROR] 같은 날짜, 테마, 시간에 중복된 예약을 생성할 수 없습니다.");
        }
        Theme theme = themeDao.findById(request.themeId())
                .orElseThrow(() -> new RoomEscapeException("[ERROR] 테마를 찾을 수 없습니다"));

        Reservation reservation = reservationMapper.mapToReservation(request, name, time, theme);
        Long saveId = reservationDao.save(reservation);
        return reservationMapper.mapToResponse(saveId, reservation);
    }

    private boolean checkPastTime(ReservationSaveRequest request, ReservationTime time) {
        LocalDate now = LocalDate.now();
        return now.isAfter(request.date()) || (now.isEqual(request.date()) && time.inPast());
    }

    public void deleteReservationById(Long id) {
        reservationDao.deleteById(id);
    }
}
