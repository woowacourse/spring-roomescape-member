package roomescape.reservationTime.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.domain.ReservationTimeRepository;
import roomescape.reservationTime.dto.ReservationTimeRequest;
import roomescape.reservationTime.dto.ReservationTimeResponse;
import roomescape.reservationTime.dto.TimeConditionRequest;
import roomescape.reservationTime.dto.TimeConditionResponse;

@Service
public class ReservationTimeService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(final ReservationRepository reservationRepository,
                                  final ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTimeResponse createReservationTime(final ReservationTimeRequest request) {
        ReservationTime reservationTime = ReservationTime.createWithoutId(request.startAt());
        Long id = reservationTimeRepository.save(reservationTime);

        return ReservationTimeResponse.from(reservationTime.assignId(id));
    }

    public void deleteReservationTimeById(final Long id) {
        if (reservationRepository.existByReservationTimeId(id)) {
            throw new IllegalArgumentException("삭제할 수 없는 예약 시간입니다.");
        }

        boolean isDeleted = reservationTimeRepository.deleteById(id);
        validateIsExistsReservationTimeId(isDeleted);
    }

    private void validateIsExistsReservationTimeId(boolean isDeleted) {
        if (!isDeleted) {
            throw new IllegalArgumentException("존재하지 않는 예약 시간입니다.");
        }
    }

    public List<ReservationTimeResponse> getReservationTimes() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public List<TimeConditionResponse> getTimesWithCondition(final TimeConditionRequest request) {
        List<Reservation> reservations = reservationRepository.findByDateAndThemeId(request.date(), request.themeId());
        List<ReservationTime> times = reservationTimeRepository.findAll();

        return times.stream()
                .map(time -> toTimeConditionResponse(time,reservations))
                .toList();
    }

    private TimeConditionResponse toTimeConditionResponse(ReservationTime time, List<Reservation> reservations) {
        boolean hasTime = reservations.stream()
                .anyMatch(reservation -> reservation.isSameTime(time));
        return new TimeConditionResponse(time.getId(), time.getStartAt(), hasTime);
    }
}
