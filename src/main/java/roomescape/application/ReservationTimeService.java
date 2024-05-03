package roomescape.application;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.application.dto.request.ReservationTimeRequest;
import roomescape.application.dto.response.AvailableTimeResponse;
import roomescape.application.dto.response.ReservationTimeResponse;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;

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
        if (existsByStartAt(request.startAt())) {
            throw new IllegalStateException(String.format("이미 존재하는 예약시간이 있습니다. 해당 시간:%s", request.startAt()));
        }
        ReservationTime reservationTime = reservationTimeRepository.create(request.toReservationTime());
        return ReservationTimeResponse.from(reservationTime);
    }

    private boolean existsByStartAt(LocalTime startAt) {
        return reservationTimeRepository.existsByStartAt(startAt);
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @Transactional
    public void deleteById(long id) {
        ReservationTime findReservationTime = reservationTimeRepository.getById(id);
        if (reservationRepository.existsByTimeId(id)) {
            throw new IllegalStateException(String.format("해당 예약 시간에 연관된 예약이 존재하여 삭제할 수 없습니다. 삭제 요청한 시간:%s",
                    findReservationTime.getStartAt()));
        }
        reservationTimeRepository.deleteById(id);
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
