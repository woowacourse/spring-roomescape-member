package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.EntityNotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.command.ReservationCreateCommand;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository timeRepository,
            ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Reservation create(
            ReservationCreateCommand createCommand
    ) {
        long timeId = createCommand.timeId();
        ReservationTime time = timeRepository.findById(timeId)
                .orElseThrow(() -> new EntityNotFoundException("예약 시간을 조회할 수 없습니다. id = " + timeId));

        long themeId = createCommand.themeId();
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new EntityNotFoundException("테마를 조회할 수 없습니다. id = " + themeId));

        Reservation reservation = Reservation.create(
                createCommand.name(),
                createCommand.date(),
                time,
                theme
        );

        return reservationRepository.persist(reservation);
    }

    @Transactional
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Transactional
    public void delete(long reservationId) {
        boolean deleted = reservationRepository.delete(reservationId);

        if (!deleted) {
            throw new EntityNotFoundException("삭제할 예약을 조회하지 못했습니다. reservationId = " + reservationId);
        }
    }
}
