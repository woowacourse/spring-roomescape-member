package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.InvalidDateException;
import roomescape.exception.InvalidTimeException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.SearchInfo;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public Reservation saveReservation(Reservation reservation) {
        validateFutureReservation(reservation);

        boolean isExist = reservationRepository.checkExists(reservation);
        validateUniqueReservation(isExist);

        long reservationId = reservationRepository.save(reservation);

        return reservationRepository.findByReservationId(reservationId);
    }

    public List<Reservation> findReservationList() {
        return reservationRepository.findAll();
    }

    public List<Reservation> findReservationListBySearchInfo(SearchInfo searchInfo) {
        return reservationRepository.findBySearchInfo(searchInfo);
    }

    public void deleteReservationById(long id) {
        Integer deleteCount = reservationRepository.deleteById(id);

        validateDeletionOccurred(deleteCount);
    }

    private void validateFutureReservation(Reservation reservation) {
        Long timeId = reservation.getTime().getId();
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId);

        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDate currentDate = currentDateTime.toLocalDate();
        LocalTime currentTime = currentDateTime.toLocalTime();

        if (reservation.isPast(currentDate)) {
            throw new InvalidDateException("지난 날짜에 대한 예약은 할 수 없습니다.");
        }
        if (reservation.isDate(currentDate) && reservationTime.checkPastTime(currentTime)) {
            throw new InvalidTimeException("지난 시간에 대한 예약은 할 수 없습니다.");
        }
    }

    private void validateUniqueReservation(boolean isExist) {
        if (isExist) {
            throw new DuplicateReservationException("이미 해당 날짜, 시간에 예약이 있습니다.");
        }
    }

    private void validateDeletionOccurred(Integer deleteCount) {
        if (deleteCount.equals(0)) {
            throw new NoSuchElementException("해당하는 예약이 없습니다.");
        }
    }
}
