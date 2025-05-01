package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.AvailableTimeResponseDto;
import roomescape.model.ReservationTime;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ReservedChecker;
import roomescape.repository.ReservedTimeChecker;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservedTimeChecker reservedTimeChecker;
    private final ReservedChecker reservedChecker;

    public ReservationTimeService(ReservationTimeRepository jdbcReservationTimeRepository,
                                  ReservedTimeChecker reservedTimeChecker, ReservedChecker reservedChecker) {
        this.reservationTimeRepository = jdbcReservationTimeRepository;
        this.reservedTimeChecker = reservedTimeChecker;
        this.reservedChecker = reservedChecker;
    }

    public ReservationTime addTime(LocalTime startAt) {
        ReservationTime reservationTime = new ReservationTime(null, startAt);
        return reservationTimeRepository.addTime(reservationTime);
    }

    public List<ReservationTime> getAllTime() {
        return reservationTimeRepository.getAllTime();
    }

    public void deleteTime(Long id) {
        if (reservedTimeChecker.isReservedTime(id)) {
            throw new IllegalArgumentException("Reservation time is already reserved.");
        }
        reservationTimeRepository.deleteTime(id);
    }

    public ReservationTime getReservationTimeById(Long id) {
        return reservationTimeRepository.findById(id);
    }

    public List<AvailableTimeResponseDto> getAvailableTimes(Long themeId, LocalDate date) {
        List<ReservationTime> reservationTimes = reservationTimeRepository.getAllTime();
        List<AvailableTimeResponseDto> availableTimeResponseDtos = new ArrayList<>();
        for (ReservationTime reservationTime : reservationTimes) {
            boolean alreadyBooked = false;
            if (reservedChecker.contains(date, reservationTime.getId(), themeId)) {
                alreadyBooked = true;
            }
            availableTimeResponseDtos.add(
                    new AvailableTimeResponseDto(reservationTime.getId(), reservationTime.getStartAt(), alreadyBooked));
        }
        return availableTimeResponseDtos;
    }
}
