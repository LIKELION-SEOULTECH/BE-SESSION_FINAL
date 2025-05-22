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

    
}
