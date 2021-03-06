/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.gui;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 * @author paolo
 * @author miglie
 */
@Named
@RequestScoped
public class LoginBean {
    @Inject
    private Logger logger;

    private String email;
    private String password;

    public LoginBean() {
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public String login() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        try {
            request.login(this.email, this.password);
            return "/index?faces-redirect=true";
        } catch (ServletException e) {
            RequestContext.getCurrentInstance().showMessageInDialog(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed", "Wrong user/password, try again.")
            );
            logger.log(Level.SEVERE,"Login Failed");
            return null;
        }
    }
    public String logout() throws ServletException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        request.getSession().invalidate();
        request.logout();
        logger.log(Level.INFO, "User Logged out");
        return "/login?faces-redirect=true";
    }
}
