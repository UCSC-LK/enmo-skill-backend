package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import org.ucsc.enmoskill.Services.DesignCategoryService;
import org.ucsc.enmoskill.model.DesignCategoryModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class DesignCategoryController extends HttpServlet {

    private TokenService.TokenInfo tokenInfo;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        Gson gson = new Gson();
        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        tokenInfo = tokenService.getTokenInfo(token);

        int categoryId = Integer.parseInt(req.getParameter("categoryId"));

        if (tokenService.isTokenValid(token)){

            DesignCategoryService service = new DesignCategoryService();

            // get all data
            if (categoryId == 0){
                List<DesignCategoryModel> list = service.getAllData();

                if (list != null){
                    resp.setStatus(HttpServletResponse.SC_OK);
                    out.write(gson.toJson(list));
                    System.out.println("Category data found");
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.write("Category data not found");
                    System.out.println("Category data not found");
                }
                // get a specific category data
            } else {
                DesignCategoryModel categoryData = service.getCategory(categoryId);

                if (categoryData != null){
                    resp.setStatus(HttpServletResponse.SC_OK);
                    out.write(gson.toJson(categoryData));
                    System.out.println("Category data found");
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.write("Category data not found");
                    System.out.println("Category data not found");
                }
            }



        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("Authorization failed");
            System.out.println("Authorization failed");
        }
    }
}
