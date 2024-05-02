package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.reservationtime.ReservationTimeCreateRequest;
import roomescape.dto.reservationtime.ReservationTimeResponse;
import roomescape.exception.BadRequestException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationtime.ReservationTimeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReservationTimeService {

    private static final String RESERVATION_TIME_NOT_FOUND = "존재하지 않는 예약 시간입니다.";

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                  ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTimeResponse createTime(ReservationTimeCreateRequest request) {
        ReservationTime reservationTime = request.toReservationTime();

        validateDuplicated(reservationTime);

        ReservationTime newReservationTime = reservationTimeRepository.save(reservationTime);
        return ReservationTimeResponse.from(newReservationTime);
    }

    private void validateDuplicated(ReservationTime reservationTime) {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        boolean isDuplicated = reservationTimes.stream()
                .anyMatch(reservationTime::isSame);
        if (isDuplicated) {
            throw new BadRequestException("중복된 예약 시간입니다.");
        }
    }

    public ReservationTimeResponse readReservationTime(Long id) {
        ReservationTime reservationTime = reservationTimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESERVATION_TIME_NOT_FOUND));
        return ReservationTimeResponse.from(reservationTime);
    }

    public List<ReservationTimeResponse> readReservationTimes(LocalDate date, Long themeId) {
        if (date == null && themeId == null) {
            return reservationTimeRepository.findAll().stream()
                    .map(ReservationTimeResponse::from)
                    .toList();
        }
        
        List<Reservation> reservations = reservationRepository.findByDateAndThemeId(date, themeId);
        Set<Long> alreadyBookedTimes = reservations.stream()
                .map(reservation -> reservation.getTime().getId())
                .collect(Collectors.toSet());

        return reservationTimeRepository.findAll().stream()
                .map(time -> createTimeResponse(time, alreadyBookedTimes))
                .toList();
    }

    private ReservationTimeResponse createTimeResponse(ReservationTime time, Set<Long> alreadyBookedTimes) {
        boolean alreadyBooked = alreadyBookedTimes.contains(time.getId());
        return ReservationTimeResponse.of(time, alreadyBooked);
    }

    public void deleteTime(Long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new BadRequestException("해당 시간대에 예약이 존재합니다.");
        }
        reservationTimeRepository.deleteById(id);
    }
}
