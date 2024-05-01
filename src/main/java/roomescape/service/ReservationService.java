package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequestDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<Reservation> getAllReservations() {
        return reservationDao.findAll();
    }

    @Transactional
    public Reservation insertReservation(ReservationRequestDto reservationRequestDto) {
        ReservationTime reservationTime = reservationTimeDao.findById(reservationRequestDto.timeId())
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 입력입니다."));
        LocalDate date = reservationRequestDto.date();
        validateDateAndTime(date, LocalTime.parse(reservationTime.getStartAt()));
        Long id = reservationDao.insert(
                reservationRequestDto.name(), reservationRequestDto.date().toString(), reservationRequestDto.timeId());

        return new Reservation(id, reservationRequestDto.name(), reservationRequestDto.date().toString(), reservationTime);
    }

    private void validateDateAndTime(LocalDate localDate, LocalTime localTime) {
        LocalDateTime inputDateTime = LocalDateTime.of(localDate, localTime);
        if (LocalDateTime.now().isAfter(inputDateTime)) {
            throw new IllegalArgumentException("지나간 날짜와 시간에 대한 예약 생성은 불가능합니다.");
        }
    }

    public void deleteReservation(Long id) {
        reservationDao.deleteById(id);
    }
}
