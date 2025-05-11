package roomescape.domain.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.AlreadyInUseException;
import roomescape.domain.reservation.dto.reservationtime.ReservationTimeRequest;
import roomescape.domain.reservation.dto.reservationtime.ReservationTimeResponse;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.reservation.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(final ReservationTimeRepository reservationTimeRepository,
                                  final ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional(readOnly = true)
    public List<ReservationTimeResponse> getAll() {
        final List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        return reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @Transactional
    public ReservationTimeResponse create(final ReservationTimeRequest request) {
        final ReservationTime reservationTime = ReservationTime.withoutId(request.startAt());

        final ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);

        return ReservationTimeResponse.from(savedReservationTime);
    }

    @Transactional
    public void delete(final Long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new AlreadyInUseException("해당 시간대에 예약이 존재하여 삭제할 수 없습니다 id = " + id);
        }

        reservationTimeRepository.deleteById(id);
    }
}
