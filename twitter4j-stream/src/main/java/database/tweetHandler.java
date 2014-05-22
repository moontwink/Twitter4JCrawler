
package database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nancy
 */
public class tweetHandler {
    
    public static String addTweet(tweetModel tm){
        String message = "* Saving Failed.";
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("INSERT INTO `Tweets` "
                    + "(statusId, username, message, retweetcount, latitude, longhitude, date) VALUES (?,?,?,?,?,?,?)"); 
            
            ps.setString(1, tm.getStatusId());
            ps.setString(2, tm.getUsername());
            ps.setString(3, tm.getMessage());
            ps.setLong(4, tm.getRetweetCount());
            ps.setDouble(5, tm.getLatitude());
            ps.setDouble(6, tm.getLonghitude());
            ps.setString(7, tm.getDate());
            
            int i = ps.executeUpdate();
            
            if (i == 1) {
                message = "* Saving successful.";
            }
            
            ps.close();
            c.close();
            
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return message;
        
    }
    
    public static String RewriteTweet(String tweet){
        String filePath = "writetweet.txt";
        String tweetLine = tweet;
        
        //Rewrites tweet to text file
        try{
            Writer write = new Writer(filePath, false);
            write.writeToFile(tweet);
//            System.out.print("__! Rewrite Successful! __");
        }catch(IOException ex){
            System.out.println("__! Sorry, No Can Do!");
        }
      
        //Reades tweet as pure text
        Reader read = new Reader(filePath);
        read.OpenFile();
        tweetLine = read.ReadFile();
        
        return tweetLine;
    }
    
    public static String normalizeTweet(String tweet){
        String tweetLine = tweet;
//        tweetLine.
        return tweetLine;
    }
    
    public static ArrayList<tweetModel> getAllRetweets(){
        ArrayList<tweetModel> results = new ArrayList<tweetModel>();
        tweetModel t;
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM tweets "
                    + "WHERE message like 'RT%' "
                    + "LIMIT 0,10");
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                t = new tweetModel();
                t.setIdTweets(rs.getInt("idTweets"));
                t.setStatusId(Integer.toString(rs.getInt("statusId")));
                t.setUsername(rs.getString("username"));
                t.setMessage(rs.getString("message"));
                t.setRetweetCount(rs.getLong("retweetCount"));
                t.setLatitude(rs.getLong("latitude"));
                t.setLonghitude(rs.getLong("longhitude"));
                t.setDate(rs.getString("date"));
                
                results.add(t);
            }
            
            rs.close();
            ps.close();
            c.close();
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return results;
    }
    
    public static Boolean checkTweet(String username, String message){
        tweetModel tw = null;
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT username, message FROM tweets "+
                    "WHERE username = ? and message = ?");
            ps.setString(1, username);
            ps.setString(2, message);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                tw = new tweetModel();
                tw.setUsername(rs.getString(1));
                tw.setMessage(rs.getString(2));
            }
            
            rs.close();
            ps.close();
            c.close();
            
            if(tw == null){
                return true;    //tweet doesn't exist
            }
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
}
