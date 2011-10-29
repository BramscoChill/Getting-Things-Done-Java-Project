
package javaapplication5;

import controller.Controller;


public class daMain {
    
    public static final Controller controller = new Controller();
    
    public static void main(String[] args) {
        System.out.println("started");
                System.out.println("java class path: " + System.getProperty("java.class.path"));
        System.out.println("java lib path: " + System.getProperty("java.library.path"));
        //DBhandler test = new DBhandler();
        //test.GetAllThoughts();
        //test.DeleteThought(test.AddThought("dddddddddddddddd"));
        
        //Project tmpProject[] = (Project[]) test.GetAllProjects();
        //String meuk = tmpProject[0].getNote();
        //test.DeleteStatus(test.AddStatus(new Status(-1,"bladiebladiebla")));

//        Timestamp stamp = new Timestamp(new Date().getTime());
//        
//        System.out.println(stamp);
//        stamp.setDate(11);
//        System.out.println(stamp);
//        System.out.println(stamp.getDate());
//        System.exit(0);
    }
}
