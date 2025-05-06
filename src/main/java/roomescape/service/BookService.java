package roomescape.service;

import java.time.LocalDateTime;
import org.springframework.dao.DuplicateKeyException;
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
        try {
            Reservation newReservation = reservationRepository.save(reservationRequest)
                    .orElseThrow(() -> new IllegalStateException("[ERROR] 예약을 저장할 수 없습니다. 관리자에게 문의해 주세요."));

            return ReservationResponseDto.from(newReservation, newReservation.getTime(), newReservation.getTheme());
        } catch (DuplicateKeyException e) {
            throw new DuplicateContentException("[ERROR] 해당 날짜와 테마로 이미 예약된 내역이 존재합니다.");
        }
    }

    private Reservation createReservationRequest(ReservationCreateRequestDto dto) {
        ReservationTime reservationTime = reservationTimeRepository.findById(dto.timeId())
                .orElseThrow(() -> new NotFoundException("[ERROR] 예약 시간을 찾을 수 없습니다. id : " + dto.timeId()));

        validateRequestDateTime(LocalDateTime.of(dto.date(), reservationTime.getStartAt()));

        Theme theme = themeRepository.findById(dto.themeId())
                .orElseThrow(() -> new NotFoundException("[ERROR] 테마를 찾을 수 없습니다. id : " + dto.themeId()));

        return dto.createWithoutId(reservationTime, theme);
    }

    private void validateRequestDateTime(LocalDateTime requestDateTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        if (requestDateTime.isBefore(currentDateTime) || requestDateTime.equals(currentDateTime)) {
            throw new InvalidRequestException("[ERROR] 현 시점 이후의 날짜와 시간을 선택해주세요.");
        }
    }

    public void deleteReservation(Long id) {
        int deletedReservationCount = reservationRepository.deleteById(id);

        if (deletedReservationCount == 0) {
            throw new NotFoundException("[ERROR] 등록된 예약번호만 삭제할 수 있습니다. 입력된 번호는 " + id + "입니다.");
        }
    }
}
