/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.dhcomitesso.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author misanchez
 */
@WebServlet("/USER")
@ServletSecurity(value = @HttpConstraint(rolesAllowed = {"user_role"}))
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("User :" + request.getUserPrincipal().getName() + "\n");
        response.getWriter().append("User in Role user_role :" + request.isUserInRole("user_role") + "\n");
        response.getWriter().append("User in Role admin_role :" + request.isUserInRole("admin_role"));
    }
}