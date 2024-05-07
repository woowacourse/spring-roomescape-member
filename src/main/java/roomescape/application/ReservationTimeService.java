package roomescape.application;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.application.dto.request.ReservationTimeRequest;
import roomescape.application.dto.response.AvailableTimeResponse;
import roomescape.application.dto.response.ReservationTimeResponse;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.exception.RoomescapeException;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                  ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public ReservationTimeResponse create(ReservationTimeRequest request) {
        if (reservationTimeRepository.existsByStartAt(request.startAt())) {
            throw new RoomescapeException("이미 존재하는 예약입니다.");
        }
        ReservationTime reservationTime = reservationTimeRepository.create(request.toReservationTime());
        return ReservationTimeResponse.from(reservationTime);
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @Transactional
    public void deleteById(long id) {
        ReservationTime time = reservationTimeRepository.getById(id);
        if (reservationRepository.existsByTimeId(time.getId())) {
            throw new RoomescapeException("연관된 예약이 존재하여 삭제할 수 없습니다.");
        }
        reservationTimeRepository.deleteById(time.getId());
    }

    public List<AvailableTimeResponse> findAvailableTimes(LocalDate date, long themeId) {
        return reservationTimeRepository.getReservationTimeAvailabilities(date, themeId)
                .stream()
                .map(timeSlot -> new AvailableTimeResponse(
                        ReservationTimeResponse.from(timeSlot.reservationTime()),
                        timeSlot.isBooked()))
                .toList();
    }
}
