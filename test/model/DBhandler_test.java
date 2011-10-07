/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import Model.Database.*;
import Model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
public class DBhandler_test {
    
    DBhandler test;
    Model.Action daAction;
    Model.Action newAction;
    
    public DBhandler_test() {
    }
    
    @Before
    public void setUp() {
        test = new DBhandler();
        daAction = new Model.Action();
    }
    
    @After
    public void tearDown() {
    }
    

    @Test
    public void UpdateExistingAction() throws Exception{
        
        //test.GetAllThoughts();
        //test.DeleteThought(test.AddThought("dddddddddddddddd"));
        
        //Project tmpProject[] = (Project[]) test.GetAllProjects();
        //String meuk = tmpProject[0].getNote();
        //test.DeleteStatus(test.AddStatus(new Status(-1,"bladiebladiebla")));
        
        daAction.setID(1);
        daAction.setDescription("bier halen");
        daAction.setNote("moet nog bier gaan halen bij de aldi. Uiteraard schultenbrau!");
        daAction.setStatus(new Status(2,"Uitstellen"));
        daAction.setContext(null);
        daAction.setProject(new Project(1,"feesje", "feesje bouwen"));
        daAction.setDatumTijd(java.sql.Timestamp.valueOf("2011-11-07 12:00:00"));
        daAction.setStatusChanged(java.sql.Timestamp.valueOf("2011-10-07 11:54:42"));
        daAction.setDone(false);
        newAction = test.AddAction(daAction);
        assertEquals(daAction.getID(), newAction.getID());
    }
    
    @Test
    public void AddNewAction() throws Exception{
        
        //test.GetAllThoughts();
        //test.DeleteThought(test.AddThought("dddddddddddddddd"));
        
        //Project tmpProject[] = (Project[]) test.GetAllProjects();
        //String meuk = tmpProject[0].getNote();
        //test.DeleteStatus(test.AddStatus(new Status(-1,"bladiebladiebla")));
        
        //daAction.setID(1);
        daAction = new Model.Action();
        daAction.setDescription("bier halen");
        daAction.setNote("moet nog bier gaan halen bij de aldi. Uiteraard schultenbrau!");
        daAction.setStatus(new Model.Status(2,"Uitstellen"));
        daAction.setContext(null);
        daAction.setProject(new Model.Project(1,"feesje", "feesje bouwen"));
        daAction.setDatumTijd(java.sql.Timestamp.valueOf("2011-11-07 12:00:00"));
        daAction.setStatusChanged(java.sql.Timestamp.valueOf("2011-10-07 11:54:42"));
        daAction.setDone(false);
        newAction = test.AddAction(daAction);
        assertNotSame(daAction.getID(), newAction.getID());
    }
}
