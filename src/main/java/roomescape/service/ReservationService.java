package roomescape.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.repository.JdbcReservationRepository;
import roomescape.repository.JdbcReservationTimeRepository;
import roomescape.service.dto.CreateReservationDto;
import roomescape.service.dto.ReservationResponseDto;
import roomescape.service.dto.ReservationSearchParamsDto;

@Service
public class ReservationService {

    private final JdbcReservationRepository reservationRepository;
    private final JdbcReservationTimeRepository reservationTimeRepository;

    public ReservationService(JdbcReservationRepository reservationRepository,
                              JdbcReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationResponseDto> findAllReservations(ReservationSearchParamsDto requestDto) {
        return reservationRepository.findReservationsWithParams(requestDto)
                .stream()
                .map(ReservationResponseDto::new)
                .toList();
    }

    public ReservationResponseDto createReservation(CreateReservationDto reservationInfo) {
        Reservation reservation = reservationInfo.toReservation();
        if (!reservationTimeRepository.isTimeExistsByTimeId(reservation.getTimeId())) {
            throw new IllegalArgumentException("예약 하려는 시간이 저장되어 있지 않습니다.");
        }

        ReservationTime time = reservationTimeRepository.findReservationTimeById(reservation.getTimeId());
        if (LocalDateTime.of(reservation.getDate(), time.getStartAt()).isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("지나간 날짜와 시간에 대한 예약은 불가능합니다.");
        }

        if (reservationRepository.isReservationExistsByDateAndTimeIdAndThemeId(reservation)) {
            throw new IllegalArgumentException("해당 테마는 같은 시간에 이미 예약이 존재합니다.");
        }

        Reservation savedReservation = reservationRepository.insertReservation(reservation);
        return new ReservationResponseDto(savedReservation);
    }

    public void deleteReservation(long id) {
        if (!reservationRepository.isReservationExistsById(id)) {
            throw new IllegalArgumentException("존재하지 않는 아이디입니다.");
        }
        reservationRepository.deleteReservationById(id);
    }
}
