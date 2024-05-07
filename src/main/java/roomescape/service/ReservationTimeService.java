package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeAddRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.exceptions.ClientException;
import roomescape.repository.reservationtime.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTimeResponse addTime(ReservationTimeAddRequest reservationTimeAddRequest) {
        if (reservationTimeRepository.isDuplicatedTime(reservationTimeAddRequest.startAt())) {
            throw new ClientException("이미 존재하는 시간입니다.");
        }
        ReservationTime reservationTime = reservationTimeRepository.save(reservationTimeAddRequest.toReservationTime());
        return new ReservationTimeResponse(reservationTime);
    }

    public List<ReservationTimeResponse> findTimes() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    public List<ReservationTimeResponse> findTimesWithAlreadyBooked(LocalDate date, Long themeId) {
        return reservationTimeRepository.findAllWithAlreadyBooked(date, themeId)
                .entrySet()
                .stream()
                .map(e -> new ReservationTimeResponse(e.getKey(), e.getValue()))
                .toList();
    }

    public ReservationTimeResponse getTime(Long id) {
        return new ReservationTimeResponse(getValidReservationTime(id));
    }

    private ReservationTime getValidReservationTime(Long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new ClientException("존재하지 않는 예약 시간 id입니다. time_id = " + id));
    }

    public void deleteTime(Long id) {
        reservationTimeRepository.delete(id);
    }
}
