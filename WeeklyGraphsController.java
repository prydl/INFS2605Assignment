/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weeklygraphs;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.time.temporal.ChronoUnit;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.TemporalQueries.localDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

/**
 timelog.db
 * @author priyal
 */
public class WeeklyGraphsController implements Initializable {
    
    @FXML
    private Label label;
    
    private final String URL = "jdbc:sqlite:timelog.db";
    private Connection connection;

    @FXML
    private Text WeeklyGraphsTitle;

    @FXML
    private BarChart<String, Double> WeeklyBreakdownGraph;
    
    @FXML
    private LineChart<Number,Number> WeeklyTrendsGraph;
    
    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;
    
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
    
    public String getDate (String query, String attr) throws SQLException {
        
        String date = null;
        
        // create connection
        Connection conn = DriverManager.getConnection(URL);
        
        // create statement
        Statement st = conn.createStatement();
        
        System.out.println("** get particular date from database **");
        
        ResultSet rs1 = st.executeQuery(query);
        while (rs1.next()) {
            date = rs1.getString(attr);
        }
        
        return date;
    }
    
    public int getWeeks (String query1, String query2) throws SQLException, ParseException {
        
        Integer nWeeks = 0;
        String firstdate = null;
        String lastdate = null;
        
        // create connection
        Connection conn = DriverManager.getConnection(URL);
        
        // create statement
        Statement st = conn.createStatement();
        
        System.out.println("** get number of weeks from database **");
        
        ResultSet rs1 = st.executeQuery(query1);
        while (rs1.next()) {
            firstdate = rs1.getString("startDate");
        }
        
        ResultSet rs2 = st.executeQuery(query2);
        while (rs2.next()) {
            lastdate = rs2.getString("startDate");
        }
        
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        LocalDate date1 = LocalDate.parse(firstdate);
        LocalDate date2 = LocalDate.parse(lastdate);
        
        long daysBetween = DAYS.between(date1, date2);
        
        nWeeks = Math.round((daysBetween / 7) + 1);
        
        return nWeeks;
    }
    
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
        
        // WEEKLY BREAKDOWN GRAPH
        // Get the date range of the database entries, calculate number of weeks
        // then retrieve metrics from the database and average per week
        // Visualised in a BarChart
        
        String firstdate = "select startDate from entries "
                         + "order by startDate limit 1";
        String lastdate  = "select startDate from entries "
                         + "order by startDate desc limit 1";
        
        Integer nWeeks = 1;
        try {
            nWeeks = getWeeks(firstdate, lastdate);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException ex) {
            Logger.getLogger(WeeklyGraphsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Show weekly breakdown graph
        String weeklyBD = "select   category, sum(total_duration) "
                        + "from     entries "
                        + "group by category "
                        + "order by sum(total_duration) desc limit 5";
        
        try {
            
            // get array list of events for selected date
            ArrayList<Event> eventsList = getEvents(weeklyBD);
            XYChart.Series series = new XYChart.Series();
            series.setName("Typical hours spent per week");
            
            // for each event in list, add to the bar chart 
            for (Event curr : eventsList) {
                
                String currCategory = curr.eventCategory;
                double currDuration = curr.eventDur / nWeeks;
                
                System.out.println("Added: " + currCategory + ", " + currDuration);
                
                series.getData().add(new XYChart.Data(currCategory, currDuration));
                
                
            }
            
            WeeklyBreakdownGraph.getData().add(series);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        
        // WEEKLY TRENDS GRAPHS
        // Get number of weeks, calculate weekly durations
        // then add each one to the graph
        try {
            String firstWeekStart = getDate(firstdate, "startDate");
            
            int weekNum = 0;
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            
            XYChart.Series studying = new XYChart.Series();
            studying.setName("Study");
            XYChart.Series work = new XYChart.Series();
            work.setName("Work");
            XYChart.Series exercise = new XYChart.Series();
            exercise.setName("Exercise");
            XYChart.Series cooking = new XYChart.Series();
            studying.setName("Cooking");
                    
            
            while (weekNum < nWeeks) {
                
                int newnum = weekNum + 1;
                System.out.println("--- Week " + newnum);
                
                LocalDate firstWeekDate = LocalDate.parse(firstWeekStart);
                
                LocalDate weekStart = firstWeekDate.plusWeeks(weekNum);
                LocalDate weekEnd   = weekStart.plusDays(6);
                
                String weeklyTrends = "select   category, sum(total_duration) "
                                    + "from     (select startDate, category, total_duration "
                                    +           "from entries "
                                    +           "where startDate >= '" + weekStart.toString() + "' "
                                    +           "and startDate <= '" + weekEnd.toString() + "') "
                                    + "where    category = 'Studying' or category = 'Work' "
                                    +           "or category = 'Cooking' or category = 'Exercise' "
                                    + "group by category "
                                    + "order by category";

                try {

                    // get array list of events for selected date
                    ArrayList<Event> eventsList = getEvents(weeklyTrends);
                    int num = weekNum + 1;
                    String currWeek = "Week " + num;
                    
                    // for each event in list, add to the bar chart 
                    for (Event curr : eventsList) {
                        String currCategory = curr.eventCategory;
                        double currDuration = curr.eventDur;

                        System.out.println("Added: " + currCategory + ", " + currDuration + " to " + currWeek);

                        if (null != currCategory) switch (currCategory) {
                            case "Studying":
                                studying.getData().add(new XYChart.Data(currWeek, currDuration));
                                break;
                            case "Work":
                                work.getData().add(new XYChart.Data(currWeek, currDuration));
                                break;
                            case "Cooking":
                                cooking.getData().add(new XYChart.Data(currWeek, currDuration));
                                break;
                            case "Exercise":
                                exercise.getData().add(new XYChart.Data(currWeek, currDuration));
                                break;
                            default:
                                break;
                        }
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                weekNum++;
            }
            
            WeeklyTrendsGraph.getData().addAll(studying, work, cooking, exercise);
            WeeklyTrendsGraph.setVisible(true);
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }    
    
}
