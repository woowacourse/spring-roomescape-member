package roomescape.service;

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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationTimeService {

    private ReservationTimeRepository reservationTimeRepository;
    private ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository, ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTimeResponseDto createReservationTime(final ReservationTimeCreateRequestDto requestDto) {
        ReservationTime requestTime = requestDto.createWithoutId();
        try {
            ReservationTime savedTime = reservationTimeRepository.save(requestTime)
                    .orElseThrow(() -> new IllegalStateException("[ERROR] 알 수 없는 오류로 인해 예약시간을 생성 실패하였습니다."));

            return ReservationTimeResponseDto.from(savedTime);
        } catch (IllegalArgumentException e) {
            throw new DuplicateContentException(e.getMessage());
        }
    }

    public List<ReservationTimeResponseDto> findAllReservationTimes() {
        List<ReservationTime> allReservationTime = reservationTimeRepository.findAll();
        return allReservationTime.stream()
                .map(reservationTime -> ReservationTimeResponseDto.from(reservationTime))
                .toList();
    }

    public List<AvailableReservationTimeResponseDto> findAvailableReservationTimes(LocalDate date, Long themeId) {
        List<ReservationTime> allReservationTimes = reservationTimeRepository.findAll();
        List<Reservation> availableReservationsByDate = reservationRepository.findByDate(date);

        List<ReservationTime> availableReservationTimes = availableReservationsByDate.stream().map(reservation -> reservation.time()).toList();
        List<AvailableReservationTimeResponseDto> dtos = new ArrayList<>();
        for (ReservationTime reservationTime : allReservationTimes) {
            if(availableReservationTimes.contains(reservationTime)) {
                dtos.add(new AvailableReservationTimeResponseDto(reservationTime.id(),reservationTime.startAt(),true));
                continue;
            }
            dtos.add(new AvailableReservationTimeResponseDto(reservationTime.id(),reservationTime.startAt(),false));
        }

        return dtos;
    }

    public void deleteReservationTimeById(final Long id) {
        int deletedReservationCount = reservationTimeRepository.deleteById(id);

        if (deletedReservationCount == 0) {
            throw new NotFoundException("[ERROR] 등록된 예약 시간 번호만 삭제할 수 있습니다. 입력된 번호는 " + id + "입니다.");
        }
    }
}
