/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.ColorProperty;
import com.google.gdata.data.calendar.HiddenProperty;
import com.google.gdata.data.calendar.TimeZoneProperty;
import com.google.gdata.client.*;
import com.google.gdata.client.calendar.*;
import com.google.gdata.data.*;
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.calendar.CalendarFeed;
import com.google.gdata.data.extensions.*;
import com.google.gdata.util.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import static view.MainConstants.*;

/**
 *
 * @author Administrator
 */
public class GoogleCalendar {
    //private String gcUsername = "h.dengrave";
    //private String gcPassword = "testtesttest";
    private CalendarService daCalendarService = new CalendarService("GTDcalendarService");
    //private String gcLink = "https://www.google.com/calendar/feeds/default/owncalendars/full";
    
    public GoogleCalendar(){
//        try {
//            //DoStuff();
//            
//        } catch (AuthenticationException ex) {
//            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (MalformedURLException ex) {
//            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ServiceException ex) {
//            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        //System.out.println("creating calendar: " + CreateCalendar());
        CheckCalendarExists();
        DeleteAction(new Action());
    }
    
    public void DoStuff() throws AuthenticationException, MalformedURLException, IOException, ServiceException{
        
    }
    
    private Boolean CheckConnection(){
        CalendarFeed resultFeed = null;
        
        try {
            daCalendarService.setUserCredentials(OPTIONS.getGCUsername(), OPTIONS.getGCPassword());
            URL feedUrl = new URL(OPTIONS.gcLink);
            resultFeed = daCalendarService.getFeed(feedUrl, CalendarFeed.class);
            return true;
        } catch (IOException iOException) {
            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, iOException);
        } catch (ServiceException serviceException) {
            if(serviceException instanceof com.google.gdata.util.InvalidEntryException){
                System.out.println("URL klopt niet waarmee verbinding wordt gemaakt!");
            } else if(serviceException instanceof com.google.gdata.client.GoogleService.InvalidCredentialsException){
                System.out.println("Gebruikersnaam en/of wachtwoord klopt niet!");
            }
            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, serviceException);
        } catch (Exception ex){
            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    private Boolean CheckCalendarExists(){
        CalendarFeed resultFeed = null;
        
        try {
            daCalendarService.setUserCredentials(OPTIONS.getGCUsername(), OPTIONS.getGCPassword());
            URL feedUrl = new URL(OPTIONS.gcLink);
            resultFeed = daCalendarService.getFeed(feedUrl, CalendarFeed.class);
            
        for (int i = 0; i < resultFeed.getEntries().size(); i++) {
          CalendarEntry entry = resultFeed.getEntries().get(i);
          System.out.println("\t" + entry.getTitle().getPlainText() + " - '" + entry.getLinks().get(0).getHref() + "'");
          if(entry.getTitle().getPlainText().trim().toLowerCase().startsWith("gtd")){
              //strNewURL = entry.getLinks().get(0).getHref();
              OPTIONS.setGcPersonalURL(entry.getLinks().get(0).getHref());
              //System.out.println(OPTIONS.getGcPersonalURL());
              return true;
          }
        }
        } catch (IOException iOException) {
            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, iOException);
        } catch (ServiceException serviceException) {
            if(serviceException instanceof com.google.gdata.util.InvalidEntryException){
                System.out.println("URL klopt niet waarmee verbinding wordt gemaakt!");
            } else if(serviceException instanceof com.google.gdata.client.GoogleService.InvalidCredentialsException){
                System.out.println("Gebruikersnaam en/of wachtwoord klopt niet!");
            }
            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, serviceException);
        } catch (Exception ex){
            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    private Boolean CreateCalendar(){
        CalendarFeed resultFeed = null;
        
        try {
            daCalendarService.setUserCredentials(OPTIONS.getGCUsername(), OPTIONS.getGCPassword());
            URL feedUrl = new URL(OPTIONS.gcLink);
            resultFeed = daCalendarService.getFeed(feedUrl, CalendarFeed.class);
            
            Boolean calenExists = false;
        for (int i = 0; i < resultFeed.getEntries().size(); i++) {
          CalendarEntry entry = resultFeed.getEntries().get(i);
          System.out.println("\t" + entry.getTitle().getPlainText() + ", " + entry.getTitle().getPlainText().trim().toLowerCase() + " - '" + entry.getLinks().get(0).getHref() + "'");
          
          if(entry.getTitle().getPlainText().trim().toLowerCase().startsWith("gtd")){
              OPTIONS.setGcPersonalURL(entry.getLinks().get(0).getHref());
              calenExists = true;
              
          }
        }
        
        if(calenExists == false){
            //maakt een nieuwe calender aan
            CalendarEntry calendar = new CalendarEntry();
            calendar.setTitle(new PlainTextConstruct("GTD (Getting Things Done)"));
            calendar.setSummary(new PlainTextConstruct("Deze kalender bevat alle informatie van de GTDne Applicatie."));
            calendar.setTimeZone(new TimeZoneProperty("Europe/Amsterdam"));
            calendar.setHidden(HiddenProperty.TRUE);
            calendar.setColor(new ColorProperty("#2952A3"));
            calendar.addLocation(new Where("","","Netherlands"));

            // Insert the calendar
            URL postUrl = new URL(OPTIONS.gcLink);
            CalendarEntry returnedCalendar = daCalendarService.insert(postUrl, calendar);
            
            return true;
        }
        } catch (IOException iOException) {
            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, iOException);
        } catch (ServiceException serviceException) {
            if(serviceException instanceof com.google.gdata.util.InvalidEntryException){
                System.out.println("URL klopt niet waarmee verbinding wordt gemaakt!");
            } else if(serviceException instanceof com.google.gdata.client.GoogleService.InvalidCredentialsException){
                System.out.println("Gebruikersnaam en/of wachtwoord klopt niet!");
            }
            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, serviceException);
        } catch (Exception ex){
            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public Boolean InsertAction(Action action){
        //@TODO InsertAction calendar functie afmaken
        
        CalendarEventEntry myEntry = new CalendarEventEntry();

        myEntry.setTitle(new PlainTextConstruct("Tennis with Beth"));
        myEntry.setContent(new PlainTextConstruct("Meet for a quick lesson."));

        DateTime startTime = DateTime.parseDateTime("2006-04-17T15:00:00-08:00");
        DateTime endTime = DateTime.parseDateTime("2006-04-17T17:00:00-08:00");
        When eventTimes = new When();
        eventTimes.setStartTime(startTime);
        eventTimes.setEndTime(endTime);
        myEntry.addTime(eventTimes);

        // Send the request and receive the response:
        //CalendarEventEntry insertedEntry = daCalendarService.insert(OPTIONS.gcLink, myEntry);
        return true;
    }
    
    //eerst CheckConnection aanroepe!!! anders gaat t mis!
    public Boolean DeleteAction(Action action){
        //@TODO DeleteAction calendar functie afmaken
        
        CalendarFeed resultFeed = null;
        
        try {
        
        if(OPTIONS.getGcPersonalURL().length() > 1){
            URL newURL = new URL(OPTIONS.getGcPersonalURL());
            //CalendarEventFeed myEventFeeds = daCalendarService.getFeed(newURL, CalendarEventFeed.class);
            Query myQuery = new Query(newURL);
            //we gaan zoeken op de beschrijving
            myQuery.setFullTextQuery(action.getDescription());
            CalendarEventFeed myEventFeeds = daCalendarService.query(myQuery,CalendarEventFeed.class);

            int amountEvents = myEventFeeds.getEntries().size();
            System.out.println("Size in myEvents: " + myEventFeeds.getEntries().size());

            for(int j = 0; j < amountEvents; j++){
                System.out.println(myEventFeeds.getEntries().get(j).getTitle().getPlainText());
                System.out.println(myEventFeeds.getEntries().get(j).getPlainTextContent());
                
                //retrievedEntry.setTitle(new PlainTextConstruct("Important meeting"));
                //verwijderd de entry
                URL editUrl = new URL(myEventFeeds.getEntries().get(j).getEditLink().getHref());
                CalendarEventEntry updatedEntry = (CalendarEventEntry)daCalendarService.update(editUrl, myEventFeeds.getEntries().get(j));
                updatedEntry.delete();
            }
        } else {
            System.out.println("personal url te klein: " + OPTIONS.getGcPersonalURL());
        }
        
        } catch (IOException iOException) {
            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, iOException);
        } catch (ServiceException serviceException) {
            if(serviceException instanceof com.google.gdata.util.InvalidEntryException){
                System.out.println("URL klopt niet waarmee verbinding wordt gemaakt!");
            } else if(serviceException instanceof com.google.gdata.client.GoogleService.InvalidCredentialsException){
                System.out.println("Gebruikersnaam en/of wachtwoord klopt niet!");
            }
            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, serviceException);
        } catch (Exception ex){
            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }
    
    public Boolean UpdateAction(Action action){
        //@TODO UpdatetAction calendar functie afmaken
        return true;
    }
    
}
