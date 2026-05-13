package roomescape.domain.reservationtime.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.BusinessException;
import roomescape.domain.reservationtime.entity.ReservationTime;
import roomescape.domain.reservationtime.exception.TimeErrorCode;
import roomescape.domain.reservationtime.repository.ReservationTimeRepository;
import roomescape.domain.reservationtime.request.ReservationTimeCreateRequest;
import roomescape.domain.reservationtime.request.ReservationTimeUpdateRequest;
import roomescape.domain.reservationtime.response.ReservationTimeResponse;
import roomescape.domain.reservationtime.response.ReservationTimesResponse;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    @Autowired
    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTimesResponse findAllReservationTimes() {
        List<ReservationTimeResponse> times = reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
        return new ReservationTimesResponse(times);
    }

    @Transactional
    public ReservationTimeResponse saveReservationTime(ReservationTimeCreateRequest request) {
        ReservationTime reservationTime = ReservationTime.create(request.startAt());

        ReservationTime savedTime = reservationTimeRepository.save(reservationTime);

        return ReservationTimeResponse.from(savedTime);
    }

    @Transactional
    public ReservationTimeResponse updateReservationTime(Long id, ReservationTimeUpdateRequest request) {
        ReservationTime reservationTime = reservationTimeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(TimeErrorCode.RESERVATION_TIME_NOT_FOUND));

        if (reservationTimeRepository.existsByStartAt(request.startAt())) {
            throw new BusinessException(TimeErrorCode.RESERVATION_TIME_DUPLICATE);
        }

        reservationTime.update(request.startAt());
        reservationTimeRepository.update(id, reservationTime);

        return ReservationTimeResponse.from(reservationTime);
    }

    @Transactional
    public void deleteReservationTimeBy(Long id) {
        try {
            int deletedCount = reservationTimeRepository.deleteById(id);
            if (deletedCount == 0) {
                throw new BusinessException(TimeErrorCode.RESERVATION_TIME_NOT_FOUND);
            }
        } catch (DataIntegrityViolationException exception) {
            throw new BusinessException(TimeErrorCode.RESERVATION_TIME_DELETE_CONFLICT, exception);
        }
    }
}
