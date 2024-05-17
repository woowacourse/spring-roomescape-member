package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.domain.reservation.Reservation;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
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

    public Long addReservationTime(ReservationTimeRequest reservationTimeRequest) {
        validateTimeDuplicate(reservationTimeRequest.startAt());
        ReservationTime reservationTime = reservationTimeRequest.toEntity();
        return reservationTimeRepository.save(reservationTime);
    }

    public List<ReservationTimeResponse> getAllReservationTimes() {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        return reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public ReservationTimeResponse getReservationTime(Long id) {
        ReservationTime reservationTime = reservationTimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id가 존재하지 않습니다 : " + id));
        return ReservationTimeResponse.from(reservationTime);
    }

    public List<ReservationTimeResponse> getAvailableTimes(LocalDate date, Long themeId) {
        List<Reservation> reservations = reservationRepository.findByDateAndThemeId(date, themeId);
        List<ReservationTime> allTimes = reservationTimeRepository.findAll();
        List<ReservationTime> bookedTimes = reservations.stream()
                .map(Reservation::getTime)
                .toList();

        return allTimes.stream()
                .filter(time -> !bookedTimes.contains(time))
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public void deleteReservationTime(Long id) {
        reservationTimeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id가 존재하지 않습니다 : " + id));
        if (reservationRepository.existsByTimeId(id)) {
            throw new IllegalArgumentException("해당 시간에 예약이 존재합니다.");
        }
        reservationTimeRepository.delete(id);
    }

    public void validateTimeDuplicate(LocalTime time) {
        if (reservationTimeRepository.existsByTime(time)) {
            throw new IllegalArgumentException("이미 등록된 시간 입니다. : " + time);
        }
    }
}
