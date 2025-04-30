package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;
import roomescape.exception.impl.AlreadyReservedException;
import roomescape.exception.impl.ReservationNotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponse> getReservations() {
        return ReservationResponse.from(reservationRepository.findAll());
    }

    public ReservationResponse createReservation(final ReservationRequest reservationRequest) {
        String name = reservationRequest.name();
        LocalDate date = reservationRequest.date();
        Long timeId = reservationRequest.timeId();
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId);

        Theme theme = themeRepository.findById(reservationRequest.themeId());
        if (reservationRepository.isDuplicateDateAndTimeAndTheme(date, reservationTime.getStartAt(), theme)) {
            throw new AlreadyReservedException();
        }
        Reservation reservation = Reservation.beforeSave(name, date, reservationTime, theme);

        final Reservation saved = reservationRepository.save(reservation);
        return ReservationResponse.from(saved);
    }

    public void delete(final Long id) {
        if (reservationRepository.findById(id) == null) {
            throw new ReservationNotFoundException();
        }
        reservationRepository.deleteById(id);
    }
}
