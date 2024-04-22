package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import org.ucsc.enmoskill.Services.DesignCategoryService;
import org.ucsc.enmoskill.model.DesignCategoryModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        Gson gson = new Gson();
        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        tokenInfo = tokenService.getTokenInfo(token);

        if (tokenService.isTokenValid(token)){
            if (tokenInfo.isAdmin()){

                // read the request body
                BufferedReader reader = req.getReader();

                // create a class
                DesignCategoryModel newCategory = gson.fromJson(reader, DesignCategoryModel.class);

                System.out.println(newCategory.getCategory());
                System.out.println(newCategory.getDel_1());
                System.out.println(newCategory.getDel_2());


                DesignCategoryService service = new DesignCategoryService();
                int result  = service.createCategory(newCategory);

                if (result > 0){
                    resp.setStatus(HttpServletResponse.SC_OK);
                    out.write(" inserted successfully");
                    System.out.println("Data inserted successfully");
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.write("Data insertion unsuccessful");
                    System.out.println("Data insertion unsuccessful");
                }
            }else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.write("Authorization failed");
                System.out.println("Authorization failed");
            }
        }else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("Authorization failed");
            System.out.println("Authorization failed");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        tokenInfo = tokenService.getTokenInfo(token);

        // get query parameter
        int categoryId = Integer.parseInt(req.getParameter("categoryId"));

        if (tokenService.isTokenValid(token)){
            if (tokenInfo.isAdmin()){

                // read the request body
                BufferedReader reader = req.getReader();
                // handle payload
                DesignCategoryModel categoryModel = gson.fromJson(reader, DesignCategoryModel.class);

                // set category id
                categoryModel.setCategoryId(categoryId);

                // update the data
                DesignCategoryService service = new DesignCategoryService();
                int result = service.updateCategoryData(categoryModel);

                if (result > 0){
                    resp.setStatus(HttpServletResponse.SC_OK);
                    out.write("Data updated successfully");
                    System.out.println("Data upated successfully");
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.write("Data update unsuccessful");
                    System.out.println("Data update unsuccessful");
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

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        tokenInfo = tokenService.getTokenInfo(token);

        if (tokenService.isTokenValid(token)){
            if (tokenInfo.isAdmin()){
                // get query parameter
                int categoryId = Integer.parseInt(req.getParameter("categoryId"));

                // call delete function
                DesignCategoryService service = new DesignCategoryService();
                int result = service.deleteCategory(categoryId);

                if (result > 0){
                    resp.setStatus(HttpServletResponse.SC_OK);
                    out.write("Data deleted successfully");
                    System.out.println("Data deleted successfully");
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.write("Data delete unsuccessful");
                    System.out.println("Data delete unsuccessful");
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
