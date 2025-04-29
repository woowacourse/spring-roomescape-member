package roomescape.reservationTime.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import roomescape.globalException.CustomException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationTime.ReservationTimeMapper;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.domain.dto.ReservationTimeReqDto;
import roomescape.reservationTime.domain.dto.ReservationTimeResDto;
import roomescape.reservationTime.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository repository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationTimeService(ReservationTimeRepository repository, ReservationRepository reservationRepository) {
        this.repository = repository;
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationTimeResDto> readAll() {
        List<ReservationTime> reservationTimes = repository.findAll();
        return reservationTimes.stream()
            .map(this::convertToReservationTimeResDto)
            .toList();
    }

    public ReservationTimeResDto add(ReservationTimeReqDto dto) {
        ReservationTime reservationTime = convertToReservationTimeReqDto(dto);
        validateDuplicateTime(reservationTime);
        ReservationTime savedReservationTime = repository.add(reservationTime);
        return convertToReservationTimeResDto(savedReservationTime);
    }

    public void delete(Long id) {
        ReservationTime reservationTime = repository.findByIdOrThrow(id);
        if (reservationRepository.existsByReservationTime(reservationTime)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "예약에서 사용 중인 시간입니다.");
        }
        repository.delete(id);
    }

    private void validateDuplicateTime(ReservationTime inputReservationTime) {
        List<ReservationTime> reservationTimes = repository.findAll();
        for (ReservationTime reservationTime : reservationTimes) {
            if (inputReservationTime.isSameTime(reservationTime)) {
                throw new CustomException(HttpStatus.CONFLICT, "이미 등록되어 있는 시간입니다.");
            }
        }
    }

    private ReservationTime convertToReservationTimeReqDto(ReservationTimeReqDto dto) {
        return ReservationTimeMapper.toEntity(dto);
    }

    public ReservationTimeResDto convertToReservationTimeResDto(ReservationTime reservationTime) {
        return ReservationTimeMapper.toResDto(reservationTime);
    }
}
