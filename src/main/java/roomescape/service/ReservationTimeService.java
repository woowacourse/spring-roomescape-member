package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.AvailableTimeResponse;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.entity.ReservationTimeEntity;
import roomescape.exception.reservationtime.ReservationTimeAlreadyExistsException;
import roomescape.exception.reservationtime.ReservationTimeNotFoundException;
import roomescape.exception.reservationtime.UsingReservationTimeException;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationtime.ReservationTimeRepository;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository timeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository timeRepository,
                                  ReservationRepository reservationRepository) {
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTimeResponse create(ReservationTimeRequest request) {
        if (timeRepository.existsByStartAt(request.startAt())) {
            throw new ReservationTimeAlreadyExistsException();
        }

        ReservationTime newReservationTime = new ReservationTime(request.startAt());
        return ReservationTimeResponse.from(timeRepository.add(newReservationTime));
    }

    public List<ReservationTimeResponse> getAll() {
        return ReservationTimeResponse.from(timeRepository.findAll());
    }

    public void deleteById(Long id) {
        if (isReservationExists(id)) {
            throw new UsingReservationTimeException();
        }

        int affectedCount = timeRepository.deleteById(id);
        if (affectedCount == 0) {
            throw new ReservationTimeNotFoundException(id);
        }
    }

    private boolean isReservationExists(Long id) {
        return reservationRepository.existsByTimeId(id);
    }

    public List<AvailableTimeResponse> getAvailableTimes(LocalDate date, Long themeId) {
        List<Long> reservedTimeIds = reservationRepository.findTimeIdsByDateAndTheme(date, themeId);
        List<ReservationTimeEntity> reservationTimeEntities = timeRepository.findAll();

        List<AvailableTimeResponse> availableTimeResponses = reservationTimeEntities.stream()
                .map(timeEntity -> {
                    boolean alreadyBooked = reservedTimeIds.contains(timeEntity.id());
                    return AvailableTimeResponse.of(timeEntity, alreadyBooked);
                })
                .toList();

        return availableTimeResponses;
    }
}
