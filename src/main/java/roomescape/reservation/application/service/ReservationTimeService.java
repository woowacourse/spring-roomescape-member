package roomescape.reservation.application.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.application.exception.DeleteTimeException;
import roomescape.reservation.application.exception.DuplicateTimeException;
import roomescape.reservation.application.repository.ReservationRepository;
import roomescape.reservation.application.repository.ReservationTimeRepository;
import roomescape.reservation.presentation.dto.AvailableReservationTimeResponse;
import roomescape.reservation.presentation.dto.ReservationTimeRequest;
import roomescape.reservation.presentation.dto.ReservationTimeResponse;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                  ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTimeResponse createReservationTime(final ReservationTimeRequest reservationTimeRequest) {
        if (reservationTimeRepository.isExists(reservationTimeRequest.getStartAt())) {
            throw new DuplicateTimeException("[ERROR] 중복된 시간은 추가할 수 없습니다.");
        }

        return new ReservationTimeResponse(reservationTimeRepository.insert(reservationTimeRequest.getStartAt()));
    }

    public List<ReservationTimeResponse> getReservationTimes() {
        return reservationTimeRepository.findAllTimes().stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    public void deleteReservationTime(final Long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new DeleteTimeException("[ERROR] 예약이 이미 존재하는 시간입니다.");
        }
        reservationTimeRepository.delete(id);
    }

    public List<AvailableReservationTimeResponse> getReservationTimes(final LocalDate date, final Long themeId) {
        return reservationTimeRepository.findAllTimes().stream()
                .map(reservationTime -> {
                    boolean alreadyBooked = reservationRepository.existsByDateAndThemeIdAndTimeId(date,
                            reservationTime.getId(), themeId);
                    return new AvailableReservationTimeResponse(reservationTime, alreadyBooked);
                })
                .toList();
    }
}
