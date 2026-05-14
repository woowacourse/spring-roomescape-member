package roomescape.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.exception.BusinessRuleViolationException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.projection.ReservationEntity;
import roomescape.repository.projection.ReservationTimeEntity;
import roomescape.repository.projection.ThemeEntity;
import roomescape.service.dto.ReservationCreateCommand;
import roomescape.service.dto.ReservationResult;

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
        validateCommand(command);
        ReservationTimeEntity timeEntity = reservationTimeRepository.findById(command.getTimeId())
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 시간입니다."));

        ThemeEntity themeEntity = themeRepository.findById(command.getThemeId())
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 테마입니다."));

        validateNotPast(command.getDate().atTime(timeEntity.getTime().getStartAt()));
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
        return reservationRepository.findByName(name).stream()
                .map(ReservationResult::from)
                .toList();
    }

    private void validateNotPast(LocalDateTime requestedAt) {
        if (!requestedAt.isAfter(LocalDateTime.now(clock))) {
            throw new BusinessRuleViolationException("지나간 날짜, 시간으로는 예약할 수 없습니다.");
        }
    }

    private void validateNotDuplicated(LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existsByDateAndTimeAndTheme(date, timeId, themeId)) {
            throw new BusinessRuleViolationException(
                    "해당 시간은 이미 예약되었습니다. 다른 시간을 선택해 주세요."
            );
        }
    }

    private void validateCommand(ReservationCreateCommand command) {
        if (command.getName() == null || command.getName().isBlank()) {
            throw new IllegalArgumentException("예약자 이름은 비어 있을 수 없습니다.");
        }
        if (command.getDate() == null) {
            throw new IllegalArgumentException("예약 날짜는 비어 있을 수 없습니다.");
        }
        if (command.getTimeId() == null) {
            throw new IllegalArgumentException("예약 시간을 선택해 주세요.");
        }
        if (command.getThemeId() == null) {
            throw new IllegalArgumentException("예약 테마를 선택해 주세요.");
        }
    }

}
