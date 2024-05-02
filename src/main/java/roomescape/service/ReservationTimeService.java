package roomescape.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.dto.AvailableReservationTimeResponse;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                  ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationTimeResponse> getAllReservationTimes() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public ReservationTimeResponse addReservationTime(ReservationTimeRequest reservationTimeRequest) {
        ReservationTime reservationTime = reservationTimeRequest.toReservationTime();
        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);

        return ReservationTimeResponse.from(savedReservationTime);
    }

    public void deleteReservationTimeById(Long id) {
        int count = reservationRepository.countByReservationTimeId(id);

        if (count > 0) {
            throw new IllegalArgumentException("해당 시간에 예약이 존재합니다.");
        }

        reservationTimeRepository.deleteById(id);
    }

    //todo: 메서드 이름 정상인가? 반복문 개선
    public List<AvailableReservationTimeResponse> getAvailableReservationTime(LocalDate date, Long themeId) {
        List<ReservationTime> bookedTimes = reservationTimeRepository.findByReservationDateAndThemeId(date, themeId);
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        List<AvailableReservationTimeResponse> availableReservationTimeResponses = new ArrayList<>();
        for (ReservationTime reservationTime : reservationTimes) {
            if (bookedTimes.contains(reservationTime)) {
                availableReservationTimeResponses.add(AvailableReservationTimeResponse.from(reservationTime, true));
                continue;
            }
            availableReservationTimeResponses.add(AvailableReservationTimeResponse.from(reservationTime, false));
        }
        return availableReservationTimeResponses;
    }
}
