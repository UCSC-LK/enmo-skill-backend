package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import org.ucsc.enmoskill.Services.PricePackageService;
import org.ucsc.enmoskill.model.PackagePricing;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class PricingDataController extends HttpServlet {

    private TokenService.TokenInfo tokenInfo;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        Gson gson = new Gson();
        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        tokenInfo = tokenService.getTokenInfo(token);

        int pricePackageId = Integer.parseInt(req.getParameter("pricePackageId"));

        if (tokenService.isTokenValid(token)){
            if (tokenInfo.isClient() || tokenInfo.isDesigner()){

                PricePackageService service = new PricePackageService();

                PackagePricing newPricing = service.getapricePackage(pricePackageId);

                if (newPricing != null){
                   resp.setStatus(HttpServletResponse.SC_OK);
                    out.write(gson.toJson(newPricing));
                    System.out.println("Data loaded successfully");
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.write("Data not found");
                    System.out.println("Data not found");
                }

            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.write("Authorization failed");
                System.out.println("Authorization failed");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("Authorization failed");
            System.out.println("Authorization failed");
        }
    }
}