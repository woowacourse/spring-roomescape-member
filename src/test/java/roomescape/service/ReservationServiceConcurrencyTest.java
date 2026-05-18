package roomescape.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.command.ReservationSaveCommand;
import roomescape.exception.ConflictException;
import roomescape.policy.UserReservationSavePolicy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReservationServiceConcurrencyTest {

    private static final long TIME_ID = 1L;
    private static final long THEME_ID = 1L;
    private static final LocalDate RESERVATION_DATE = LocalDate.of(2099, 12, 31);
    private static final LocalDateTime NOW = LocalDateTime.of(2026, 5, 18, 10, 0);

    private final UserReservationSavePolicy savePolicy = new UserReservationSavePolicy();

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Sql({"/test-truncate.sql", "/test-theme.sql", "/test-reservation-time.sql"})
    void 동일한_날짜_시간_테마로_동시_저장_요청이_와도_하나만_성공() throws InterruptedException {
        int threadCount = 16;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger conflictCount = new AtomicInteger();
        List<Throwable> unexpectedErrors = new ArrayList<>();

        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            String name = "u" + i;
            futures.add(executor.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await();
                    ReservationSaveCommand command = new ReservationSaveCommand(
                            name, RESERVATION_DATE, TIME_ID, THEME_ID);
                    reservationService.saveReservation(command, NOW, savePolicy);
                    successCount.incrementAndGet();
                } catch (ConflictException e) {
                    conflictCount.incrementAndGet();
                } catch (Throwable t) {
                    synchronized (unexpectedErrors) {
                        unexpectedErrors.add(t);
                    }
                } finally {
                    doneLatch.countDown();
                }
            }));
        }

        readyLatch.await();
        startLatch.countDown();
        boolean finished = doneLatch.await(10, TimeUnit.SECONDS);
        executor.shutdownNow();

        assertThat(finished).isTrue();
        assertThat(unexpectedErrors).isEmpty();
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(conflictCount.get()).isEqualTo(threadCount - 1);

        Integer rowCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?",
                Integer.class, java.sql.Date.valueOf(RESERVATION_DATE), TIME_ID, THEME_ID);
        assertThat(rowCount).isEqualTo(1);

        for (Future<?> future : futures) {
            assertThat(future.isDone()).isTrue();
        }
    }
}
