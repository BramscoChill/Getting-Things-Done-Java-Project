
package controller;

import Model.*;
import model.DBhandler;


public class Main {

    public static void main(String[] args) {
        DBhandler test = new DBhandler();
        //test.GetAllThoughts();
        //test.DeleteThought(test.AddThought("dddddddddddddddd"));
        
        //Project tmpProject[] = (Project[]) test.GetAllProjects();
        //String meuk = tmpProject[0].getNote();
        //test.DeleteStatus(test.AddStatus(new Status(-1,"bladiebladiebla")));
        test.GetAllActions();
        System.out.println();
    }
}
