package roomescape.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.exception.CustomException;
import roomescape.exception.ErrorCode;
import roomescape.repository.ReservationTimeDao;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;

@Service
public class AdminTimeService {
    private final ReservationTimeDao reservationTimeDao;

    public AdminTimeService(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationTimeResponse save(ReservationTimeRequest request) {
        try {
            Long id = reservationTimeDao.save(request.startAt());
            ReservationTime saved = reservationTimeDao.findById(id);
            return ReservationTimeResponse.from(saved);
        } catch(DuplicateKeyException e){
            throw new CustomException(ErrorCode.ALREADY_EXISTS_TIME);
        }

    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeDao.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public void delete(Long id) {
        try{
            reservationTimeDao.delete(id);
        } catch (DataIntegrityViolationException e){
            throw new CustomException(ErrorCode.UNALLOWED_DELETE_RESERVED_TIME);
        }

    }
}
