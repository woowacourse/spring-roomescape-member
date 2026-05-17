package roomescape.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.ReservationDetailResponse;
import roomescape.domain.EntityId;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.EntityNotFoundException;
import roomescape.exception.ErrorCode;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.AssembledReservation;
import roomescape.service.mapper.ReservationResponseMapper;

@Service
@RequiredArgsConstructor
public class AdminReservationService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    private final ReservationResponseMapper reservationResponseMapper;

    @Transactional(readOnly = true)
    public List<ReservationDetailResponse> findAllIncludeDetail() {
        List<Reservation> reservations = reservationRepository.findAll();

        return mapToDetailResponses(reservations);
    }

    @Transactional
    public void delete(EntityId reservationId) {
        boolean deleted = reservationRepository.delete(reservationId);

        if (!deleted) {
            throw new EntityNotFoundException(
                    ErrorCode.RESERVATION_NOT_FOUND,
                    "삭제할 예약을 조회하지 못했습니다. reservationId = " + reservationId
            );
        }
    }

    private List<ReservationDetailResponse> mapToDetailResponses(List<Reservation> reservations) {
        return reservations.stream()
                .map(this::assembleReservation)
                .map(reservationResponseMapper::mapToDetailResponse)
                .toList();
    }

    private AssembledReservation assembleReservation(Reservation reservation) {
        ReservationTime time = reservation.getTime();
        Theme theme = findThemeById(reservation.getThemeId());

        return new AssembledReservation(reservation, time, theme);
    }

    private Theme findThemeById(EntityId themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorCode.THEME_NOT_FOUND,
                        "테마를 조회할 수 없습니다. themeId = " + themeId
                ));
    }
}
