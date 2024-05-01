package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationRepository reservationRepository,
                                  ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public Long addReservationTime(ReservationTimeRequest reservationTimeRequest) {
        validateTimeNotExist(reservationTimeRequest.startAt());
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
        validateIdExist(id);
        ReservationTime reservationTime = reservationTimeRepository.findById(id);
        return ReservationTimeResponse.from(reservationTime);
    }

    public void deleteReservationTime(Long id) {
        validateIdExist(id);
        if (reservationRepository.existTimeId(id)) {
            throw new IllegalArgumentException("[ERROR] 해당 시간에 예약이 존재합니다.");
        }
        reservationRepository.delete(id);
    }

    public void validateIdExist(Long id) {
        if (!reservationTimeRepository.existId(id)) {
            throw new IllegalArgumentException("[ERROR] id가 존재하지 않습니다 : " + id);
        }
    }

    public void validateTimeNotExist(LocalTime time) {
        if (reservationTimeRepository.existTime(time)) {
            throw new IllegalArgumentException("[ERROR] 이미 등록된 시간 입니다. : " + time);
        }
    }

    public List<ReservationTimeResponse> getAvailableTimes(LocalDate date, Long themeId) {

        List<Reservation> reservations = reservationRepository.findByDateAndThemeId(date, themeId);
        List<ReservationTime> allTimes = reservationTimeRepository.findAll();
        List<ReservationTime> bookedTimes = reservations.stream().map(Reservation::getTime).toList();

        return allTimes.stream()
                .filter(time -> !bookedTimes.contains(time))
                .map(ReservationTimeResponse::from)
                .toList();
    }
}
