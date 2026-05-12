package roomescape.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.reservationTime.ReservationTimeCondition;
import roomescape.domain.reservationTime.ReservationTimeWithAvailable;
import roomescape.dto.reservationTime.AddReservationTimeRequest;
import roomescape.exception.DataReferencedException;
import roomescape.exception.ErrorMessage;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationTime.ReservationTimeRepository;

import java.util.List;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository, ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationTime> getAllReservationTime() {
        return reservationTimeRepository.getAllReservationTime();
    }

    @Transactional
    public ReservationTime addReservationTime(AddReservationTimeRequest addReservationTimeRequest) {
        return reservationTimeRepository.addReservationTime(addReservationTimeRequest.toEntity());
    }

    @Transactional
    public void deleteReservationTime(long id) {
        boolean hasTimeId = reservationRepository.existsByTimeId(id);

        if(hasTimeId) {
            throw new DataReferencedException(ErrorMessage.CANNOT_DELETE_RESERVATION_TIME_IN_USE);
        }

        try {
            reservationTimeRepository.deleteReservationTime(id);
        }  catch(DataIntegrityViolationException e) {
            throw new DataReferencedException(ErrorMessage.INTEGRITY_VIOLATION_ON_DELETE);
        }
    }

    public List<ReservationTimeWithAvailable> getAvailableReservationTimeByDateAndTheme(ReservationTimeCondition reservationTimeCondition) {
        return reservationTimeRepository.getAvailableReservationTimeByDateAndTheme(reservationTimeCondition);
    }
}
