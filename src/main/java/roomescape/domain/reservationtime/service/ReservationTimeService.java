package roomescape.domain.reservationtime.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.dto.request.ReservationTimeRequestDto;
import roomescape.domain.reservationtime.dto.response.BookedReservationTimeResponseDto;
import roomescape.domain.reservationtime.dto.response.ReservationTimeResponseDto;
import roomescape.domain.reservationtime.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTimeResponseDto> getAllReservationTimes() {
        return reservationTimeRepository.findAll().stream()
            .map(ReservationTimeResponseDto::from)
            .toList();
    }

    public ReservationTimeResponseDto saveReservationTime(
        ReservationTimeRequestDto reservationTimeRequestDto) {
        ReservationTime reservationTime = reservationTimeRequestDto.toReservationTime();
        reservationTimeRepository.save(reservationTime);
        return ReservationTimeResponseDto.from(reservationTime);
    }

    public void deleteReservationTime(Long id) {
        reservationTimeRepository.delete(id);
    }

    public List<BookedReservationTimeResponseDto> getTimesContainsReservationInfoBy(String date,
        Long themeId) {
        return reservationTimeRepository.findBooked(date, themeId);
    }
}
