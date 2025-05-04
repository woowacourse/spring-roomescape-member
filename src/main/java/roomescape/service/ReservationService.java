package roomescape.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.ReservationRequestDto;
import roomescape.dto.ReservationValuesDto;
import roomescape.model.Reservation;
import roomescape.model.ReservationDateTime;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservedChecker;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservedChecker reservedChecker;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    public ReservationService(ReservationRepository reservationRepository, ReservedChecker reservedChecker,
                              ReservationTimeService reservationTimeService, ThemeService themeService) {
        this.reservationRepository = reservationRepository;
        this.reservedChecker = reservedChecker;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.getAllReservations();
    }

    public Reservation addReservation(ReservationRequestDto reservationRequestDto) {
        ReservationTime reservationTime = reservationTimeService.getReservationTimeById(
                reservationRequestDto.timeId());

        ReservationDateTime reservationDateTime = new ReservationDateTime(
                reservationRequestDto.date(), reservationTime);

        validateFutureDateTime(reservationDateTime);
        Theme theme = themeService.getThemeById(reservationRequestDto.themeId());
        Reservation reservation = reservationRequestDto.toEntity(null, reservationTime, theme);
        ReservationValuesDto reservationValuesDto = ReservationValuesDto.of(reservation);

        validateUniqueReservation(reservationValuesDto);

        return reservationRepository.addReservation(reservationValuesDto);
    }

    private void validateUniqueReservation(final ReservationValuesDto reservationValuesDto) {
        if (reservedChecker.contains(reservationValuesDto)) {
            throw new IllegalArgumentException("Reservation already exists");
        }
    }

    public void deleteReservation(long id) {

        reservationRepository.deleteReservation(id);
    }

    private void validateFutureDateTime(ReservationDateTime reservationDateTime) {
        LocalDateTime dateTime = LocalDateTime.of(reservationDateTime.getDate(),
                reservationDateTime.getTime().getStartAt());
        LocalDateTime now = LocalDateTime.now();
        if (dateTime.isBefore(now)) {
            throw new IllegalArgumentException("과거 예약은 불가능합니다.");
        }
    }


}
