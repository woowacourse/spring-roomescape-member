package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeStatus;
import roomescape.repository.JdbcReservationRepository;
import roomescape.repository.JdbcReservationTimeRepository;
import roomescape.service.dto.AvailableTimeRequestDto;
import roomescape.service.dto.AvailableTimeResponseDtos;
import roomescape.service.dto.ReservationTimeRequestDto;
import roomescape.service.dto.ReservationTimeResponseDto;

@Service
public class ReservationTimeService {

    private final JdbcReservationTimeRepository reservationTimeRepository;
    private final JdbcReservationRepository reservationRepository;

    public ReservationTimeService(JdbcReservationTimeRepository reservationTimeRepository,
                                  JdbcReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationTimeResponseDto> findAllReservationTimes() {
        return reservationTimeRepository.findAllReservationTimes()
                .stream()
                .map(ReservationTimeResponseDto::new)
                .toList();
    }

    public AvailableTimeResponseDtos findAvailableReservationTimes(AvailableTimeRequestDto requestDto) {
        List<ReservationTime> allTimes = reservationTimeRepository.findAllReservationTimes();
        List<ReservationTime> bookedTimes = reservationTimeRepository.findReservedTimeByThemeAndDate(
                requestDto.getDate(), requestDto.getThemeId());

        ReservationTimeStatus reservationStatus = new ReservationTimeStatus(allTimes, bookedTimes);
        return new AvailableTimeResponseDtos(reservationStatus);
    }

    public ReservationTimeResponseDto createReservationTime(ReservationTimeRequestDto requestDto) {
        ReservationTime reservationTime = requestDto.toReservationTime();
        if (reservationTimeRepository.isTimeExistsByStartTime(reservationTime.getStartAt().toString())) {
            throw new IllegalArgumentException("중복된 시간을 입력할 수 없습니다.");
        }
        ReservationTime savedTime = reservationTimeRepository.insertReservationTime(reservationTime);
        return new ReservationTimeResponseDto(savedTime);
    }

    public void deleteReservationTime(long id) {
        if (!reservationTimeRepository.isTimeExistsByTimeId(id)) {
            throw new IllegalArgumentException("존재하지 않는 아이디입니다.");
        }
        if (reservationRepository.isReservationExistsByTimeId(id)) {
            throw new IllegalArgumentException("해당 시간에 예약이 있어 삭제할 수 없습니다.");
        }
        reservationTimeRepository.deleteReservationTimeById(id);
    }
}
