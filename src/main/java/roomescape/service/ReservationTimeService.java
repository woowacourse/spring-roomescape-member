package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequestDto;
import roomescape.exception.NotRemovableByConstraintException;
import roomescape.exception.WrongStateException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReservationTimeService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationTime insertReservationTime(ReservationTimeRequestDto reservationTimeRequestDto) {
        Long id = reservationTimeDao.insert(
                reservationTimeRequestDto.startAt().format(DateTimeFormatter.ofPattern("HH:mm")));

        return new ReservationTime(id, reservationTimeRequestDto.startAt().format(DateTimeFormatter.ofPattern("HH:mm")));
    }

    public List<ReservationTime> getAllReservationTimes() {
        return reservationTimeDao.findAll();
    }

    public void deleteReservationTime(Long id) {
        if (reservationDao.countByTimeId(id) != 0) {
            throw new NotRemovableByConstraintException("예약이 존재하는 시간은 삭제할 수 없습니다.");
        }
        reservationTimeDao.deleteById(id);
    }

    public boolean isBooked(String date, Long timeId, Long themeId) {
        if (LocalDate.parse(date).isBefore(LocalDate.parse("2020-01-01"))) {
            throw new WrongStateException("2020년 이전의 날짜는 입력할 수 없습니다.");
        }
        int count = reservationDao.count(date, timeId, themeId);
        return count > 0;
    }
}
