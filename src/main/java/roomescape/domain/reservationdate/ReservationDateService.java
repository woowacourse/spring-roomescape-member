package roomescape.domain.reservationdate;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.reservationdate.dto.AdminReservationDateResponse;
import roomescape.domain.reservationdate.dto.ReservationDateCreationRequest;
import roomescape.domain.reservationdate.dto.CreateReservationDateResponse;
import roomescape.domain.reservationdate.dto.ReservationDateResponse;
import roomescape.support.exception.ReservationDateErrorCode;
import roomescape.support.exception.RoomescapeException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationDateService {

    private final ReservationRepository reservationRepository;
    private final ReservationDateRepository reservationDateRepository;

    public List<AdminReservationDateResponse> getAllReservationDateForAdmin() {
        return reservationDateRepository.findAll().stream()
            .map(AdminReservationDateResponse::from)
            .toList();
    }

    public CreateReservationDateResponse createReservationDate(ReservationDateCreationRequest request) {
        ReservationDate reservationDate = reservationDateRepository.save(request.toEntity());
        return CreateReservationDateResponse.from(reservationDate);
    }

    public void deleteReservationDate(Long id) {
        if (reservationRepository.countByReservationDateId(id) > 0) {
            throw new RoomescapeException(ReservationDateErrorCode.RESERVATION_DATE_IN_USE);
        }
        int deletedCount = reservationDateRepository.deleteById(id);
        if (deletedCount == 0) {
            log.warn("이미 삭제된 날짜의 삭제 요청이 들어왔습니다. dateId={}", id);
        }
    }

    public List<ReservationDateResponse> getAllReservationDate() {
        return reservationDateRepository.findAll().stream()
            .map(ReservationDateResponse::from)
            .toList();
    }
}
