package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Component;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationCreateRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.exception.DuplicateContentException;
import roomescape.exception.InvalidRequestException;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Component
public class BookService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public BookService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponseDto createReservation(ReservationCreateRequestDto dto) {
        Reservation reservationRequest = createReservationRequest(dto);
        Reservation newReservation = reservationRepository.save(reservationRequest)
                .orElseThrow(() -> new IllegalStateException("[ERROR] 예약을 저장할 수 없습니다. 관리자에게 문의해 주세요."));

        return ReservationResponseDto.from(newReservation, newReservation.getTime(), newReservation.getTheme());
    }

    public Reservation createReservationRequest(ReservationCreateRequestDto dto) {
        ReservationTime reservationTime = reservationTimeRepository.findById(dto.timeId())
                .orElseThrow(() -> new NotFoundException("[ERROR] 예약 시간을 찾을 수 없습니다. id : " + dto.timeId()));
        Theme theme = themeRepository.findById(dto.themeId())
                .orElseThrow(() -> new NotFoundException("[ERROR] 테마를 찾을 수 없습니다. id : " + dto.themeId()));

        Reservation reservation = dto.createWithoutId(reservationTime, theme);

        validateReservationRequest(reservation);

        return reservation;
    }

    private void validateReservationRequest(Reservation reservationRequest) {
        if (reservationRequest.isBeforeCurrentDateTime()) {
            throw new InvalidRequestException("[ERROR] 현 시점 이후의 날짜와 시간을 선택해주세요.");
        }

        ReservationTime time = reservationRequest.getTime();
        Theme theme = reservationRequest.getTheme();
        List<Reservation> reservations = reservationRepository.findByDateTimeTheme(reservationRequest.getDate(), time.getStartAt(), theme.getId());
        if (!reservations.isEmpty()) {
            throw new DuplicateContentException("[ERROR] 이미 예약이 존재합니다. 다른 예약 일정을 선택해주세요.");
        }
    }

    public void deleteReservation(Long id) {
        int deletedReservationCount = reservationRepository.deleteById(id);

        if (deletedReservationCount == 0) {
            throw new NotFoundException("[ERROR] 등록된 예약번호만 삭제할 수 있습니다. 입력된 번호는 " + id + "입니다.");
        }
    }
}
