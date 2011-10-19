/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class MessageBox {
    
    public MessageBox(){                 
    }
    
    private static int DoOMessage(JFrame frame,String title, String message, int typeMessage){
        Object[] options = {"Ok"};
        int n = JOptionPane.showOptionDialog(frame,
            message,
            title,
            JOptionPane.OK_OPTION,
            typeMessage,
            null,
            options,
            options[0]);
        return n;
    }
    
    private static String DoInputDialog(JFrame frame,String title, String message, Object[] options){
        Object[] possibilities = options; //{"ham", "spam", "yam"};
        String s = (String)JOptionPane.showInputDialog(
                    frame,
                    message,
                    title,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    possibilities,
                    null);
        return s;
    }
    
    private static int DoOkCancelMessage(JFrame frame,String title, String message, int typeMessage){
        Object[] options = {"Ok", "Annuleren"};
        int n = JOptionPane.showOptionDialog(frame,
            message,
            title,
            JOptionPane.OK_CANCEL_OPTION,
            typeMessage,
            null,
            options,
            options[0]);
        return n;
    }
    
    private static int DoYesNoCancelMessage(JFrame frame,String title, String message, int typeMessage){
        Object[] options = {"Ja", "Nee", "Annuleren"};
        int n = JOptionPane.showOptionDialog(frame,
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            typeMessage,
            null,
            options,
            options[0]);
        return n;
    }
    
    private static int DoYesNoMessage(JFrame frame,String title, String message, int typeMessage){
        Object[] options = {"Ja", "Nee"};
        int n = JOptionPane.showOptionDialog(frame,
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            typeMessage,
            null,
            options,
            options[0]);
        return n;
    }
    
    
    public static int DoOkCancelQuestionMessage(JFrame frame,String title, String message){
        return DoOkCancelMessage(frame, title, message,JOptionPane.QUESTION_MESSAGE);
    }
    
    public static int DoYesNoQuestionMessage(JFrame frame,String title, String message){
        return DoYesNoMessage(frame, title, message,JOptionPane.QUESTION_MESSAGE);
    }
    
    public static int DoYesNoCancelQuestionMessage(JFrame frame,String title, String message){
        return DoYesNoCancelMessage(frame, title, message,JOptionPane.QUESTION_MESSAGE);
    }
    
        
    public static int DoOkCancelErrorMessage(JFrame frame,String title, String message){
        return DoOkCancelMessage(frame, title, message,JOptionPane.ERROR_MESSAGE);
    }
    
    public static int DoOkQuestionMessageBox(JFrame frame,String title, String message){
        return DoOMessage(frame, title, message,JOptionPane.QUESTION_MESSAGE);
    }
    
    public static int DoOkErrorMessageBox(JFrame frame,String title, String message){
        return DoOMessage(frame, title, message,JOptionPane.ERROR_MESSAGE);
    }
    
    public static String DoStaticOptionsInputDialog(JFrame frame,String title, String message, Object[] options){
        return DoInputDialog(frame, title, message, options);
    }
    
    public static String DoEnterTextInputDialog(JFrame frame,String title, String message){
        return DoInputDialog(frame, title, message, null);
    }
    
}
