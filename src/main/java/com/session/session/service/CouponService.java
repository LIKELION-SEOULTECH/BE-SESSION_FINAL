package com.session.session.service;

import com.session.session.domain.Coupon;
import com.session.session.domain.CouponPolicy;
import com.session.session.domain.CouponPolicyRepository;
import com.session.session.domain.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponPolicyRepository couponPolicyRepository;
    private final RedissonClient redissonClient;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public boolean issueWithoutLock(Long userId) {

        CouponPolicy policy = couponPolicyRepository.findById(1L).orElseThrow();
        if (!policy.canIssue()) return false;

        policy.issue();
        couponPolicyRepository.save(policy);
        couponRepository.save(new Coupon(userId));
        return true;
    }

    public boolean issueWithLock(Long userId) {

        RLock lock = redissonClient.getLock("couponLock");

        try {
            if (lock.tryLock(3, 1, TimeUnit.SECONDS)) {
                CouponPolicy policy = couponPolicyRepository.findById(1L).orElseThrow();
                if (!policy.canIssue()) return false;

                policy.issue();
                couponPolicyRepository.save(policy);
                couponRepository.save(new Coupon(userId));
                return true;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (lock.isHeldByCurrentThread()) lock.unlock();
        }
        return false;
    }

    public void enqueueCouponRequest(Long userId) {
        rabbitTemplate.convertAndSend("coupon.exchange", "coupon.issue", userId);
    }
}
