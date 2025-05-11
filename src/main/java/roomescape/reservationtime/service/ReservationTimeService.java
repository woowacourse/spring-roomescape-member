package roomescape.reservationtime.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.global.exception.RoomEscapeException.BadRequestException;
import roomescape.global.exception.RoomEscapeException.ResourceNotFoundException;
import roomescape.reservation.repository.ReservationDao;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.dto.request.ReservationTimeRequest;
import roomescape.reservationtime.dto.response.ReservationTimeResponse;
import roomescape.reservationtime.dto.response.ReservationTimesWithTotalPageResponse;
import roomescape.reservationtime.repository.ReservationTimeDao;

@Service
public class ReservationTimeService {
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationTimeResponse addTime(ReservationTimeRequest reservationTimeRequest) {
        if (reservationTimeDao.isExistTime(reservationTimeRequest.startAt())) {
            throw new BadRequestException("이미 존재하는 시간입니다.");
        }
        ReservationTime time = reservationTimeRequest.toEntity();
        ReservationTime savedTime = reservationTimeDao.save(time);
        return ReservationTimeResponse.fromEntity(savedTime);
    }

    public void deleteTime(Long id) {
        if (reservationDao.isExistByTimeId(id)) {
            throw new BadRequestException("예약이 존재하여 삭제할 수 없습니다.");
        }
        boolean isDeleted = reservationTimeDao.deleteById(id);
        if (!isDeleted) {
            throw new ResourceNotFoundException("해당하는 ID가 없습니다.");
        }
    }

    public List<ReservationTimeResponse> getReservationTimes() {
        return reservationTimeDao.findAll().stream()
                .map(ReservationTimeResponse::fromEntity)
                .toList();
    }

    public ReservationTimesWithTotalPageResponse getReservationTimesByPage(int page) {
        int totalThemes = reservationTimeDao.countTotalReservationTimes();
        int totalPage = (totalThemes % 10 == 0) ?
                totalThemes / 10 : (totalThemes / 10) + 1;
        if (page < 1 || page > totalPage) {
            throw new ResourceNotFoundException("해당하는 페이지가 없습니다");
        }
        int start = (page - 1) * 10 + 1;
        int end = start + 10 - 1;
        List<ReservationTimeResponse> reservationTimeResponses = reservationTimeDao.findReservationTimesWithPage(start,
                        end).stream()
                .map(ReservationTimeResponse::fromEntity)
                .toList();
        return new ReservationTimesWithTotalPageResponse(totalPage, reservationTimeResponses);
    }
}
