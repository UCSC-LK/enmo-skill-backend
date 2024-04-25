package org.ucsc.enmoskill.controller;

import org.ucsc.enmoskill.Services.LanguageGET;
import org.ucsc.enmoskill.Services.SkillGET;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class LanguageController extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        LanguageGET service = new LanguageGET(res);
        try {
            service.Run();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
