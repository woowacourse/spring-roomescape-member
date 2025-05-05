package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.dto.ReservationCreateRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationRepository;

@Service
public class ReservationService {

    private final ReservationRequestFactory reservationRequestFactory;
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRequestFactory reservationRequestFactory, ReservationRepository reservationRepository) {
        this.reservationRequestFactory = reservationRequestFactory;
        this.reservationRepository = reservationRepository;
    }

    public ReservationResponseDto createReservation(ReservationCreateRequestDto dto) {
        Reservation reservationRequest = reservationRequestFactory.createReservationRequest(dto, reservationRepository);
        Reservation newReservation = reservationRepository.save(reservationRequest)
                .orElseThrow(() -> new IllegalStateException("[ERROR] 예약을 저장할 수 없습니다. 관리자에게 문의해 주세요."));

        return ReservationResponseDto.from(newReservation, newReservation.getTime(), newReservation.getTheme());
    }

    public List<ReservationResponseDto> findAllReservationResponses() {
        List<Reservation> allReservations = reservationRepository.findAll();

        return allReservations.stream()
                .map(reservation -> ReservationResponseDto.from(reservation, reservation.getTime(), reservation.getTheme()))
                .toList();
    }

    public void deleteReservation(Long id) {
        int deletedReservationCount = reservationRepository.deleteById(id);

        if (deletedReservationCount == 0) {
            throw new NotFoundException("[ERROR] 등록된 예약번호만 삭제할 수 있습니다. 입력된 번호는 " + id + "입니다.");
        }
    }
}
