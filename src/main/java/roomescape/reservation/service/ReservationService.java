package roomescape.reservation.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.common.util.DateTime;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.domain.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;

@Service
public class ReservationService {

    private final DateTime dateTime;
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
            final DateTime dateTime,
            final ReservationRepository reservationRepository,
            final ReservationTimeRepository reservationTimeRepository,
            final ThemeRepository themeRepository
    ) {
        this.dateTime = dateTime;
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse createReservation(final ReservationRequest request) {
        ReservationTime time = reservationTimeRepository.findById(request.timeId());
        Theme theme = themeRepository.findById(request.themeId());

        LocalDateTime now = dateTime.now();
        LocalDateTime reservationDateTime = LocalDateTime.of(request.date(), time.getStartAt());

        validateCanReserveDateTime(reservationDateTime, now);
        validateExistDuplicateReservation(request, time);

        Reservation reservation = Reservation.createWithoutId(request.name(), request.date(), time, theme);
        Long id = reservationRepository.save(reservation);

        return ReservationResponse.from(reservation.assignId(id));
    }

    private void validateExistDuplicateReservation(ReservationRequest request, ReservationTime time) {
        if (reservationRepository.existBy(request.themeId(), request.date(), time.getStartAt())) {
            throw new IllegalArgumentException("이미 예약이 존재합니다.");
        }
    }

    private void validateCanReserveDateTime(LocalDateTime reservationDateTime, LocalDateTime now) {
        if (reservationDateTime.isBefore(now)) {
            throw new IllegalArgumentException("예약할 수 없는 날짜와 시간입니다.");
        }
    }

    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void deleteReservationById(final Long id) {
        boolean isDeleted = reservationRepository.deleteById(id);
        validateExistIdToDelete(isDeleted);
    }

    private void validateExistIdToDelete(boolean isDeleted) {
        if (!isDeleted) {
            throw new IllegalArgumentException("존재하지 않는 예약입니다.");
        }
    }
}
