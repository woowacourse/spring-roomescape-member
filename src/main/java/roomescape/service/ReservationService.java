package roomescape.service;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationSaveRequest;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

import java.util.List;
import roomescape.repository.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
            final ReservationRepository reservationRepository,
            final ReservationTimeRepository reservationTimeRepository,
            final ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public ReservationResponse saveReservation(final ReservationSaveRequest reservationSaveRequest) {
        final ReservationTime time = reservationTimeRepository.findById(reservationSaveRequest.timeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다."));

        final Theme theme = themeRepository.findById(reservationSaveRequest.themeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        final Reservation reservation = reservationSaveRequest.toReservation(time, theme);
        validateReservation(reservation);

        final Reservation savedReservation = reservationRepository.save(reservation);
        return new ReservationResponse(savedReservation);
    }

    private void validateReservation(final Reservation reservation) {
        validateDateTime(reservation);
        validateUnique(reservation);
    }

    private void validateDateTime(final Reservation reservation) {
        final LocalDateTime reservationDateTime = reservation.getDateTime();
        final boolean isBeforeNow = reservationDateTime.isBefore(LocalDateTime.now());
        if (isBeforeNow) {
            throw new IllegalArgumentException("지나간 시간입니다.");
        }
    }

    private void validateUnique(final Reservation reservation) {
        final boolean isReservationExist = reservationRepository.existByDateAndTimeIdAndThemeId(reservation.getDate(),
                reservation.getTimeId(), reservation.getThemeId());

        if (isReservationExist) {
            throw new IllegalArgumentException("이미 존재하는 예약입니다.");
        }
    }

    public void deleteReservation(final Long id) {
        if (reservationRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 예약입니다.");
        }

        reservationRepository.deleteById(id);
    }
}
