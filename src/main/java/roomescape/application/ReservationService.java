package roomescape.application;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.application.dto.ReservationDto;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.repository.ReservationRepository;
import roomescape.exception.NotFoundException;
import roomescape.presentation.dto.request.ReservationRequest;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeService timeService;
    private final ThemeService themeService;

    public ReservationService(ReservationRepository reservationRepository, TimeService timeService,
                              ThemeService themeService) {
        this.reservationRepository = reservationRepository;
        this.timeService = timeService;
        this.themeService = themeService;
    }

    public ReservationDto registerReservation(ReservationRequest request) {
        Theme theme = themeService.getThemeById(request.themeId());
        ReservationTime reservationTime = timeService.getTimeById(request.timeId());
        Reservation reservation = Reservation.withoutId(request.name(), theme, request.date(), reservationTime);
        validateNotPast(reservation);
        validteNotDuplicate(reservation);
        Long id = reservationRepository.save(reservation);

        return ReservationDto.from(Reservation.assignId(id, reservation));
    }

    private void validteNotDuplicate(Reservation reservation) {
        List<Reservation> allReservations = reservationRepository.findAll();
        boolean duplicated = allReservations.stream()
                .anyMatch(r -> r.isDuplicated(reservation));
        if (duplicated) {
            throw new IllegalArgumentException("이미 예약된 일시입니다");
        }
    }

    private void validateNotPast(Reservation reservation) {
        if (reservation.isPast()) {
            throw new IllegalArgumentException("과거 일시로 예약할 수 없습니다.");
        }
    }

    public List<ReservationDto> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return ReservationDto.from(reservations);
    }

    public void deleteReservation(Long id) {
        boolean deleted = reservationRepository.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("삭제하려는 예약 id가 존재하지 않습니다. id: " + id);
        }
    }
}
