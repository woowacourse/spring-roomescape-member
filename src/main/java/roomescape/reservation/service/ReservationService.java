package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.InvalidDateException;
import roomescape.exception.InvalidTimeException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequestDto;
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

    public ReservationService(final ReservationRepository reservationRepository, final ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation save(final ReservationRequestDto requestDto) {
        final ReservationTime reservationTime = reservationTimeRepository.findById(requestDto.timeId());
        final Reservation reservation = requestDto.toReservation();
        validateFutureReservation(reservation.getDate(), reservationTime);
        boolean isExist = reservationRepository.checkReservationExists(reservation.getDate().toString(), requestDto.timeId(), requestDto.themeId());
        validateUniqueReservation(isExist);

        final long reservationId = reservationRepository.save(reservation);
        return reservationRepository.findById(reservationId);
    }

    public void deleteById(final long id) {
        final Integer deleteCount = reservationRepository.deleteById(id);

        validateDeletionOccurred(deleteCount);
    }

    private void validateFutureReservation(final LocalDate localDate, final ReservationTime time) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDate currentDate = currentDateTime.toLocalDate();
        LocalTime currentTime = currentDateTime.toLocalTime();

        if (localDate.isBefore(currentDate)) {
            throw new InvalidDateException("지난 날짜에 대한 예약은 할 수 없습니다.");
        }
        if (localDate.equals(currentDate) && time.checkPastTime(currentTime)) {
            throw new InvalidTimeException("지난 시간에 대한 예약은 할 수 없습니다.");
        }
    }

    private void validateUniqueReservation(final boolean isExist) {
        if (isExist) {
            throw new DuplicateReservationException("이미 해당 날짜, 시간에 예약이 있습니다.");
        }
    }

    private void validateDeletionOccurred(final Integer deleteCount) {
        if (deleteCount.equals(0)) {
            throw new NoSuchElementException("해당하는 예약이 없습니다.");
        }
    }
}
