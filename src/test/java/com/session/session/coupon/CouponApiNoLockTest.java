package com.session.session.coupon;

import com.session.session.domain.CouponPolicy;
import com.session.session.domain.CouponPolicyRepository;
import com.session.session.domain.CouponRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CouponApiNoLockTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponPolicyRepository couponPolicyRepository;

    @BeforeEach
    void setUp() {
        couponRepository.deleteAll();
        couponPolicyRepository.save(new CouponPolicy(1L, 10, 0));
    }

    @Test
    void 동시성_문제_테스트_락_없이_100명_요청_결과는_10개_초과됨() throws Exception {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final long userId = i;
            executorService.submit(() -> {
                try {
                    mockMvc.perform(post("/api/coupon")
                            .param("userId", String.valueOf(userId)))
                            .andReturn();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        long count = couponRepository.count();
        System.out.println("💥 발급된 쿠폰 수: " + count); // 10개를 초과할 수 있음
        Assertions.assertTrue(count > 10);
    }
}
