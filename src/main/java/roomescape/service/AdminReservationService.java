package roomescape.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.ReservationDetailResponse;
import roomescape.domain.EntityId;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDateTime;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.EntityNotFoundException;
import roomescape.exception.ErrorCode;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.AssembledReservation;
import roomescape.service.mapper.ReservationResponseMapper;

@Service
@RequiredArgsConstructor
public class AdminReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;
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
                .map(this::mapToDetail)
                .toList();
    }

    private AssembledReservation assembleReservation(Reservation reservation) {
        ReservationTime time = findTimeById(reservation.timeId());
        Theme theme = findThemeById(reservation.themeId());

        return new AssembledReservation(reservation, time, theme);
    }

    private ReservationDetailResponse mapToDetail(AssembledReservation assembledReservation) {
        Reservation reservation = assembledReservation.reservation();
        ReservationTime time = assembledReservation.time();

        boolean cancelable = new ReservationDateTime(reservation.date(), time.startAt())
                .isAvailable(LocalDateTime.now());

        return reservationResponseMapper.mapToDetailResponse(
                assembledReservation,
                cancelable
        );
    }

    private ReservationTime findTimeById(EntityId timeId) {
        return timeRepository.findById(timeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorCode.RESERVATION_TIME_NOT_FOUND,
                        "예약 시간을 조회할 수 없습니다. timeId = " + timeId
                ));
    }

    private Theme findThemeById(EntityId themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorCode.THEME_NOT_FOUND,
                        "테마를 조회할 수 없습니다. themeId = " + themeId
                ));
    }
}
