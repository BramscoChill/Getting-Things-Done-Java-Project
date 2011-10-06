
package controller;

import model.DBhandler;


public class Main {

    public static void main(String[] args) {
        DBhandler test = new DBhandler();
        //test.GetAllThoughts();
        test.DeleteThought(test.AddThought("dddddddddddddddd"));
    }
}
