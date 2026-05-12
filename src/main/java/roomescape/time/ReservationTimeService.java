package roomescape.time;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomescapeException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.dto.ReservationTimeRequest;
import roomescape.time.dto.ReservationTimeResponse;
import roomescape.time.repository.ReservationTimeRepository;

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
    public ReservationTimeResponse create(ReservationTimeRequest reservationTimeRequest) {
        ReservationTime reservationTime = new ReservationTime(
                reservationTimeRequest.startAt()
        );

        ReservationTime saved = reservationTimeRepository.save(reservationTime);
        return ReservationTimeResponse.from(saved);
    }

    public List<ReservationTimeResponse> read() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        reservationTimeRepository.findById(id)
                .orElseThrow(() -> new RoomescapeException(ErrorCode.RESERVATION_TIME_NOT_FOUND));
        if (reservationRepository.existsByTimeId(id)) {
            throw new RoomescapeException(ErrorCode.RESERVATION_TIME_IN_USE);
        }
        reservationTimeRepository.deleteById(id);
    }

    public List<ReservationTimeResponse> readAvailableTimes(Long themeId, LocalDate date) {

        List<LocalTime> reservedTimes = reservationRepository.findByThemeAndDate(themeId, date).stream()
                .map(m -> m.getTime().getStartAt())
                .toList();

        List<ReservationTime> availableTimes = reservationTimeRepository.findAll().stream()
                .filter(r -> !reservedTimes.contains(r.getStartAt()))
                .toList();

        return availableTimes.stream().map(ReservationTimeResponse::from).toList();
    }
}
