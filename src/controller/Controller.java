/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Model.GTDcomplete;
import view.GTDmainFrame;

/**
 *
 * @author Administrator
 */
public class Controller {
    private GTDcomplete gtd = new GTDcomplete();
    private GTDmainFrame mainFrames = new GTDmainFrame();
    
    public Controller(){
        
    }
    
    public GTDcomplete getModel(){
        return gtd;
    }
    
}
