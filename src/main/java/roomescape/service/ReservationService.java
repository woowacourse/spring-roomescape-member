package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.request.ReservationRequest;
import roomescape.controller.response.ReservationResponse;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponse> findAll() {
        List<Reservation> reservations = reservationRepository.findAll();

        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = reservationTimeRepository.findById(reservationRequest.timeId())
                .orElseThrow(() -> new IllegalArgumentException("예약할 수 없는 시간입니다. timeId: " + reservationRequest.timeId()));
        Theme theme = themeRepository.findById(reservationRequest.themeId())
                .orElseThrow(() -> new IllegalArgumentException("예약할 수 없는 테마입니다. themeId: " + reservationRequest.themeId()));

        Reservation reservation = reservationRequest.toEntity(reservationTime, theme);

        rejectPastTimeReservation(reservation);
        rejectDuplicateDateTime(reservation);

        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponse.from(savedReservation);
    }

    private void rejectDuplicateDateTime(Reservation reservation) {
        LocalDateTime reservationDateTime = LocalDateTime.of(reservation.getDate(), reservation.getTime().getStartAt());
        boolean isDuplicateReservationPresent = reservationRepository.findAll().stream()
                .filter(reservation1 -> reservation.getTheme().equals(reservation1.getTheme()))
                .map(savedReservation -> LocalDateTime.of(savedReservation.getDate(), savedReservation.getTime().getStartAt()))
                .anyMatch(dateTime -> dateTime.equals(reservationDateTime));

        if (isDuplicateReservationPresent) {
            throw new IllegalArgumentException("중복된 예약이 존재합니다.");
        }
    }

    private void rejectPastTimeReservation(Reservation reservation) {
        LocalDateTime dateTime = LocalDateTime.of(reservation.getDate(), reservation.getTime().getStartAt());
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("이미 지난 시간입니다. 입력한 시간: " + dateTime.toLocalDate() + " "
                    + dateTime.toLocalTime());
        }
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}
