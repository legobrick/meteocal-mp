/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.gui;
import it.polimi.deib.se2.mp.weathercal.boundary.UserManager;
import java.io.IOException;
import javax.persistence.EntityManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import it.polimi.deib.se2.mp.weathercal.entity.Participation;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 *
 * @author Marco
 */

public class ProvaJUnitTest {
    private searchUser su=new searchUser();
    private EventManagerBean evm=new EventManagerBean();
    public ProvaJUnitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    evm.em=mock(EntityManager.class);
    evm.um=mock(UserManager.class);
    su.setSearched("b@b.it");
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void hello() throws IOException {
    /*    int size=0;
        List ev=new ArrayList();
    Query query=mock(Query.class);
    given(evm.em.createNamedQuery("Participation.findByIdCalendarandAvailability")).willReturn(query);
    given(query.setParameter("idCalendar",12l)).willReturn(query);
    given(query.setParameter("av","si")).willReturn(query);
    given(query.getResultList()).willReturn(ev);*/
  //  when(evm.em.createNamedQuery("Participation.findByIdCalendarandAvailability")).thenReturn(query);
    //assertEquals(1,ev.size());
    System.out.println("the test will pass");
    
    }
}
