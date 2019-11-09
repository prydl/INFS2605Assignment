/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timegraphs;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import java.util.Date; 
import java.util.Iterator;
import javafx.beans.InvalidationListener;
import javafx.collections.ArrayChangeListener;
import javafx.collections.ObservableArray;


/**
 *
 * @author priyal
 */
public class TimeGraphsController implements Initializable {

    private final String URL = "jdbc:sqlite:timelog.db";
    private Connection connection;

    @FXML
    private Text DailyBreakdownTitle;

    @FXML
    private BarChart<String, Double> DailyBreakdown;

    @FXML
    private NumberAxis hoursAxis;

    @FXML
    private CategoryAxis activityAxis;

    @FXML
    private Button DailyBreakdownRefresh;
    
    public class Event {
        public String eventCategory = "";
        public double eventDur = 0.0;
        
        // constructor
        public Event(String name, double duration) {
            this.eventCategory = name;
            this.eventDur = duration;
        }

        public String getEventCategory() {
            return eventCategory;
        }

        public void setEventCategory(String eventCat) {
            this.eventCategory = eventCat;
        }

        public double getEventDur() {
            return eventDur;
        }

        public void setEventDur(int eventDur) {
            this.eventDur = eventDur;
        }
    }

    //private ArrayList<String> activityArrayList = new ArrayList<>();
    //private ArrayList<Double> hoursArrayList = new ArrayList<>();
    /*
    public ArrayList<String> getDates(String query) throws SQLException {
        // create new EventArray
        ArrayList<String> datesList = new ArrayList<>();
        
        //create connection
        Connection conn = DriverManager.getConnection(URL);

        //create statement
        Statement st = conn.createStatement();

        //write the SQL query to retrieve all pets that are of the species specified in the parameter of this method
        System.out.println("** get dates from database **");

        ResultSet rs = st.executeQuery(query);
        
        // Iterate over result set from query
        while (rs.next()) {
            
            // Get individual attributes from each row
            String currentDate  = rs.getString("doDate");
            datesList.add(currentDate);
        }

        //close connection
        st.close();
        conn.close();
        
        // return array of events
        return datesList;
        
    }
    */
    
    public ArrayList<Event> getEvents(String query) throws SQLException {
        
        // create new EventArray
        ArrayList<Event> eventsList = new ArrayList<>();
        
        //create connection
        Connection conn = DriverManager.getConnection(URL);

        //create statement
        Statement st = conn.createStatement();

        //write the SQL query to retrieve all pets that are of the species specified in the parameter of this method
        System.out.println("** get events from database **");

        ResultSet rs = st.executeQuery(query);
        
        // Iterate over result set from query
        int i = 0;
        while (rs.next()) {
            
            // Get individual attributes from each row
            String currentCategory = rs.getString("category");
            double currentDuration = rs.getDouble("sum(total_duration)");
            
            // Populate new event with attributes and add to event array
            Event currentEvent = new Event(currentCategory, currentDuration);
            eventsList.add(currentEvent);
            i++;
        }

        //close connection 
        st.close();
        conn.close();
        
        // return array of events
        return eventsList;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Show weekly breakdown graph
        String dailyBD =  "select   category, sum(total_duration) "
                        + "from     entries "
                        + "group by category "
                        + "order by sum(total_duration) desc limit 5";
        
        try {
            
            // get array list of events for selected date
            ArrayList<Event> eventsList = getEvents(dailyBD);
            XYChart.Series series = new XYChart.Series();
            series.setName("Top 5 Daily Entries");
                    
            // for each event in list, add to the bar chart 
            for (Event curr : eventsList) {
                
                String currCategory = curr.eventCategory;
                double currDuration = curr.eventDur;
                
                System.out.println("Added: " + currCategory + ", " + currDuration);
                
                series.getData().add(new XYChart.Data(currCategory, currDuration));
                
                
            }
            
            DailyBreakdown.getData().add(series);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        
    }
    /*
    public void addSeriesToBarChart (String name, ArrayList<String> activityArrayList, ArrayList<Double> hoursArrayList){
        XYChart.Series series = new XYChart.Series(); 
        series.setName(name);
        for(int i = 0; i < activityArrayList.size() && i < hoursArrayList.size(); i++) {
            series.getData().add(new XYChart.Data(activityArrayList.get(i),hoursArrayList.get(i))); 
        }
        DailyBreakdown.getData().add(series); 
    }
    */
}
