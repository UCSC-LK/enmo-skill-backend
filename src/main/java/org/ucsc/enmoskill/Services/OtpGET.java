package org.ucsc.enmoskill.Services;
import io.github.cdimascio.dotenv.Dotenv;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.model.otpModel;
import org.ucsc.enmoskill.utils.OTPhash;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.http.HttpServletResponse;

public class OtpGET {
    private TokenService.TokenInfo tokenInfo;
    public OtpGET(TokenService.TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    public ResponsModel Run(){

        OTPhash otphash = new OTPhash();
        String ranNum=otphash.randomNumGen();

        Dotenv dotenv = Dotenv.load();
        String signKey = dotenv.get("otpSign");

        String hashString=otphash.OTPHashGen(signKey+ranNum);

        otpModel otpmodel = new otpModel(ranNum,hashString);

        return new ResponsModel(hashString, HttpServletResponse.SC_CREATED);

    }





}
