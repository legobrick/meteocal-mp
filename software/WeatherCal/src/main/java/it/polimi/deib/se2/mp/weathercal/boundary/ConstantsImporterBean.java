/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.boundary;

import javax.annotation.PostConstruct;
import javax.el.ELContextEvent;
import javax.el.ELContextListener;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 * @author paolo
 */
@ManagedBean(eager=true)
@ApplicationScoped
public class ConstantsImporterBean {

    @PostConstruct
    public void init() {
        FacesContext.getCurrentInstance().getApplication().addELContextListener((ELContextEvent event) -> {
            event.getELContext().getImportHandler().importPackage("it.polimi.deib.se2.mp.weathercal.entity");
        });
    }

}
