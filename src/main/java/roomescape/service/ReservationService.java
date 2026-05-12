package roomescape.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationCreateRequest;
import roomescape.dto.response.AvailableTimeResponse;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.ReservationAlreadyExistsException;
import roomescape.exception.ReservationNotFoundException;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    @Autowired
    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationResponse> getReservations() {
        return ReservationResponse.from(reservationDao.findAllReservations());
    }

    @Transactional
    public ReservationResponse createReservation(ReservationCreateRequest request) {
        try {
            Long id = reservationDao.insertReservation(request.name(), request.date(),
                    request.timeId(), request.themeId());
            return ReservationResponse.from(reservationDao.findReservationById(id));
        } catch (DuplicateKeyException e) {
            throw new ReservationAlreadyExistsException();
        }
    }

    @Transactional
    public void deleteReservation(Long id) {
        int deleteCount = reservationDao.delete(id);
        validateDelete(deleteCount);
    }

    public List<AvailableTimeResponse> getAvailableTimes(LocalDate date, Long id) {
        List<Long> reservedTimeIds = reservationDao.findReservationTimeIds(date, id);
        List<ReservationTime> allTimes = reservationTimeDao.findAllReservationTimes();

        Map<ReservationTime, Boolean> reservationTimeMap = allTimes.stream()
                .collect(Collectors.toMap(
                        time -> time,
                        time -> !reservedTimeIds.contains(time.getId()),
                        (v1, v2) -> v1,
                        LinkedHashMap::new
                ));
        return AvailableTimeResponse.from(reservationTimeMap);
    }

    private void validateDelete(int deleteCount) {
        if (deleteCount == 0) {
            throw new ReservationNotFoundException();
        }
    }
}
