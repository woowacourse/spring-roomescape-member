package roomescape.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationRepository;
import roomescape.dao.ReservationTimeRepository;
import roomescape.domain.ReservationTime;
import roomescape.exception.InvalidReservationException;
import roomescape.service.dto.ReservationTimeRequest;
import roomescape.service.dto.ReservationTimeResponse;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationTimeService(final ReservationTimeRepository reservationTimeRepository,
                                  ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTimeResponse create(final ReservationTimeRequest reservationTimeRequest) {
        validateDuplicated(reservationTimeRequest);
        ReservationTime reservationTime = reservationTimeRepository.save(
                new ReservationTime(reservationTimeRequest.startAt()));
        return new ReservationTimeResponse(reservationTime);
    }

    private void validateDuplicated(ReservationTimeRequest reservationTimeRequest) {
        if (reservationTimeRepository.existsByTime(reservationTimeRequest.startAt())) {
            throw new InvalidReservationException("이미 같은 시간이 존재합니다.");
        }
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    public void deleteById(final long id) {
        validateByReservation(id);
        reservationTimeRepository.deleteById(id);
    }

    private void validateByReservation(long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new InvalidReservationException("해당 시간에 예약이 존재해서 삭제할 수 없습니다.");
        }
    }
}
