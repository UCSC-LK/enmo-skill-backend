package org.ucsc.enmoskill.model;

public class otpModel {
    String otp,otpHash;

    public otpModel(String otp, String otpHash) {
        this.otp = otp;
        this.otpHash = otpHash;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getOtpHash() {
        return otpHash;
    }

    public void setOtpHash(String otpHash) {
        this.otpHash = otpHash;
    }
}
