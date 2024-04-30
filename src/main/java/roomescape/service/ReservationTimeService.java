package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(final ReservationRepository reservationRepository,
                                  final ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public Long addReservationTime(ReservationTimeRequest reservationTimeRequest) {
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
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 잘못된 id 입니다. : " + id));

        return ReservationTimeResponse.from(reservationTime);
    }

    public void deleteReservationTime(Long id) {
        if (reservationRepository.isTimeIdExist(id)) {
            throw new IllegalArgumentException("[ERROR] 해당 시간에 예약이 존재합니다.");
        }
        boolean isDeleted = reservationTimeRepository.delete(id);
        if (!isDeleted) {
            throw new IllegalArgumentException("[ERROR] 삭제할 아이디가 존재하지 않습니다");
        }
        reservationRepository.delete(id);
    }
}
