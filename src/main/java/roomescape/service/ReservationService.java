package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.ReservationDetailResponse;
import roomescape.controller.dto.ReservationSummaryResponse;
import roomescape.domain.EntityId;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDateTime;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.EntityNotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.AssembledReservation;
import roomescape.service.dto.ReservationCreateCommand;
import roomescape.service.mapper.ReservationResponseMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final ReservationResponseMapper reservationResponseMapper;

    @Transactional
    public ReservationSummaryResponse create(
            ReservationCreateCommand command
    ) {
        ReservationTime time = findTimeById(command.timeId());
        validateReservationAvailable(command.date(), time.startAt());

        EntityId reservationId = EntityId.random();
        Reservation reservation = new Reservation(
                reservationId,
                command.name(),
                command.date(),
                command.timeId(),
                command.themeId()
        );

        Reservation persisted = reservationRepository.persist(reservation);

        return reservationResponseMapper.mapToSummaryResponse(persisted);
    }

    @Transactional(readOnly = true)
    public List<ReservationDetailResponse> findAllIncludeDetail() {
        List<Reservation> reservations = reservationRepository.findAll();

        return mapToDetailResponses(reservations);
    }

    @Transactional(readOnly = true)
    public List<ReservationDetailResponse> findAllIncludeDetail(String name) {
        List<Reservation> reservations = reservationRepository.findByName(name);

        return mapToDetailResponses(reservations);
    }

    @Transactional
    public void delete(EntityId reservationId, String name) {
        Reservation reservation = findReservationById(reservationId);
        if (reservation.hasDifferentName(name)) {
            throw new IllegalArgumentException("본인의 예약만 삭제할 수 있습니다.");
        }

        ReservationTime time = findTimeById(reservation.timeId());
        validateReservationAvailable(reservation.date(), time.startAt());

        deleteWithoutValidate(reservationId);
    }

    @Transactional
    public void deleteWithoutValidate(EntityId reservationId) {
        boolean deleted = reservationRepository.delete(reservationId);

        if (!deleted) {
            throw new EntityNotFoundException("삭제할 예약을 조회하지 못했습니다. reservationId = " + reservationId);
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

    private void validateReservationAvailable(LocalDate dateForReservation, LocalTime timeForReservation) {
        new ReservationDateTime(dateForReservation, timeForReservation)
                .validateAvailable(LocalDateTime.now());
    }

    private Reservation findReservationById(EntityId reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("예약을 조회할 수 없습니다. reservationId = " + reservationId));
    }

    private ReservationTime findTimeById(EntityId timeId) {
        return timeRepository.findById(timeId)
                .orElseThrow(() -> new EntityNotFoundException("예약 시간을 조회할 수 없습니다. timeId = " + timeId));
    }

    private Theme findThemeById(EntityId themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new EntityNotFoundException("테마를 조회할 수 없습니다. themeId = " + themeId));
    }
}
