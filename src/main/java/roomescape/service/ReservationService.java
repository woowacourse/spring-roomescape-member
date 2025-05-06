package roomescape.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.controller.rest.request.ReservationCreateRequest;
import roomescape.controller.rest.response.ReservationResponse;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservation.ReservationDateTime;
import roomescape.domain.reservation.ReserverName;
import roomescape.repository.ReservationRepository;
import roomescape.domain.theme.Theme;
import roomescape.repository.ThemeRepository;
import roomescape.domain.time.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final Clock clock;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository,
            Clock clock
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.clock = clock;
    }

    public List<ReservationResponse> findAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return ReservationResponse.from(reservations);
    }

    public ReservationResponse createReservation(ReservationCreateRequest request) {
        ReservationDateTime dateTime = convertReservationDateTime(request.date(), request.timeId());
        validateReservationAvailability(dateTime);
        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new NoSuchElementException("해당 테마가 존재하지 않습니다."));
        ReserverName reserverName = new ReserverName(request.name());
        Reservation created = reservationRepository.save(reserverName, dateTime, theme);
        return ReservationResponse.from(created);
    }

    public void deleteReservationById(Long id) {
        Reservation reservation = getReservation(id);
        reservationRepository.deleteById(reservation.getId());
    }

    private ReservationDateTime convertReservationDateTime(final LocalDate date, final Long timeId) {
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new NoSuchElementException("예약 시간을 찾을 수 없습니다."));
        ReservationDate reservationDate = new ReservationDate(date);
        return new ReservationDateTime(reservationDate, reservationTime, clock);
    }

    private void validateReservationAvailability(ReservationDateTime dateTime) {
        if (reservationRepository.existSameDateTime(dateTime.getReservationDate(), dateTime.getTimeId())) {
            throw new IllegalArgumentException("이미 예약이 찼습니다.");
        }
    }

    private Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("예약을 찾을 수 없습니다."));
    }
}
