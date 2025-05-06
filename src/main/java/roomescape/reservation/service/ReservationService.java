package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.error.NotFoundException;
import roomescape.error.ReservationException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.dto.AvailableReservationTimeResponse;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public List<ReservationResponse> findAllReservation() {
        final List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public List<AvailableReservationTimeResponse> findAllReservationTime(final LocalDate date, final Long themeId) {
        return reservationTimeRepository.findAllAvailable(date, themeId);
    }

    public ReservationResponse saveReservation(final ReservationRequest request) {
        final ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약 시간입니다."));
        final Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 테마입니다."));
        if (reservationRepository.existsByDateAndTimeAndTheme(request.date(), reservationTime.getStartAt(),
                theme.getId())) {
            throw new ReservationException("해당 시간은 이미 예약되어있습니다.");
        }

        final Reservation reservation = new Reservation(request.name(), request.date(), reservationTime, theme);
        final Reservation newReservation = reservationRepository.save(reservation);
        return new ReservationResponse(newReservation);
    }

    public void deleteReservation(final Long id) {
        final int deletedCount = reservationRepository.deleteById(id);
        if (deletedCount == 0) {
            throw new NotFoundException("존재하지 않는 예약입니다. id=" + id);
        }
    }
}
