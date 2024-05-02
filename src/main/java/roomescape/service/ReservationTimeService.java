package roomescape.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.TimeMemberResponse;
import roomescape.dto.TimeResponse;
import roomescape.dto.TimeSaveRequest;
import roomescape.exception.IllegalTimeException;
import roomescape.mapper.TimeMapper;
import roomescape.repository.ReservationDao;
import roomescape.repository.TimeDao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationTimeService {

    private final TimeMapper timeMapper = new TimeMapper();
    private final TimeDao timeDao;
    private final ReservationDao reservationDao; //TODO: 상위 개념 dao 호출 적절한지 고려
//    private final ReservationService reservationService;

    public ReservationTimeService(TimeDao timeDao, ReservationDao reservationDao) {
        this.timeDao = timeDao;
        this.reservationDao = reservationDao;
    }

    public List<TimeMemberResponse> findAllTimes(LocalDate date, Long themeId) {
        List<ReservationTime> allTimes = timeDao.findAll();
        List<TimeMemberResponse> newAllTimes = new ArrayList<>();

        // TODO: 인덴트 해결
        if (date != null && themeId != null) {
            List<Long> bookedTimeIds = reservationDao.findTimeIdByDateThemeId(date, themeId);
            for (ReservationTime time : allTimes) {
                if (bookedTimeIds.contains(time.getId())) {
                    newAllTimes.add(timeMapper.mapToResponse(time, true));
                }
                if (!bookedTimeIds.contains(time.getId())) {
                    newAllTimes.add(timeMapper.mapToResponse(time, false));
                }
            }
        }
        return newAllTimes;
    }

    public List<TimeResponse> findAllTimes() {
        List<ReservationTime> reservationTimes = timeDao.findAll();
        return reservationTimes.stream()
                .map(timeMapper::mapToResponse)
                .toList();
    }

    public ReservationTime findTimeById(Long id) {
        if (id == null) {
            throw new IllegalTimeException("[ERROR] 유효하지 않은 형식의 예약 시간입니다.");
        }
        Optional<ReservationTime> optionalReservationTime = timeDao.findById(id);
        if (optionalReservationTime.isEmpty()) {
            throw new IllegalTimeException("[ERROR] 예약 시간을 찾을 수 없습니다");
        }
        return optionalReservationTime.get();
    }

    public TimeResponse saveTime(TimeSaveRequest request) {
        ReservationTime reservationTime = timeMapper.mapToTime(request);

        if (timeDao.existByTime(reservationTime.getStartAt())) {
            throw new IllegalTimeException("[ERROR] 중복된 시간을 생성할 수 없습니다.");
        }

        Long saveId = timeDao.save(reservationTime);
        return timeMapper.mapToResponse(saveId, reservationTime);
    }

    public void deleteTimeById(Long id) {
        try {
            timeDao.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalTimeException("[ERROR] 예약이 존재하는 시간은 삭제할 수 없습니다.");
        }
    }
}
