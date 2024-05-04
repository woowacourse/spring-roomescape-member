package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeAddRequest;
import roomescape.dto.TimeWithBookStatusResponse;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private ReservationTimeRepository reservationTimeRepository;

    ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTime> findAllReservationTime() {
        return reservationTimeRepository.findAll();
    }

    public List<TimeWithBookStatusResponse> findReservationTimesWithReservationStatus(LocalDate date, Long themeId) {
        return reservationTimeRepository.findAllWithBookStatus(date, themeId);
    }

    public ReservationTime saveReservationTime(ReservationTimeAddRequest reservationTimeAddRequest) {
        if (reservationTimeRepository.existByStartAt(reservationTimeAddRequest.getStartAt())) {
            throw new IllegalArgumentException("이미 존재하는 예약시간은 추가할 수 없습니다.");
        }
        return reservationTimeRepository.save(reservationTimeAddRequest.toEntity());
    }

    public void removeReservationTime(Long id) {
        reservationTimeRepository.deleteById(id);
    }
}
