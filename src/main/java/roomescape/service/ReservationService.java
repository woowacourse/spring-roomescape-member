package roomescape.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationCreateRequest;
import roomescape.dto.request.ReservationUpdateRequest;
import roomescape.dto.response.AvailableTimeResponse;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.PastReservationTimeException;
import roomescape.exception.ReservationNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    @Autowired
    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public List<ReservationResponse> getReservations() {
        List<Reservation> reservations = reservationDao.findAllReservations();
        List<Long> timeIds = reservations.stream().map(Reservation::getTimeId).toList();
        List<Long> themeIds = reservations.stream().map(Reservation::getThemeId).toList();
        Map<Long, ReservationTime> timeMap = reservationTimeDao.findAllByIds(timeIds)
                .stream().collect(Collectors.toMap(ReservationTime::getId, Function.identity()));
        Map<Long, Theme> themeMap = themeDao.findAllByIds(themeIds)
                .stream().collect(Collectors.toMap(Theme::getId, Function.identity()));
        return reservations.stream()
                .map(reservation -> ReservationResponse.from(
                            reservation,
                            timeMap.get(reservation.getTimeId()),
                            themeMap.get(reservation.getThemeId())
                )).toList();
    }

    @Transactional
    public ReservationResponse createReservation(ReservationCreateRequest request) {
        ReservationTime time = reservationTimeDao.findById(request.timeId());
        LocalDateTime dateTime = LocalDateTime.of(request.date(), time.getStartAt());
        validateNotPastDate(dateTime);
        
        Long id = reservationDao.insertReservation(request.name(), request.date(),
                request.timeId(), request.themeId());
        Reservation reservation = reservationDao.findReservationById(id);
        Theme theme = themeDao.findById(reservation.getThemeId());
        
        return ReservationResponse.from(reservation, time, theme);
    }

    @Transactional
    public void deleteReservation(Long id) {
        int deleteCount = reservationDao.delete(id);
        validateChanged(deleteCount);
    }

    public List<AvailableTimeResponse> getAvailableTimes(LocalDate date, Long id) {
        List<Long> reservedTimeIds = reservationDao.findReservationTimeIds(date, id);
        List<ReservationTime> allTimes = reservationTimeDao.findAllReservationTimes();

        Map<ReservationTime, Boolean> reservationTimeMap = allTimes.stream()
                .collect(Collectors.toMap(
                        time -> time,
                        time -> !reservedTimeIds.contains(time.getId()),
                        (v1, v2) -> v1,
                        LinkedHashMap::new
                ));
        return AvailableTimeResponse.from(reservationTimeMap);
    }

    public List<ReservationResponse> getUserReservations(String name) {
        List<Reservation> reservations = reservationDao.findUserReservations(name);
        return reservations.stream()
                .map(reservation -> {
                    ReservationTime time = reservationTimeDao.findById(reservation.getTimeId());
                    Theme theme = themeDao.findById(reservation.getThemeId());
                    return ReservationResponse.from(reservation, time, theme);
                })
                .toList();
    }

    @Transactional
    public void deleteUserReservation(Long id, String name) {
        Reservation reservation = reservationDao.findReservationById(id);
        ReservationTime time = reservationTimeDao.findById(reservation.getTimeId());
        LocalDateTime dateTime = LocalDateTime.of(reservation.getDate(), time.getStartAt());
        validateNotPastDate(dateTime);

        int deleteCount = reservationDao.deleteUserReservation(id, name);
        validateChanged(deleteCount);
    }

    @Transactional
    public void updateUserReservation(Long id, ReservationUpdateRequest request) {
        int updateCount = reservationDao.update(id, request.date(), request.timeId(), request.themeId());
        validateChanged(updateCount);
    }

    private void validateNotPastDate(LocalDateTime dateTime) {
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new PastReservationTimeException("이전 날짜는 선택하실 수 없습니다.");
        }
    }

    private void validateChanged(int count) {
        if (count == 0) {
            throw new ReservationNotFoundException("해당 예약을 찾을 수 없습니다.");
        }
    }
}
