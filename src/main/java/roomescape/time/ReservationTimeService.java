package roomescape.time;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.exception.ErrorReason;
import roomescape.exception.RoomescapeException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.dto.ReservationTimeRequest;
import roomescape.time.dto.ReservationTimeResponse;
import roomescape.time.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;
    private final Clock clock;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                  ReservationRepository reservationRepository, Clock clock) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
        this.clock = clock;
    }

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

    public void delete(Long id) {
        reservationTimeRepository.findById(id)
                .orElseThrow(() -> new RoomescapeException(ErrorReason.RESERVATION_TIME_NOT_FOUND));
        if (reservationRepository.existsByTimeId(id)) {
            throw new RoomescapeException(ErrorReason.RESERVATION_TIME_IN_USE);
        }
        reservationTimeRepository.deleteById(id);
    }

    public List<ReservationTimeResponse> readAvailableTimes(Long themeId, LocalDate date) {
        LocalDateTime now = LocalDateTime.now(clock);

        List<LocalTime> reservedTimes = reservationRepository.findByThemeAndDate(themeId, date).stream()
                .map(m -> m.getTime().getStartAt())
                .toList();

        return reservationTimeRepository.findAll().stream()
                .filter(r -> !reservedTimes.contains(r.getStartAt()))
                .filter(r -> !r.isPast(date, now))
                .map(ReservationTimeResponse::from)
                .toList();
    }
}
