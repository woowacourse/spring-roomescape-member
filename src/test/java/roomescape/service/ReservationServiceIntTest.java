package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.repository.ReservationDataSource;
import roomescape.web.dto.ReservationRequest;

class ReservationServiceIntTest extends BaseIntegrationTest {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationDataSource reservationDataSource;

    @BeforeEach
    void setUp() {
        reservationDataSource.clearTable();
        reservationDataSource.clearId();

        reservationDataSource.insertTheme("공포의 테마", "공포 테마", "https://image.com/image.png");
        reservationDataSource.insertReservationTime(LocalTime.of(10, 0));
    }

    @Test
    void 동시에_2명이_예약하면_1명만_성공해야_한다() throws InterruptedException {
        // given
        ReservationRequest request = new ReservationRequest("이프", LocalDate.now().plusDays(1), 1L, 1L);
        int threadCount = 2;
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger errorCount = new AtomicInteger(0);

        // when
        try (ExecutorService executorService = Executors.newFixedThreadPool(threadCount)) {
            for (int i = 0; i < threadCount; i++) {
                executorService.execute(() -> {
                    try {
                        reservationService.reserve(request);
                    } catch (DataIntegrityViolationException e) {
                        errorCount.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }
        }
        latch.await();

        // then: DB에 예약이 딱 하나만 있어야 하고, DataIntergrityViolation 예외가 한 번 발생해야 됨.
        assertThat(reservationService.getAllReservationsByPaging(0, 10)).hasSize(1);
        assertThat(errorCount.get()).isEqualTo(1);
    }
}
