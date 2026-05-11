package roomescape.closeddate.service;

import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.closeddate.domain.ClosedDate;
import roomescape.closeddate.repository.ClosedDateRepository;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;

@Slf4j
@Service
public class ClosedDateService {
    private final ClosedDateRepository closedDateRepository;

    public ClosedDateService(ClosedDateRepository closedDateRepository) {
        this.closedDateRepository = closedDateRepository;
    }

    @Transactional(readOnly = true)
    public List<ClosedDate> readClosedDates() {
        return closedDateRepository.findAll();
    }

    @Transactional
    public ClosedDate register(LocalDate date) {
        if (closedDateRepository.existsByDate(date)) {
            throw new ConflictException("이미 등록된 휴무일입니다.");
        }
        log.info("Closed date registered: date={}", date);
        return closedDateRepository.save(ClosedDate.create(date));
    }

    @Transactional
    public void deregister(Long id) {
        ClosedDate closedDate = getClosedDate(id);
        closedDateRepository.delete(id);
        log.info("Closed date deleted: id={}, date={}", closedDate.id(), closedDate.date());
    }

    @NonNull
    private ClosedDate getClosedDate(Long id) {
        return closedDateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 휴무일입니다."));
    }
}
