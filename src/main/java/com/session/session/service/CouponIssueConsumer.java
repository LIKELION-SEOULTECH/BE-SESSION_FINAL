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

    
}
