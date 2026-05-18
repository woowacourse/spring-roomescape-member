package roomescape.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.TimeErrorCode;
import roomescape.common.exception.RestApiException;
import roomescape.dao.ReservationDao;
import roomescape.dao.TimeDao;
import roomescape.dao.row.TimeRow;
import roomescape.domain.Time;
import roomescape.dto.request.TimeRequestDto;
import roomescape.dto.response.TimeResponseDto;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TimeService {
    private final TimeDao timeDao;
    private final ReservationDao reservationDao;

    public TimeService(TimeDao timeDao, ReservationDao reservationDao) {
        this.timeDao = timeDao;
        this.reservationDao = reservationDao;
    }

    public List<TimeResponseDto> findAll() {
        return timeDao.findAll().stream()
                .map(TimeResponseDto::from)
                .toList();
    }

    public TimeResponseDto findById(Long id) {
        return timeDao.findById(id)
                .map(TimeResponseDto::from)
                .orElseThrow(() -> new RestApiException(TimeErrorCode.NOT_FOUND));
    }

    @Transactional
    public TimeResponseDto create(TimeRequestDto timeRequest) {
        Time time = Time.create(timeRequest.startAt());

        if (timeDao.existsByStartAt(time.getStartAt())) {
            throw new RestApiException(TimeErrorCode.DUPLICATE_START_AT);
        }

        return TimeResponseDto.from(timeDao.create(TimeRow.from(time)));
    }

    @Transactional
    public void delete(Long id) {
        if (!timeDao.existsById(id)) {
            throw new RestApiException(TimeErrorCode.NOT_FOUND);
        }

        if (reservationDao.existsByTimeId(id)) {
            throw new RestApiException(TimeErrorCode.REFERENCED_BY_RESERVATION);
        }
        timeDao.delete(id);
    }
}
