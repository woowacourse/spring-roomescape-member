package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.reservationTime.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeCreateRequest;
import roomescape.dto.response.ReservationTimeCreateResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.ReservationTimeUserResponse;
import roomescape.exception.ReservationExistException;
import roomescape.support.page.PageRequest;
import roomescape.support.page.PageResponse;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(final ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    @Transactional
    public ReservationTimeCreateResponse create(final ReservationTimeCreateRequest reservationTimeCreateRequest) {
        final ReservationTime reservationTime = reservationTimeDao.create(
                new ReservationTime(reservationTimeCreateRequest.startAt()));
        return ReservationTimeCreateResponse.from(reservationTime);
    }

    @Transactional(readOnly = true)
    public List<ReservationTimeUserResponse> findAllByDateAndTheme(final long themeId, final LocalDate date) {
        final List<ReservationTime> allTimes = reservationTimeDao.findAll();
        final List<ReservationTime> reservedTimes = reservationTimeDao.findAllReservedByThemeAndDate(themeId, date);
        return allTimes.stream()
                .map(time -> ReservationTimeUserResponse.from(
                        time,
                        reservedTimes.contains(time)))
                .toList();
    }

    @Transactional
    public void deleteIfNoReservation(final long id) {
        if (!reservationTimeDao.existsById(id)) {
            throw new NoSuchElementException("예약 시간이 존재하지 않습니다.");
        }
        if (reservationTimeDao.deleteIfNoReservation(id)) {
            return;
        }
        throw new ReservationExistException("이 시간에 대한 예약이 존재합니다.");
    }

    @Transactional(readOnly = true)
    public ReservationTime findById(final Long id) {
        final Optional<ReservationTime> reservationTime = reservationTimeDao.findById(id);
        if (reservationTime.isEmpty()) {
            throw new NoSuchElementException("예약 시간이 존재하지 않습니다.");
        }
        return reservationTime.get();
    }

    @Transactional(readOnly = true)
    public List<ReservationTimeResponse> findAll() {
        return reservationTimeDao.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PageResponse<ReservationTimeResponse> findAllWithPaging(final PageRequest pageRequest) {
        final List<ReservationTime> reservationTimes = reservationTimeDao.findAllWithPaging(pageRequest);
        final List<ReservationTimeResponse> reservationTimeResponses = reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();

        final long totalElements = reservationTimeDao.countAll();

        return new PageResponse<>(
                reservationTimeResponses,
                pageRequest.getPageNo(),
                pageRequest.getPageSize(),
                totalElements
        );
    }
}
