package com.session.session.service;

import com.session.session.domain.Coupon;
import com.session.session.domain.CouponPolicy;
import com.session.session.domain.CouponPolicyRepository;
import com.session.session.domain.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponIssueConsumer {

    private final CouponRepository couponRepository;
    private final CouponPolicyRepository couponPolicyRepository;

    @RabbitListener(queues = "coupon.queue")
    public void receive(Long userId) {

        CouponPolicy policy = couponPolicyRepository.findById(1L).orElseThrow();

        if (policy.canIssue()) {
            policy.issue();
            couponPolicyRepository.save(policy);
            couponRepository.save(new Coupon(userId));
            System.out.println("✔ 쿠폰 발급 완료 (큐): userId = " + userId);
        } else {
            System.out.println("❌ 쿠폰 소진 (큐): userId = " + userId);
        }
    }
}
