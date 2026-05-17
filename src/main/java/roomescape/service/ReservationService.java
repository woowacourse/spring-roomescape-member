package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.ReservationDetailResponse;
import roomescape.controller.dto.ReservationSummaryResponse;
import roomescape.domain.EntityId;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.EntityNotFoundException;
import roomescape.exception.ErrorCode;
import roomescape.exception.NotAcceptableReservationException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.AssembledReservation;
import roomescape.service.dto.ReservationCreateCommand;
import roomescape.service.dto.ReservationUpdateCommand;
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
        validateReservationNotDuplicate(command.date(), command.themeId(), command.timeId());
        validateThemeExist(command.themeId());

        EntityId reservationId = EntityId.random();
        ReservationTime time = findTimeById(command.timeId());
        Reservation reservation = Reservation.create(
                reservationId,
                command.name(),
                command.date(),
                time,
                command.themeId()
        );

        Reservation persisted = reservationRepository.persist(reservation);

        return reservationResponseMapper.mapToSummaryResponse(persisted);
    }

    @Transactional(readOnly = true)
    public List<ReservationDetailResponse> findAllIncludeDetail(String name) {
        List<Reservation> reservations = reservationRepository.findByName(name);

        return mapToDetailResponses(reservations);
    }

    @Transactional
    public ReservationSummaryResponse update(
            ReservationUpdateCommand command
    ) {
        Reservation reservation = findReservationById(command.reservationId());
        validateNameEquality(reservation, command.name());

        ReservationTime timeToUpdate = findTimeById(command.timeId());
        validateReservationNotDuplicate(command.date(), reservation.getThemeId(), timeToUpdate.id());

        Reservation updatedReservation = reservationRepository.updateDateAndTimeId(
                reservation,
                command.date(),
                timeToUpdate
        );

        return reservationResponseMapper.mapToSummaryResponse(updatedReservation);
    }

    @Transactional
    public ReservationSummaryResponse cancel(EntityId reservationId, String name) {
        Reservation reservation = findReservationById(reservationId);
        validateNameEquality(reservation, name);

        Reservation updatedReservation = reservationRepository.updateCanceled(reservation, true);

        return reservationResponseMapper.mapToSummaryResponse(updatedReservation);
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

    private void validateReservationNotDuplicate(
            LocalDate date,
            EntityId themeId,
            EntityId timeId
    ) {
        if (reservationRepository.existNotCanceledByDateAndThemeIdAndTimeId(date, themeId, timeId)) {
            throw new DuplicateReservationException(
                    "같은 테마의 같은 날짜/시간에는 하나의 예약만 가능합니다."
                            + " 요청한 날짜: " + date
                            + ", 요청한 테마 ID: " + themeId
                            + ", 요청한 시간 ID: " + timeId
            );
        }
    }

    private void validateNameEquality(Reservation reservation, String name) {
        if (reservation.hasDifferentName(name)) {
            throw new NotAcceptableReservationException(
                    ErrorCode.NOT_RESERVATION_OWNER,
                    "본인의 예약만 삭제할 수 있습니다."
            );
        }
    }

    private void validateThemeExist(EntityId themeId) {
        findThemeById(themeId);
    }

    private Reservation findReservationById(EntityId reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorCode.RESERVATION_NOT_FOUND,
                        "예약을 조회할 수 없습니다. reservationId = " + reservationId
                ));
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
