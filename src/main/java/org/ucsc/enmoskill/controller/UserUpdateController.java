package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.ucsc.enmoskill.Services.UserSer;
import org.ucsc.enmoskill.model.User;
import org.ucsc.enmoskill.utils.Hash;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class UserUpdateController extends HttpServlet {

    private TokenService.TokenInfo tokenInfo;

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);//default req is a request of controller
        Gson gson = new Gson();

        try {
            tokenInfo = tokenService.getTokenInfo(token);

            if (tokenService.isTokenValidState(token) == 1) {
                if (tokenInfo.isAdmin()) {

                    try {

                        BufferedReader reader = req.getReader();

                        User user = gson.fromJson(reader, User.class);
                        // extract query params
                        String accDeactivation = req.getParameter("deactivation");
                        int roleNo = Integer.parseInt(req.getParameter("role"));
                        UserSer service = new UserSer();

                        if (accDeactivation == null){
                            // make csa
                            if (roleNo == 4) {

                                int result = service.makeCSA(user.getId());

                                if (result > 0){
                                    resp.setStatus(HttpServletResponse.SC_OK);
                                    System.out.println("User level upgraded successfully");
                                    out.write("User level upgraded successfully");

                                } else {
                                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                    System.out.println("User level did not upgraded");
                                    out.write("User level did not upgraded");
                                }


                            } else if (roleNo == 1 || roleNo == 2) {

                                String newPw = Hash.hashPassword(user.getPassword());
                                int result = service.updatePassword(user, newPw);

                                if(result > 0){
                                    resp.setStatus(HttpServletResponse.SC_OK);
                                    System.out.println("Password updated successfully");
                                    out.write("Password updated successfully");
                                }else {
                                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                    System.out.println("Password did not updated");
                                    out.write("Password did not updated");
                                }


                            } else {
                                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            }
                        } else {

                            int result = service.accountDeactivation(user.getId());

                            if(result > 0){
                                resp.setStatus(HttpServletResponse.SC_OK);
                                System.out.println("Password updated successfully");
                                out.write("Password updated successfully");
                            }else {
                                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                System.out.println("Password did not updated");
                                out.write("Password did not updated");
                            }
                        }


                    } catch (Exception e) {
                        out.write(e.toString());
                        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }

                } else {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                }
            }else if (tokenService.isTokenValidState(token) == 2) {
                resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            }
        } catch (ExpiredJwtException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
        catch (JwtException | IllegalArgumentException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }
}
