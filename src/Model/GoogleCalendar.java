/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;
import com.google.gdata.client.GoogleAuthTokenFactory.UserToken;
import Model.exceptions.GoogleCaptachaAuthenticationError;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import view.MessageBox;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import com.google.gdata.client.GoogleService.CaptchaRequiredException;
import java.util.Date;
import java.util.Iterator;
import java.sql.Timestamp;
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
    private URL feedUrl;
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
        //CheckCalendarExists();
        //DeleteAction(new Action());
    }
    
    public void DoStuff() throws AuthenticationException, MalformedURLException, IOException, ServiceException{
        
    }
    
    public Boolean CheckConnection() throws IOException, ServiceException, GoogleCaptachaAuthenticationError{
        CalendarFeed resultFeed = null;
        
            SetCredentialsAndURL();
            resultFeed = daCalendarService.getFeed(feedUrl, CalendarFeed.class);
            return true;
        
    }
    
    //haalt ook je juiste url van de calender op, checkt dus ook of die bestaat!
    //er is een wazig token nodig ipv login, deze gaat ie opslaan als je 1x de captacha ingevuld hebt
    public void SetCredentialsAndURL() throws IOException, ServiceException, GoogleCaptachaAuthenticationError{
        //als de feedUrl null is of als de link veranderd is, dan moet ie een nieuwe verbinding maken
        if(feedUrl != null || !(new URL(OPTIONS.gcLink).equals(feedUrl))){
            try{
            
            if(OPTIONS.getGcAuthToken().trim().length() > 1){
                daCalendarService.setUserToken(OPTIONS.getGcAuthToken().trim());
            } else {
                daCalendarService.setUserCredentials(OPTIONS.getGCUsername(), OPTIONS.getGCPassword());
            }
            System.out.println("OPTIONS.gcLink: " + OPTIONS.gcLink);
            feedUrl = new URL(OPTIONS.gcLink);
            SetPersonalURL();
            } catch (CaptchaRequiredException e) {
              //System.out.println("Please visit " + e.getCaptchaUrl());
              //System.out.print("Answer to the challenge? ");
              
              ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(LoadImage(e.getCaptchaUrl()).getSource()));
              String answer = MessageBox.DoEnterTextInputDialogWithIcon(null,"Captacha nodig!","Typ de captacha code over: ",icon);
              
              
              
              //BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
              //String answer = in.readLine();
              try{
                daCalendarService.setUserCredentials(OPTIONS.getGCUsername(), OPTIONS.getGCPassword(), e.getCaptchaToken(), answer);
                UserToken auth_token = (UserToken)daCalendarService.getAuthTokenFactory().getAuthToken();
                OPTIONS.setGcAuthToken(auth_token.getValue());
                OPTIONS.SaveOptions();
                feedUrl = new URL(OPTIONS.gcLink);
                CheckCalendarExists();
              } catch (CaptchaRequiredException e2) {
                  throw new GoogleCaptachaAuthenticationError("Captacha code foutief overgetypt, probeer het opnieuw!");
              }

            } catch (AuthenticationException e) {
              System.out.println(e.getMessage());
            }
        }
    }
    
//    public Boolean CheckConnection(){
//        CalendarFeed resultFeed = null;
//        
//        try {
//            daCalendarService.setUserCredentials(OPTIONS.getGCUsername(), OPTIONS.getGCPassword());
//            URL feedUrl = new URL(OPTIONS.gcLink);
//            resultFeed = daCalendarService.getFeed(feedUrl, CalendarFeed.class);
//            return true;
//        } catch (IOException iOException) {
//            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, iOException);
//        } catch (ServiceException serviceException) {
//            if(serviceException instanceof com.google.gdata.util.InvalidEntryException){
//                System.out.println("URL klopt niet waarmee verbinding wordt gemaakt!");
//            } else if(serviceException instanceof com.google.gdata.client.GoogleService.InvalidCredentialsException){
//                System.out.println("Gebruikersnaam en/of wachtwoord klopt niet!");
//            }
//            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, serviceException);
//        } catch (Exception ex){
//            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return false;
//    }
    
    public Boolean CheckCalendarExists() throws IOException, ServiceException, GoogleCaptachaAuthenticationError{
        CalendarFeed resultFeed = null;

            SetCredentialsAndURL();
            
            resultFeed = daCalendarService.getFeed(feedUrl, CalendarFeed.class);
            
        for (int i = 0; i < resultFeed.getEntries().size(); i++) {
          CalendarEntry entry = resultFeed.getEntries().get(i);
          System.out.println("\t" + entry.getTitle().getPlainText() + " - '" + entry.getLinks().get(0).getHref() + "'");
          if(entry.getTitle().getPlainText().trim().toLowerCase().startsWith("gtd")){
              //strNewURL = entry.getLinks().get(0).getHref();
              //stelt je persoonlijke link in!
              OPTIONS.setGcPersonalURL(entry.getLinks().get(0).getHref());
              //System.out.println(OPTIONS.getGcPersonalURL());
              return true;
          }
        }
        
        return false;
    }
    
    //je hebt een persoonlijke url bij google om meuk te bewerken. Deze slaat ie hier intern op
    public Boolean SetPersonalURL() throws IOException, ServiceException, GoogleCaptachaAuthenticationError{
        CalendarFeed resultFeed = null;

            //SetCredentialsAndURL();
            
            resultFeed = daCalendarService.getFeed(feedUrl, CalendarFeed.class);
            
        for (int i = 0; i < resultFeed.getEntries().size(); i++) {
          CalendarEntry entry = resultFeed.getEntries().get(i);
          System.out.println("\t" + entry.getTitle().getPlainText() + " - '" + entry.getLinks().get(0).getHref() + "'");
          if(entry.getTitle().getPlainText().trim().toLowerCase().startsWith("gtd")){
              //strNewURL = entry.getLinks().get(0).getHref();
              //stelt je persoonlijke link in!
              OPTIONS.setGcPersonalURL(entry.getLinks().get(0).getHref());
              //System.out.println(OPTIONS.getGcPersonalURL());
              return true;
          }
        }
        
        return false;
    }
    
    
//    public Boolean CheckCalendarExists(){
//        CalendarFeed resultFeed = null;
//        
//        try {
//            daCalendarService.setUserCredentials(OPTIONS.getGCUsername(), OPTIONS.getGCPassword());
//            URL feedUrl = new URL(OPTIONS.gcLink);
//            resultFeed = daCalendarService.getFeed(feedUrl, CalendarFeed.class);
//            
//        for (int i = 0; i < resultFeed.getEntries().size(); i++) {
//          CalendarEntry entry = resultFeed.getEntries().get(i);
//          System.out.println("\t" + entry.getTitle().getPlainText() + " - '" + entry.getLinks().get(0).getHref() + "'");
//          if(entry.getTitle().getPlainText().trim().toLowerCase().startsWith("gtd")){
//              //strNewURL = entry.getLinks().get(0).getHref();
//              OPTIONS.setGcPersonalURL(entry.getLinks().get(0).getHref());
//              //System.out.println(OPTIONS.getGcPersonalURL());
//              return true;
//          }
//        }
//        } catch (IOException iOException) {
//            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, iOException);
//        } catch (ServiceException serviceException) {
//            if(serviceException instanceof com.google.gdata.util.InvalidEntryException){
//                System.out.println("URL klopt niet waarmee verbinding wordt gemaakt!");
//            } else if(serviceException instanceof com.google.gdata.client.GoogleService.InvalidCredentialsException){
//                System.out.println("Gebruikersnaam en/of wachtwoord klopt niet!");
//            }
//            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, serviceException);
//        } catch (Exception ex){
//            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return false;
//    }
    
    //maakt een nieuwe calender aan
    public Boolean CreateCalendar(){
        CalendarFeed resultFeed = null;
        
        try {
            SetCredentialsAndURL();
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
    
    public Boolean InsertActions(Action[] actions, String typeSync){
        try {
            //@TODO InsertActions calendar functie afmaken
            SetCredentialsAndURL();
                long nineteenhundredyears = 1900 * 365 * 24 * 60 * 60 * 1000;
                long oneYear = 365 * 24 * 60 * 60 * 1000;
                long oneDay = 1 * 24 * 60 * 60 * 1000;
                long oneHour = 60 * 60 * 1000;
            
                //verwijderd eerst alles
                DeleteAllEntrysWithExtendedPropertyDaID();
                
                CalendarEventFeed myFeed = daCalendarService.getFeed(new URL(OPTIONS.getGcPersonalURL()), CalendarEventFeed.class);
                
                for(int i = 0; i < actions.length; i++){
                    //als het type 2 is en de datum in het heden ligt dan moet ie syncen
                    if((typeSync.equalsIgnoreCase(OPTIONSGCSYNCTYPRVALUES[0])) || 
                            (typeSync.equalsIgnoreCase(OPTIONSGCSYNCTYPRVALUES[1]) && actions[i].getDatumTijd().after(new Date()))){
                        CalendarEventEntry myEntry = new CalendarEventEntry();

                        myEntry.setTitle(new PlainTextConstruct(actions[i].getDescription()));
                        myEntry.setContent(new PlainTextConstruct(actions[i].getDescription() + "\nProject: " + actions[i].getProject().getName()
                                + "\nContext: " + actions[i].getContext().getName() + "\nStatus: " + actions[i].getStatus().getName()
                                + "\n\nBeschrijving: " + actions[i].getNote()
                                ));

                        ExtendedProperty property = new ExtendedProperty();
                        property.setName(EXTENDEDPROPERTYGCIDENTRY);
                        property.setValue("" + actions[i].getID());

                        myEntry.addExtendedProperty(property);

                        When eventTimes = new When();

                        Timestamp actionStamp = actions[i].getDatumTijd();
                        long daStamp = actions[i].getDatumTijd().getTime() - nineteenhundredyears;
                        actionStamp.setYear(actionStamp.getYear()-1900);

                        final SimpleDateFormat mTimestampZ =
                            new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss");
                        String startRFC822 = mTimestampZ.format(new Date(actionStamp.getTime()));
                        //System.out.println("event as RFC822: " + startRFC822);
                        DateTime startTime = DateTime.parseDateTime(startRFC822);
                        
                        eventTimes.setStartTime(new DateTime(actionStamp));

                        actionStamp.setTime(actionStamp.getTime() + oneHour + oneHour);
                        //DateTime endTime = DateTime.parseDateTime(newTimeStamp.toString());
                        String endRFC822 = mTimestampZ.format(new Date(actionStamp.getTime()));
                        DateTime endTime = DateTime.parseDateTime(endRFC822);
                        
                        eventTimes.setEndTime(new DateTime(actionStamp));
                        


                        //eventTimes.setStartTime(new DateTime(actionStamp));
                        //eventTimes.setEndTime(DateTime.parseDateTime(endRFC822));
            //            eventTimes.setStartTime(startTime);
            //            eventTimes.setEndTime(endTime);
            //            eventTimes.setStartTime(DateTime.parseDateTime("2011-10-01T15:00:00"));
            //            eventTimes.setEndTime(DateTime.parseDateTime("2011-10-01T16:33:00"));

                        //myEntry.addTime(eventTimes);

                        // Send the request and receive the response:
                        //System.out.println("event startTime: " + startTime.toStringRfc822() + ", event endtime: " + endTime.toUiString());
                        //System.out.println("event startRFC822 startTime: " + startRFC822 + ", event startRFC822 endtime: " + endRFC822);

                        myEntry.addTime(eventTimes);

                        
                        CalendarEventEntry insertedEntry = myFeed.insert(myEntry);
                        actionStamp.setYear(actionStamp.getYear()+1900);
                        System.out.println("added entry: " + insertedEntry.getTitle());
                    }
                }
            
            
            

            return true;
        } catch (GoogleCaptachaAuthenticationError ex) {
            ex.printStackTrace();
            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServiceException ex) {
            ex.printStackTrace();
            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
   public List<CalendarEventEntry> GettAllActionEntrys(){
        //@TODO InsertActions calendar functie afmaken
       
        try {
            
            CalendarEventEntry myEntry = new CalendarEventEntry();
            SetCredentialsAndURL();

            //persoonlijke URL!
            CalendarQuery myQuery = new CalendarQuery(new URL(OPTIONS.getGcPersonalURL()));
            
            long oneYear = 365 * 24 * 60 * 60 * 1000;
            long oneDay = 1 * 24 * 60 * 60 * 1000;
            long oneHour = 60 * 60 * 1000;
            //myQuery.setMinimumStartTime(new DateTime(new Date().getTime() - oneYear));
            //myQuery.setMaximumStartTime(new DateTime(new Date().getTime() + oneYear)); //(DateTime.parseDateTime("2012-03-24T23:59:59"));

            CalendarEventFeed resultFeed = daCalendarService.query(myQuery,CalendarEventFeed.class);

            int amountEvents = resultFeed.getEntries().size();

            //System.out.println("aantal events: " + amountEvents);
            
            return resultFeed.getEntries();
            //Iterator li = resultFeed.getEntries().iterator();
            
            
            
//            while (li.hasNext()) {
//                CalendarEventEntry cee = (CalendarEventEntry) li.next();
//                System.out.println( cee.getTitle().getPlainText() );
//                for(ExtendedProperty ep : cee.getExtendedProperty()){
//                    System.out.println("extended property name: " + ep.getName() + " value: " + ep.getValue());
//                }
//                
//                System.out.println();
//            }
        
        } catch (IOException ex) {
            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServiceException ex) {
            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GoogleCaptachaAuthenticationError ex){
            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//        
//        myEntry.setTitle(new PlainTextConstruct(action.getDescription()));
//        myEntry.setContent(new PlainTextConstruct(action.getDescription() + "\nProject: " + action.getProject().getName()
//                + "\nContext: " + action.getContext().getName() + "\nStatus: " + action.getStatus().getName()
//                + "\n\nBeschrijving: " + action.getNote()
//                ));
//
////        DateTime startTime = DateTime.parseDateTime("2006-04-17T15:00:00-08:00");
////        DateTime endTime = DateTime.parseDateTime("2006-04-17T17:00:00-08:00");
//        DateTime startTime = DateTime.parseDateTime(action.getDatumTijd().toString());
//
//        long oneDay = 1 * 24 * 60 * 60 * 1000;
//        long oneHour = 60 * 60 * 1000;
//        Timestamp newTimeStamp = action.getDatumTijd();
//        newTimeStamp.setTime(newTimeStamp.getTime() + oneHour);
//        DateTime endTime = DateTime.parseDateTime(newTimeStamp.toString());
//        
//        When eventTimes = new When();
//        eventTimes.setStartTime(startTime);
//        eventTimes.setEndTime(endTime);
//        myEntry.addTime(eventTimes);

        // Send the request and receive the response:
        //CalendarEventEntry insertedEntry = daCalendarService.insert(OPTIONS.gcLink, myEntry);
        return null;
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
    
    public Boolean DeleteAllEntrysWithExtendedPropertyDaID(){
        //@TODO DeleteAction calendar functie afmaken
        
        
        try {
            SetCredentialsAndURL();
            
            URL newURL = new URL(OPTIONS.getGcPersonalURL());
            
            List<CalendarEventEntry> allEntrys = GettAllActionEntrys();
            
            if(allEntrys != null){
                
            }
            Iterator li = allEntrys.iterator();
            
            
            
            while (li.hasNext()) {
                CalendarEventEntry cee = (CalendarEventEntry) li.next();
                Boolean gotRightEntry = false;
                //System.out.println( cee.getTitle().getPlainText() );
                for(ExtendedProperty ep : cee.getExtendedProperty()){
                    if(ep.getName().equalsIgnoreCase(EXTENDEDPROPERTYGCIDENTRY)){
                        gotRightEntry = true;
                        //System.out.println("Ready to delete:" + cee.getTitle().getPlainText() );
                    }
                    //System.out.println("extended property name: " + ep.getName() + " value: " + ep.getValue());
                    
                    if(gotRightEntry){
                        URL editUrl = new URL(cee.getEditLink().getHref());
                        
//                        CalendarEventFeed myFeed = daCalendarService.getFeed(new URL(OPTIONS.getGcPersonalURL()), CalendarEventFeed.class);
//                        CalendarEventEntry insertedEntry = myFeed.insert(cee);
                        
                        CalendarEventEntry updatedEntry = (CalendarEventEntry)daCalendarService.update(editUrl, cee);
                        updatedEntry.delete();
                        //updatedEntry = (CalendarEventEntry)daCalendarService.update(editUrl, cee);
                        //System.out.println("Deleted:" + cee.getTitle().getPlainText() + ", url: " + cee.getEditLink().getHref());
                        
                    }
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

        return true;
    }
    
    public Boolean UpdateAction(Action action){
        //@TODO UpdatetAction calendar functie afmaken
        return true;
    }
    
    public Boolean UpdateActions(Action[] actions){
        //@TODO UpdateActions afmaken
        return false;
    }
    
}
