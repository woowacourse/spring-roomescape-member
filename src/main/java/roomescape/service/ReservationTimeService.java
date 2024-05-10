package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.exceptions.DuplicationException;
import roomescape.exceptions.NotFoundException;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationtime.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(
            ReservationTimeRepository reservationTimeRepository,
            ReservationRepository reservationRepository
    ) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTimeResponse addTime(ReservationTimeRequest reservationTimeRequest) {
        if (reservationTimeRepository.isDuplicatedTime(reservationTimeRequest.startAt())) {
            throw new DuplicationException("이미 존재하는 시간입니다.");
        }
        ReservationTime reservationTime = reservationTimeRepository.save(reservationTimeRequest.toReservationTime());
        return new ReservationTimeResponse(reservationTime);
    }

    public List<ReservationTimeResponse> findTimes() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    public List<ReservationTimeResponse> findTimesWithAlreadyBooked(LocalDate date, Long themeId) {
        List<Long> alreadyBookedTimeIds = reservationRepository.findAlreadyBookedTimeIds(date, themeId);

        return reservationTimeRepository.findAll()
                .stream()
                .map(reservationTime -> new ReservationTimeResponse(
                                reservationTime,
                                reservationTime.isBelongTo(alreadyBookedTimeIds)
                        )
                )
                .toList();
    }

    public ReservationTimeResponse getTime(Long id) {
        return new ReservationTimeResponse(getValidReservationTime(id));
    }

    private ReservationTime getValidReservationTime(Long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약 시간 id입니다. time_id = " + id));
    }

    public void deleteTime(Long id) {
        reservationTimeRepository.delete(id);
    }
}
