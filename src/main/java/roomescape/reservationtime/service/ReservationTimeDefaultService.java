package roomescape.reservationtime.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.dto.AvailableTimeResponse;
import roomescape.reservationtime.dto.ReservationTimeRequest;
import roomescape.reservationtime.dto.ReservationTimeResponse;
import roomescape.reservationtime.exception.ReservationTimeAlreadyExistsException;
import roomescape.reservationtime.exception.ReservationTimeNotFoundException;
import roomescape.reservationtime.exception.UsingReservationTimeException;
import roomescape.reservationtime.repository.ReservationTimeRepository;

@Service
public class ReservationTimeDefaultService {
    private final ReservationTimeRepository timeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeDefaultService(ReservationTimeRepository timeRepository,
                                         ReservationRepository reservationRepository) {
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTimeResponse create(ReservationTimeRequest request) {
        if (timeRepository.existsByStartAt(request.startAt())) {
            throw new ReservationTimeAlreadyExistsException();
        }

        ReservationTime newReservationTime = ReservationTime.createWithoutId(request.startAt());
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
        if (affectedCount != 1) {
            throw new ReservationTimeNotFoundException();
        }
    }

    private boolean isReservationExists(Long id) {
        return reservationRepository.existsByTimeId(id);
    }

    public List<AvailableTimeResponse> getAvailableTimes(LocalDate date, Long themeId) {
        List<Long> bookedTimeIds = reservationRepository.findTimeIdsByDateAndTheme(date, themeId);
        List<ReservationTime> reservationTimeEntities = timeRepository.findAll();

        return reservationTimeEntities.stream()
                .map(time -> {
                    boolean alreadyBooked = bookedTimeIds.contains(time.getId());
                    return AvailableTimeResponse.of(time, alreadyBooked);
                })
                .toList();
    }
}
