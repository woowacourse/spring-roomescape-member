package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.AvailableReservationTimeResponseDto;
import roomescape.dto.ReservationTimeCreateRequestDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.exception.DuplicateContentException;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository, ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTimeResponseDto createReservationTime(final ReservationTimeCreateRequestDto requestDto) {
        ReservationTime requestTime = requestDto.createWithoutId();
        try {
            ReservationTime savedTime = reservationTimeRepository.save(requestTime)
                    .orElseThrow(() -> new IllegalStateException("[ERROR] 예약시간을 생성 실패하였습니다."));

            return ReservationTimeResponseDto.from(savedTime);
        } catch (IllegalStateException e) {
            throw new DuplicateContentException(e.getMessage());
        }
    }

    public List<ReservationTimeResponseDto> findAllReservationTimes() {
        List<ReservationTime> allReservationTime = reservationTimeRepository.findAll();
        return allReservationTime.stream()
                .map(ReservationTimeResponseDto::from)
                .toList();
    }

    public List<AvailableReservationTimeResponseDto> findAvailableReservationTimes(LocalDate date, Long themeId) {
        List<ReservationTime> allReservationTimes = reservationTimeRepository.findAll();
        List<Reservation> availableReservationsByDate = reservationRepository.findByDateAndTheme(date, themeId);

        List<ReservationTime> availableReservationTimes = availableReservationsByDate.stream()
                .map(Reservation::getTime)
                .toList();

        return allReservationTimes.stream()
                .map(reservationTime -> new AvailableReservationTimeResponseDto(
                        reservationTime.id(),
                        reservationTime.startAt(),
                        availableReservationTimes.contains(reservationTime)
                ))
                .toList();
    }

    public void deleteReservationTimeById(final Long id) {
        if(reservationRepository.existsByTimeId(id)){
            throw new IllegalStateException("[ERROR] 이 시간의 예약이 이미 존재합니다. id : " + id);
        }

        int deletedReservationCount = reservationTimeRepository.deleteById(id);
        if (deletedReservationCount == 0) {
            throw new NotFoundException("[ERROR] 등록된 예약 시간 번호만 삭제할 수 있습니다. 입력된 번호는 " + id + "입니다.");
        }
    }
}
