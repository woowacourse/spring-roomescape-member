package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.controller.dto.CreateReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.dto.CreateReservationParams;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;
import roomescape.time.repository.dto.FindReservedTimeParams;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public List<ReservationResponse> findAllReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public ReservationResponse reserve(CreateReservationRequest request) {
        validateReservationAvailable(request.date(), request.timeId(), request.themeId());
        CreateReservationParams params = new CreateReservationParams(request.name(), request.date(),
                request.timeId(), request.themeId());
        Reservation reservation = reservationRepository.save(params);

        return ReservationResponse.from(reservation);
    }

    private void validateReservationAvailable(LocalDate date, Long timeId, Long themeId) {
        //실제로 존재하는지 확인하기 위함.
        ReservationTime time = reservationTimeRepository.findById(timeId);
        Theme theme = themeRepository.findById(themeId);

        List<Long> reservedIds = reservationTimeRepository.findIdByCondition(new FindReservedTimeParams(theme.getId(), date));
        if(reservedIds.contains(time.getId())) {
            throw new IllegalArgumentException("이미 예약된 시간입니다.");
        }
    }

    @Transactional
    public void cancelReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
