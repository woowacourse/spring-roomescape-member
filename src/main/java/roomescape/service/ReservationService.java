package roomescape.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.exception.BusinessRuleViolationException;
import roomescape.exception.DataInconsistencyException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.projection.ReservationEntity;
import roomescape.repository.projection.ReservationTimeEntity;
import roomescape.repository.projection.ThemeEntity;
import roomescape.service.dto.ReservationCreateCommand;
import roomescape.service.dto.ReservationResult;
import roomescape.service.dto.ReservationUpdateCommand;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final Clock clock;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository,
            Clock clock
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.clock = clock;
    }


    public List<ReservationResult> findAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResult::from)
                .toList();
    }

    public ReservationResult create(ReservationCreateCommand command) {
        ReservationTimeEntity timeEntity = findTimeOrThrow(command.getTimeId());
        ThemeEntity themeEntity = findThemeOrThrow(command.getThemeId());

        validateCreateNotPast(command.getDate().atTime(timeEntity.getTime().getStartAt()));
        validateNotDuplicated(command.getDate(), timeEntity.getId(), themeEntity.getId());

        Reservation reservation = new Reservation(
                command.getName(),
                command.getDate(),
                timeEntity.getTime(),
                themeEntity.getTheme()
        );

        ReservationEntity saved = reservationRepository.save(
                reservation,
                timeEntity.getId(),
                themeEntity.getId()
        );
        return ReservationResult.from(saved);
    }

    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResult> findByName(String name) {
        return reservationRepository.findByNameOrderByDateAscTimeAsc(name).stream()
                .map(ReservationResult::from)
                .toList();
    }

    public void deleteByOwner(Long id, String name) {
        ReservationEntity entity = findByIdAndName(id, name);
        validateCancelNotPast(entity);
        reservationRepository.deleteById(id);
    }

    public ReservationResult updateByOwner(ReservationUpdateCommand command) {
        ReservationEntity entity = findByIdAndName(command.getId(), command.getName());
        validateCurrentReservationNotPast(entity);

        ReservationTimeEntity newTimeEntity = findTimeOrThrow(command.getTimeId());
        validateUpdateNotPast(command.getDate().atTime(newTimeEntity.getTime().getStartAt()));

        validateNotDuplicatedExcludingSelf(command, entity.getThemeId());
        reservationRepository.updateDateAndTime(command.getId(), command.getDate(), command.getTimeId());
        return ReservationResult.from(findUpdatedReservationOrThrow(command.getId()));
    }

    private ReservationTimeEntity findTimeOrThrow(Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 시간입니다."));
    }

    private ThemeEntity findThemeOrThrow(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 테마입니다."));
    }

    private ReservationEntity findUpdatedReservationOrThrow(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new DataInconsistencyException(
                        "저장된 예약을 찾을 수 없습니다. 데이터 정합성 문제가 의심됩니다."
                ));
    }

    private ReservationEntity findByIdAndName(Long id, String name) {
        return reservationRepository.findById(id)
                .filter(e -> e.getReservation().getName().equals(name))
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 예약입니다."));
    }


    private void validateNotPast(LocalDateTime requestedAt, String errorMessage) {
        if (!requestedAt.isAfter(LocalDateTime.now(clock))) {
            throw new BusinessRuleViolationException(errorMessage);
        }
    }

    private void validateNotDuplicated(LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existsByDateAndTimeAndTheme(date, timeId, themeId)) {
            throw new BusinessRuleViolationException(
                    "해당 시간은 이미 예약되었습니다. 다른 시간을 선택해 주세요."
            );
        }
    }

    private void validateCurrentReservationNotPast(ReservationEntity entity) {
        LocalDateTime reservedAt = entity.getReservation().getDate()
                .atTime(entity.getReservation().getTime().getStartAt());
        validateNotPast(reservedAt, "이미 지난 예약은 변경할 수 없습니다.");
    }

    private void validateNotDuplicatedExcludingSelf(ReservationUpdateCommand command, Long themeId) {
        if (reservationRepository.existsByDateAndTimeAndThemeExcludingId(
                command.getDate(), command.getTimeId(), themeId, command.getId())) {
            throw new BusinessRuleViolationException(
                    "해당 시간은 이미 예약되었습니다. 다른 시간을 선택해 주세요."
            );
        }
    }

    private void validateCreateNotPast(LocalDateTime requestedAt) {
        if (isPast(requestedAt)) {
            throw new BusinessRuleViolationException("지나간 날짜, 시간으로는 예약할 수 없습니다.");
        }
    }

    private void validateUpdateNotPast(LocalDateTime requestedAt) {
        if (isPast(requestedAt)) {
            throw new BusinessRuleViolationException("지나간 날짜, 시간으로는 변경할 수 없습니다.");
        }
    }

    private void validateCancelNotPast(ReservationEntity entity) {
        LocalDateTime reservedAt = entity.getReservation().getDate()
                .atTime(entity.getReservation().getTime().getStartAt());
        if (isPast(reservedAt)) {
            throw new BusinessRuleViolationException("이미 지난 예약은 취소할 수 없습니다.");
        }
    }

    private boolean isPast(LocalDateTime dateTime) {
        return !dateTime.isAfter(LocalDateTime.now(clock));
    }

}
