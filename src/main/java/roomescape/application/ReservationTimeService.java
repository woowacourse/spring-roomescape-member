package roomescape.application;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.application.dto.request.ReservationTimeRequest;
import roomescape.application.dto.response.AvailableReservationTimeResponse;
import roomescape.application.dto.response.ReservationTimeResponse;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTimeResponse> getAllReservationTimes() {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        return reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @Transactional
    public ReservationTimeResponse addReservationTime(ReservationTimeRequest reservationTimeRequest) {
        ReservationTime reservationTime = reservationTimeRequest.toReservationTime();

        if (reservationTimeRepository.existsByStartAt(reservationTime.getStartAt())) {
            throw new IllegalArgumentException("해당 시간은 이미 존재합니다.");
        }

        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);

        return ReservationTimeResponse.from(savedReservationTime);
    }

    @Transactional
    public void deleteReservationTimeById(Long id) {
        if (!reservationTimeRepository.existsById(id)) {
            throw new NoSuchElementException("해당 id의 시간이 존재하지 않습니다.");
        }

        if (reservationRepository.existsByTimeId(id)) {
            throw new IllegalArgumentException("해당 시간을 사용하는 예약이 존재합니다.");
        }

        reservationTimeRepository.deleteById(id);
    }

    public List<AvailableReservationTimeResponse> getAvailableReservationTimes(LocalDate date, Long themeId) {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        List<Reservation> reservations = reservationRepository.findByDateAndThemeId(date, themeId);

        return reservationTimes.stream()
                .map(reservationTime -> new AvailableReservationTimeResponse(
                        reservationTime.getId(),
                        reservationTime.getStartAt(),
                        isAlreadyBooked(reservationTime, reservations)
                ))
                .toList();
    }

    private boolean isAlreadyBooked(ReservationTime reservationTime, List<Reservation> reservations) {
        return reservations.stream()
                .anyMatch(reservation -> reservationTime.equals(reservation.getTime()));
    }
}
