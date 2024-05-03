package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.exception.DuplicatedDomainException;
import roomescape.exception.ReservationExistsException;
import roomescape.service.request.ReservationTimeAppRequest;
import roomescape.service.response.ReservationTimeAppResponse;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                  ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTime save(ReservationTimeAppRequest request) {
        ReservationTime newReservationTime = new ReservationTime(request.startAt());
        validateDuplication(newReservationTime.getStartAt());

        return reservationTimeRepository.save(newReservationTime);
    }

    private void validateDuplication(LocalTime parsedTime) {
        if (reservationTimeRepository.isStartTimeExists(parsedTime)) {
            throw new DuplicatedDomainException();
        }
    }

    public int delete(Long id) {
        if (reservationRepository.isTimeIdExists(id)) {
            throw new ReservationExistsException();
        }
        return reservationTimeRepository.deleteById(id);
    }

    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }

    public List<ReservationTimeAppResponse> findAllWithBookAvailability(LocalDate date, Long themeId) {
        List<Reservation> reservations = reservationRepository.findAllByDateAndThemeId(date, themeId);
        List<ReservationTime> reservedTimes = reservations.stream()
            .map(Reservation::getReservationTime)
            .toList();

        return findAll().stream()
            .map(time -> new ReservationTimeAppResponse(
                    time.getId(),
                    time.getStartAt(),
                    reservedTimes.contains(time)
                )
            ).toList();
    }
}
