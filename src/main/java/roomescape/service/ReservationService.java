package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.ReservationRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.EntityNotFoundException;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationResponseDto> findAll() {
        List<Reservation> reservations = reservationDao.findAll();
        return reservations.stream()
            .map(this::createResponseDto)
            .toList();
    }

    public ReservationResponseDto add(ReservationRequestDto requestDto) {
        ReservationTime reservationTime = reservationTimeDao.findById(requestDto.timeId())
            .orElseThrow(() -> new EntityNotFoundException("선택한 예약 시간이 존재하지 않습니다."));

        if (reservationDao.isExist(requestDto.date(), requestDto.timeId())) {
            throw new DuplicateReservationException("이미 예약이 존재합니다.");
        }

        Reservation reservation = new Reservation(requestDto.name(), requestDto.date(), reservationTime);
        Reservation saved = reservationDao.save(reservation);
        return createResponseDto(saved);
    }

    public void deleteById(Long id) {
        reservationDao.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("삭제할 예약정보가 없습니다."));

        reservationDao.deleteById(id);
    }

    private ReservationResponseDto createResponseDto(Reservation reservation) {
        ReservationTime time = reservation.getTime();
        ReservationTimeResponseDto reservationTimeResponseDto = new ReservationTimeResponseDto(time.getId(), time.getStartAt());
        return new ReservationResponseDto(reservation.getId(), reservation.getName(), reservation.getDate(), reservationTimeResponseDto);
    }
}
