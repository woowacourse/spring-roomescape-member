package roomescape.service;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationTime.ReservationTime;
import roomescape.domain.ReservationTime.ReservationTimeCommand;
import roomescape.domain.ReservationTime.ReservationTimeCondition;
import roomescape.domain.ReservationTime.ReservationTimeWithAvailable;
import roomescape.exception.DataReferencedException;
import roomescape.exception.ErrorMessage;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationTime.ReservationTimeRepository;
import roomescape.repository.theme.ThemeRepository;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository, ReservationRepository reservationRepository, ThemeRepository themeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationTime> getAllReservationTime() {
        return reservationTimeRepository.getAllReservationTime();
    }

    @Transactional
    public ReservationTime addReservationTime(ReservationTimeCommand reservationTimeCommand) {
        return reservationTimeRepository.addReservationTime(reservationTimeCommand);
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

    public List<ReservationTimeWithAvailable> getReservationTimeByDateAndTheme(ReservationTimeCondition reservationTimeCondition) {
        long themeId = reservationTimeCondition.themeId();
        if (themeRepository.existsByTimeId(themeId)) {

        }
        return reservationTimeRepository.getReservationTimeByDateAndTheme(reservationTimeCondition);
    }
}
