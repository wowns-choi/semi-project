package com.project.semi.exception;

public class PaymentVerificationException extends RuntimeException{
    private Integer portOneAmount; // 실제 결제한 금액
    private Integer myDbAmount; // 결제했어야 할 db 내 금액
    private Integer priceDifference; // 둘의 가격차이
    public PaymentVerificationException() {
    }
    public PaymentVerificationException(String message) {
        super(message);
    }

    public PaymentVerificationException(Integer portOneAmount, Integer myDbAmount, Integer priceDifference) {
        this.portOneAmount = portOneAmount;
        this.myDbAmount = myDbAmount;
        this.priceDifference = priceDifference;
    }

    //getter
    public Integer getPortOneAmount() {
        return portOneAmount;
    }

    public Integer getMyDbAmount() {
        return myDbAmount;
    }
	public Integer getPriceDifference() {
		return priceDifference;
	}
    
    
    
}