package roomescape.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.EntityNotFoundException;
import roomescape.controller.dto.ReservationCreateRequest;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    @Transactional
    public Reservation create(
            ReservationCreateRequest createRequest
    ) {
        ReservationTime time = findTimeById(createRequest.timeId());
        Theme theme = findThemeById(createRequest.themeId());

        Reservation reservation = Reservation.create(
                createRequest.name(),
                createRequest.date(),
                time,
                theme
        );

        return reservationRepository.persist(reservation);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Transactional
    public void delete(long reservationId) {
        boolean deleted = reservationRepository.delete(reservationId);

        if (!deleted) {
            throw new EntityNotFoundException(
                    "삭제할 예약을 조회하지 못했습니다.",
                    "reservationId = " + reservationId
            );
        }
    }

    private ReservationTime findTimeById(long timeId) {
        return timeRepository.findById(timeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "예약 시간을 조회할 수 없습니다.",
                        "timeId = " + timeId
                ));
    }

    private Theme findThemeById(long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "테마를 조회할 수 없습니다.",
                        "themeId = " + themeId
                ));
    }
}
