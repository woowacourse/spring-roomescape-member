package roomescape.time.service;

import org.springframework.stereotype.Service;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationUserTime;
import roomescape.time.dto.ReservationTimeRequestDto;
import roomescape.time.dto.ReservationTimeResponseDto;
import roomescape.time.repository.ReservationTimeRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(final ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTimeResponseDto> findAll() {
        final List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        return reservationTimes.stream()
                .map(ReservationTimeResponseDto::new)
                .toList();
    }

    public ReservationTimeResponseDto save(final ReservationTimeRequestDto requestDto) {
        final long id = reservationTimeRepository.save(requestDto.toReservationTime());
        final ReservationTime reservationTime = reservationTimeRepository.findById(id);
        return new ReservationTimeResponseDto(id, reservationTime.getStartAt().toString());
    }

    public void deleteById(final long id) {
        final int deleteCount = reservationTimeRepository.deleteById(id);
        if (deleteCount == 0) {
            throw new NoSuchElementException("해당하는 시간이 없습니다.");
        }
    }

    public List<ReservationUserTime> findAvailableTime(final String date, final long themeId) {
        return reservationTimeRepository.findAvailableTime(date, themeId);
    }
}
