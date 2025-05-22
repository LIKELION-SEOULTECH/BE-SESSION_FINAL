package com.session.session.presentation;

import com.session.session.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/coupon")
    public ResponseEntity<String> issueCouponWithoutLock(@RequestParam Long userId) {
        boolean result = couponService.issueWithoutLock(userId);
        return result ? ResponseEntity.ok("쿠폰 발급 성공 (일반)") : ResponseEntity.badRequest().body("쿠폰 소진");
    }

    // 락으로 걸 API
    @PostMapping("/lock-coupon")
    public ResponseEntity<String> issueCouponWithLock(@RequestParam Long userId) {
        boolean result = couponService.issueWithLock(userId);
        return result ? ResponseEntity.ok("쿠폰 발급 성공 (락)") : ResponseEntity.badRequest().body("쿠폰 소진");
    }

    // RabbitMQ 를 통해 만들 API
    @PostMapping("/queue-coupon")
    public ResponseEntity<String> issueCouponWithQueue(@RequestParam Long userId) {
        couponService.enqueueCouponRequest(userId);
        return ResponseEntity.ok("발급 요청 완료 (큐)");
    }
}
