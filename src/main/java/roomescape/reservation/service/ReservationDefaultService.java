package roomescape.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReservationDateTime;
import roomescape.reservation.exception.ReservationAlreadyExistsException;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.exception.ReservationTimeNotFoundException;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ReservationDefaultService implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationDefaultService(ReservationRepository reservationRepository,
                                     ReservationTimeRepository timeRepository,
                                     ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse create(ReservationRequest request) {
        ReservationDate date = new ReservationDate(request.date());
        ReservationTime time = timeRepository.findById(request.timeId())
                .orElseThrow(ReservationTimeNotFoundException::new);

        ReservationDateTime dateTime = new ReservationDateTime(date, time);

        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(ThemeNotFoundException::new);

        Reservation newReservation = Reservation.createWithoutId(request.name(), dateTime.getDate(),
                dateTime.getTime(), theme);

        if (reservationRepository.existsByDateAndTime(request.date(), time.getId())) {
            throw new ReservationAlreadyExistsException();
        }

        return ReservationResponse.from(reservationRepository.add(newReservation));
    }

    public List<ReservationResponse> getAll() {
        return ReservationResponse.from(reservationRepository.findAll());
    }

    public void deleteById(Long id) {
        int affectedCount = reservationRepository.deleteById(id);
        if (affectedCount != 1) {
            throw new ReservationNotFoundException();
        }
    }
}
