package roomescape.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.EntityId;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.EntityNotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.AssembledReservation;
import roomescape.service.dto.ReservationCreateCommand;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    @Transactional
    public Reservation create(
            ReservationCreateCommand command
    ) {
        EntityId reservationId = EntityId.random();
        Reservation reservation = new Reservation(
                reservationId,
                command.name(),
                command.date(),
                command.timeId(),
                command.themeId()
        );

        return reservationRepository.persist(reservation);
    }

    @Transactional(readOnly = true)
    public List<AssembledReservation> findAllIncludeDetail() {
        return reservationRepository.findAll()
                .stream()
                .map(this::assembleReservation)
                .toList();
    }

    @Transactional
    public void delete(EntityId reservationId) {
        boolean deleted = reservationRepository.delete(reservationId);

        if (!deleted) {
            throw new EntityNotFoundException(
                    "삭제할 예약을 조회하지 못했습니다.",
                    "reservationId = " + reservationId
            );
        }
    }

    private AssembledReservation assembleReservation(Reservation reservation) {
        ReservationTime time = findTimeById(reservation.timeId());
        Theme theme = findThemeById(reservation.themeId());

        return new AssembledReservation(reservation, time, theme);
    }

    private ReservationTime findTimeById(EntityId timeId) {
        return timeRepository.findById(timeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "예약 시간을 조회할 수 없습니다.",
                        "timeId = " + timeId
                ));
    }

    private Theme findThemeById(EntityId themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "테마를 조회할 수 없습니다.",
                        "themeId = " + themeId
                ));
    }
}
