package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.exception.DuplicatedDataException;
import roomescape.exception.EmptyDataAccessException;
import roomescape.exception.PastDateReservationException;
import roomescape.exception.PastTimeReservationException;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequestDto;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.domain.Theme;
import roomescape.time.dao.ReservationTimeDao;
import roomescape.time.domain.ReservationTime;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationService(final ReservationDao reservationDao,
                              final ReservationTimeDao reservationTimeDao,
                              final ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }

    public Reservation save(final ReservationRequestDto requestDto) {
        final ReservationTime reservationTime = findReservationTime(requestDto);
        final Theme theme = findTheme(requestDto);
        final Reservation reservation = requestDto.toReservation(reservationTime, theme);

        validateReservationAvailable(reservation);
        
        final long reservationId = reservationDao.save(reservation);
        return new Reservation(reservationId, reservation);
    }

    private Theme findTheme(final ReservationRequestDto requestDto) {
        return themeDao.findById(requestDto.themeId())
                       .orElseThrow(() ->
                               new EmptyDataAccessException("themeId : %d에 해당하는 테마가 존재하지 않습니다.", requestDto.themeId()));
    }

    private ReservationTime findReservationTime(final ReservationRequestDto requestDto) {
        return reservationTimeDao.findById(requestDto.timeId())
                                 .orElseThrow(() ->
                                         new EmptyDataAccessException("timeId : %d에 해당하는 시간이 존재하지 않습니다.", requestDto.timeId()));
    }

    private void validateReservationAvailable(final Reservation reservation) {
        final LocalDate date = reservation.getDate();
        final ReservationTime time = reservation.getTime();
        final Theme theme = reservation.getTheme();
        if (date.isBefore(LocalDate.now())) {
            throw new PastDateReservationException("날짜가 과거인 경우 모든 시간에 대한 예약이 불가능 합니다.");
        }
        if (date.equals(LocalDate.now()) && time.checkPastTime()) {
            throw new PastTimeReservationException("날짜가 오늘인 경우 지나간 시간에 대한 예약이 불가능 합니다.");
        }
        boolean isExist = reservationDao.checkExistByReservation(date, time.getId(), theme.getId());
        if (isExist) {
            throw new DuplicatedDataException("이미 해당 날짜, 시간에 예약이 존재합니다.");
        }
    }

    public void deleteById(final long id) {
        int affectedColumn = reservationDao.deleteById(id);
        if (affectedColumn == 0) {
            throw new EmptyDataAccessException("reservationId : %d에 해당하는 예약이 존재하지 않습니다.", id);
        }
    }
}
