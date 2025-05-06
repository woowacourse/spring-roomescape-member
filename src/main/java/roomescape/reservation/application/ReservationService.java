package roomescape.reservation.application;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.exception.AlreadyExistException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.ReservationTimeRepository;
import roomescape.reservation.ui.dto.AvailableReservationTimeRequest;
import roomescape.reservation.ui.dto.AvailableReservationTimeResponse;
import roomescape.reservation.ui.dto.CreateReservationRequest;
import roomescape.reservation.ui.dto.CreateReservationResponse;
import roomescape.reservation.ui.dto.ReservationResponse;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public CreateReservationResponse create(final CreateReservationRequest request) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(request.date(), request.timeId(),
                request.themeId())) {
            throw new AlreadyExistException("해당 시간에 이미 예약된 테마입니다.");
        }

        final ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new ResourceNotFoundException("해당 예약 시간 데이터가 존재하지 않습니다. id = " + request.timeId()));
        final Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new ResourceNotFoundException("해당 테마 데이터가 존재하지 않습니다. id = " + request.themeId()));
        final Reservation reservation = new Reservation(request.name(), request.date(), reservationTime, theme);

        final Long id = reservationRepository.save(reservation);
        final Reservation found = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 예약 데이터가 존재하지 않습니다. id = " + id));

        return CreateReservationResponse.from(found);
    }

    public void delete(final Long id) {
        reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 예약 데이터가 존재하지 않습니다. id = " + id));

        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<AvailableReservationTimeResponse> findAvailableReservationTimes(
            AvailableReservationTimeRequest request
    ) {
        final List<AvailableReservationTimeResponse> availableReservationTimeResponses = new ArrayList<>();
        final List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        for (ReservationTime reservationTime : reservationTimes) {
            availableReservationTimeResponses.add(new AvailableReservationTimeResponse(
                    reservationTime.getId(),
                    reservationTime.getStartAt(),
                    reservationRepository.existsByDateAndStartAtAndThemeId(
                            request.date(),
                            reservationTime.getStartAt(),
                            request.themeId()
                    ))
            );
        }

        return availableReservationTimeResponses;
    }
}
