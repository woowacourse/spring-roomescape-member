package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.reservation.ReservationTimeRepository;
import roomescape.service.dto.request.ReservationTimeRequest;
import roomescape.service.dto.response.AvailableReservationTimeResponse;
import roomescape.service.dto.response.ReservationTimeResponse;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(
            ReservationTimeRepository reservationTimeRepository,
            ReservationRepository reservationRepository
    ) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTimeResponse save(ReservationTimeRequest reservationTimeRequest) {
        ReservationTime reservationTime = reservationTimeRequest.toDomain();

        rejectDuplicateReservationTime(reservationTime);

        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);

        return ReservationTimeResponse.from(savedReservationTime);
    }

    public List<ReservationTimeResponse> findAll() {
        List<ReservationTime> savedReservationTimes = reservationTimeRepository.findAll();

        return savedReservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public List<AvailableReservationTimeResponse> findAllWithAvailability(LocalDate date, Long themeId) {
        List<ReservationTime> savedReservationTimes = reservationTimeRepository.findAll();
        List<ReservationTime> bookedReservationTimes = getBookedReservationTimes(date, themeId);

        return savedReservationTimes.stream()
                .map(time -> AvailableReservationTimeResponse.from(time, bookedReservationTimes.contains(time)))
                .toList();
    }

    private List<ReservationTime> getBookedReservationTimes(LocalDate date, Long themeId) {
        List<Reservation> bookedReservations = reservationRepository.findByDateAndThemeId(date, themeId);

        return bookedReservations.stream()
                .map(Reservation::getTime)
                .toList();
    }

    private void rejectDuplicateReservationTime(ReservationTime reservationTime) {
        List<ReservationTime> savedReservationTimes = reservationTimeRepository.findAll();
        boolean isDuplicateReservationTimePresent = savedReservationTimes.stream()
                .anyMatch(reservationTime::hasSameStartAt);

        if (isDuplicateReservationTimePresent) {
            throw new IllegalStateException(String.format("중복된 예약 시간이 존재합니다. 입력한 시간: %s",
                    reservationTime.getStartAt().toString()));
        }
    }

    public void deleteById(Long id) {
        reservationTimeRepository.deleteById(id);
    }
}
