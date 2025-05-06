package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.BookedReservationTimeResponseDto;
import roomescape.dto.ReservationTimeRequestDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTimeResponseDto> getAllReservationTimes() {
        return reservationTimeRepository.findAllReservationTimes().stream()
            .map(ReservationTimeResponseDto::from)
            .toList();
    }

    public ReservationTimeResponseDto saveReservationTime(
        ReservationTimeRequestDto reservationTimeRequestDto) {
        ReservationTime reservationTime = reservationTimeRequestDto.toReservationTime();
        reservationTimeRepository.saveReservationTime(reservationTime);
        return ReservationTimeResponseDto.from(reservationTime);
    }

    public void deleteReservationTime(Long id) {
        reservationTimeRepository.deleteReservationTime(id);
    }

    public List<BookedReservationTimeResponseDto> getTimesContainsReservationInfoBy(String date,
        Long themeId) {
        return reservationTimeRepository.findBookedReservationTime(date, themeId);
    }
}
