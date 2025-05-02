package roomescape.application;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.application.dto.ReservationDto;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRegistrationPolicy;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.repository.ReservationRepository;
import roomescape.presentation.dto.request.ReservationRequest;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeService timeService;
    private final ThemeService themeService;
    private final ReservationRegistrationPolicy reservationRegistrationPolicy;

    public ReservationService(ReservationRepository reservationRepository, TimeService timeService,
                              ThemeService themeService, ReservationRegistrationPolicy reservationRegistrationPolicy) {
        this.reservationRepository = reservationRepository;
        this.timeService = timeService;
        this.themeService = themeService;
        this.reservationRegistrationPolicy = reservationRegistrationPolicy;
    }

    public ReservationDto registerReservation(ReservationRequest request) {
        Theme theme = themeService.getThemeById(request.themeId());
        ReservationTime reservationTime = timeService.getTimeById(request.timeId());
        Reservation reservation = Reservation.withoutId(request.name(), theme, request.date(), reservationTime);
        validateCanRegister(request, reservation);
        Long id = reservationRepository.save(reservation);

        return ReservationDto.from(Reservation.assignId(id, reservation));
    }

    private void validateCanRegister(ReservationRequest request, Reservation reservation) {
        boolean existsDuplicatedReservation = reservationRepository.existsDuplicatedReservation(
                request.date(), request.timeId(), request.themeId());
        reservationRegistrationPolicy.validate(reservation, existsDuplicatedReservation);
    }

    public List<ReservationDto> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return ReservationDto.from(reservations);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
