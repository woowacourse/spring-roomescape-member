package roomescape.time.service;

import org.springframework.stereotype.Service;
import roomescape.time.dao.ReservationTimeDao;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationUserTime;
import roomescape.time.dto.ReservationTimeRequestDto;
import roomescape.time.dto.ReservationTimeResponseDto;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(final ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTimeResponseDto> findAll() {
        final List<ReservationTime> reservationTimes = reservationTimeDao.findAll();
        return reservationTimes.stream()
                .map(ReservationTimeResponseDto::new)
                .toList();
    }

    public ReservationTimeResponseDto save(final ReservationTimeRequestDto requestDto) {
        final long id = reservationTimeDao.save(requestDto.toReservationTime());
        final ReservationTime reservationTime = reservationTimeDao.findById(id);
        return new ReservationTimeResponseDto(id, reservationTime.getStartAt().toString());
    }

    public void deleteById(final long id) {
        int deleteCount = reservationTimeDao.deleteById(id);
        if (deleteCount == 0) {
            throw new NoSuchElementException(id + "를 아이디로 갖는 시간이 존재하지 않습니다.");
        }
    }

    public List<ReservationUserTime> findAvailableTime(final String date, final long themeId) {
        return reservationTimeDao.findAvailableTime(date, themeId);
    }
}
