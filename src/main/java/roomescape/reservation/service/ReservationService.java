package roomescape.reservation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.exception.RoomEscapeException;
import roomescape.member.domain.Member;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
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

    public Reservation save(final Member member, final ReservationRequestDto requestDto) {
        final ReservationTime reservationTime = reservationTimeDao.getById(requestDto.timeId());
        final Theme theme = themeDao.getById(requestDto.themeId());
        final Reservation reservation = requestDto.toReservation(member, reservationTime, theme);

        validateReservationAvailable(reservation);

        final long reservationId = reservationDao.save(reservation);
        return new Reservation(reservationId, reservation);
    }

    private void validateReservationAvailable(final Reservation reservation) {
        final ReservationDate date = reservation.getReservationDate();
        final ReservationTime time = reservation.getTime();
        final Theme theme = reservation.getTheme();
        if (date.isBeforeToday()) {
            throw new RoomEscapeException("날짜가 과거인 경우 모든 시간에 대한 예약이 불가능 합니다.");
        }
        if (date.isToday() && time.checkPastTime()) {
            throw new RoomEscapeException("날짜가 오늘인 경우 지나간 시간에 대한 예약이 불가능 합니다.");
        }
        boolean isExist = reservationDao.checkExistByReservation(date.getDate(), time.getId(), theme.getId());
        if (isExist) {
            throw new RoomEscapeException("이미 해당 날짜, 시간에 예약이 존재합니다.");
        }
    }

    public void deleteById(final long id) {
        reservationDao.deleteById(id);
    }
}
