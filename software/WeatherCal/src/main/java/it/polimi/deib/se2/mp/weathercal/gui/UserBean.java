/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.gui;

import it.polimi.deib.se2.mp.weathercal.boundary.UserManager;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import java.io.IOException;

/**
 *
 * @author miglie
 */
@Named
@RequestScoped
public class UserBean{

    @EJB
    UserManager um;

    public UserBean() {
    }

    public String getUsername() {
        return um.getLoggedUser().getUsername();
    }
 public String searchUser() throws IOException{
        return "searched_user_page?faces-redirect=true";
//        FacesContext.getCurrentInstance().getExternalContext().redirect("create_event");
    }
}
