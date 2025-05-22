package com.session.session.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CouponPolicy {

    @Id
    private Long id;

    private int totalQuantity;
    private int issuedQuantity;

    public boolean canIssue() {
        return issuedQuantity < totalQuantity;
    }

    public void issue() {
        if (!canIssue()) throw new IllegalStateException("쿠폰이 모두 소진되었습니다.");
        this.issuedQuantity++;
    }
}