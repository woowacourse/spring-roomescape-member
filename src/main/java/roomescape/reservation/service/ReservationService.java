package roomescape.reservation.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import roomescape.globalException.CustomException;
import roomescape.reservation.ReservationMapper;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.dto.ReservationReqDto;
import roomescape.reservation.domain.dto.ReservationResDto;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationTime.ReservationTimeMapper;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.domain.dto.ReservationTimeResDto;
import roomescape.reservationTime.repository.ReservationTimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationResDto> readAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
            .map(this::convertReservationResDto)
            .collect(Collectors.toList());
    }

    public ReservationResDto add(ReservationReqDto dto) {
        Reservation reservation = convertReservation(dto);
        validateDuplicateDateTime(reservation);
        Reservation savedReservation = reservationRepository.add(reservation);
        return convertReservationResDto(savedReservation);
    }

    public void delete(Long id) {
        reservationRepository.findByIdOrThrow(id);
        reservationRepository.delete(id);
    }

    private void validateDuplicateDateTime(Reservation inputReservation) {
        List<Reservation> reservations = reservationRepository.findAll();
        for (Reservation reservation : reservations) {
            if (inputReservation.isSameDateTime(reservation)) {
                throw new CustomException(HttpStatus.CONFLICT, "이미 예약되어 있는 시간입니다.");
            }
        }
    }

    private Reservation convertReservation(ReservationReqDto dto) {
        ReservationTime reservationTime = reservationTimeRepository.findByIdOrThrow(dto.timeId());
        return ReservationMapper.toEntity(dto, reservationTime);
    }

    private ReservationResDto convertReservationResDto(Reservation reservation) {
        ReservationTimeResDto reservationTimeResDto = ReservationTimeMapper.toResDto(reservation.getReservationTime());
        return ReservationMapper.toResDto(reservation, reservationTimeResDto);
    }
}
