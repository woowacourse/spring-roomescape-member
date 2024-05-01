package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.AvailableReservationTimeResponse;
import roomescape.dto.ReservationTimeResponse;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                  ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public ReservationTimeResponse create(ReservationTime reservationTime) {
        var savedReservationTime = reservationTimeRepository.save(reservationTime);
        return ReservationTimeResponse.from(savedReservationTime);
    }

    public List<ReservationTimeResponse> findAll() {
        var reservationTimes = reservationTimeRepository.findAll();
        return reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        var reservationTime = reservationTimeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 ID의 예약 시간이 없습니다."));
        validateHasReservation(reservationTime);
        reservationTimeRepository.deleteById(reservationTime.getId());
    }

    private void validateHasReservation(ReservationTime reservationTime) {
        int reservationCount = reservationRepository.countByTimeId(reservationTime.getId());
        if (reservationCount > 0) {
            throw new IllegalArgumentException("해당 예약 시간의 예약 건이 존재합니다.");
        }
    }

    public List<AvailableReservationTimeResponse> findAvailableReservationTimes(LocalDate date, Long themeId) {
        List<Reservation> reservations = reservationRepository.findAllByDateAndThemeId(date, themeId);
        HashSet<Long> reservedTimeIds = reservations.stream()
                .map(Reservation::getReservationTimeId)
                .collect(Collectors.toCollection(HashSet::new));
        List<ReservationTime> times = reservationTimeRepository.findAll();

        return times.stream()
                .map(reservationTime -> {
                    boolean isReserved = reservedTimeIds.contains(reservationTime.getId());
                    return AvailableReservationTimeResponse.of(reservationTime, isReserved);
                })
                .collect(Collectors.toList());
    }
}
