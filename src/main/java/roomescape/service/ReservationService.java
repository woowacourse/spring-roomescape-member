package roomescape.service;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ReservationDao;
import roomescape.dto.request.ReservationCreateRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.ReservationAlreadyExistsException;
import roomescape.exception.ReservationNotFoundException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public List<ReservationResponse> getReservations() {
        return ReservationResponse.from(reservationDao.findAllReservations());
    }

    @Transactional
    public ReservationResponse createReservation(ReservationCreateRequest request) {
        try {
            Long id = reservationDao.insertReservation(request.name(), request.date(),
                    request.timeId(), request.themeId());
            return ReservationResponse.from(reservationDao.findReservationById(id));
        } catch (DuplicateKeyException e) {
            throw new ReservationAlreadyExistsException();
        }
    }

    @Transactional
    public void deleteReservation(Long id) {
        int deleteCount = reservationDao.delete(id);
        validateDelete(deleteCount);
    }

    private void validateDelete(int deleteCount) {
        if (deleteCount == 0) {
            throw new ReservationNotFoundException();
        }
    }
}
