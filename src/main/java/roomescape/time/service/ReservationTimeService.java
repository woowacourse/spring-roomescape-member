package roomescape.time.service;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.controller.request.AvailableReservationTimeRequest;
import roomescape.time.controller.request.CreateReservationTimeRequest;
import roomescape.time.controller.response.AvailableReservationTimeResponse;
import roomescape.time.controller.response.ReservationTimeResponse;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(
            final ReservationTimeRepository reservationTimeRepository,
            final ReservationRepository reservationRepository
    ) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTimeResponse createReservationTime(final CreateReservationTimeRequest request) {
        LocalTime startAt = request.startAt();
        if (reservationTimeRepository.existByStartAt(startAt)) {
            throw new IllegalArgumentException("이미 존재하는 시간입니다.");
        }
        ReservationTime created = reservationTimeRepository.save(startAt);
        return ReservationTimeResponse.from(created);
    }

    public List<ReservationTimeResponse> getAllReservationTimes() {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        return ReservationTimeResponse.from(reservationTimes);
    }

    public void deleteReservationTimeById(final Long id) {
        if (reservationRepository.existReservationByTimeId(id)) {
            throw new IllegalArgumentException("해당 시간에 이미 예약이 존재하여 삭제할 수 없습니다.");
        }
        ReservationTime reservationTime = getReservationTime(id);
        reservationTimeRepository.deleteById(reservationTime.getId());
    }

    public ReservationTime getReservationTime(final Long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("예약 시간을 찾을 수 없습니다."));
    }

    public List<AvailableReservationTimeResponse> findAvailableReservationTimes(
            final AvailableReservationTimeRequest request
    ) {
        return reservationTimeRepository.findAllAvailableReservationTimes(request.date(), request.themeId());
    }
}
