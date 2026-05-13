package roomescape.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
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
            throw  new IllegalArgumentException("이미 존재하는 시간은 저장할 수 없습니다.");
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
            throw new IllegalArgumentException("예약중인 시간은 삭제할 수 없습니다.");
        }

    }
}
