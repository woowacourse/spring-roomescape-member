package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.ReservationAvailableTimeResponse;
import roomescape.dto.response.ReservationTimeResponse;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationTimeService {

    private final ReservationRepository repository;

    public ReservationTimeService(final ReservationRepository repository) {
        this.repository = repository;
    }

    public ReservationTimeResponse save(ReservationTimeRequest request) {
        ReservationTime reservationTime = new ReservationTime(request.startAt());
        return ReservationTimeResponse.from(repository.saveReservationTime(reservationTime));
    }

    public List<ReservationTimeResponse> findAll() {
        return repository.findAllReservationTimes()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public void deleteReservationTime(Long id) {
        repository.deleteReservationTimeById(id);
    }


    public List<ReservationAvailableTimeResponse> findAvailableTimes(Long themeId, LocalDate date) {
        List<ReservationTime> times = repository.findAllReservationTimes();

        return times.stream()
                .map(time -> {
                    Long timeId = time.getId();
                    boolean isBooked = (repository.getCountByTimeIdAndThemeIdAndDate(timeId, themeId, date) != 0);
                    return ReservationAvailableTimeResponse.from(time, isBooked);
                }).toList();
    }
}
