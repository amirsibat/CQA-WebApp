package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;



import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import constants.AppConstants;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import model.Question;
import model.Answer;

public class Topic implements Comparable<Topic> {
	private String topic;
	private double popularity;
	
	
	public Topic(String topic, double popularity)
	{
		this.topic = topic;
		this.popularity = popularity;
	}
	
	public Topic() {
		// TODO Auto-generated constructor stub
	}

	public static Comparator<Topic> COMPARE_BY_TOPIC_ID = new Comparator<Topic>() {
        public int compare(Topic one, Topic other) {
        	int result = Integer.parseInt(other.topic) - Integer.parseInt(one.topic);
            return result;
        }
    };
    
    
	 public int compareTo(Topic comparetopic) {
	        double comparePopularity=((Topic)comparetopic).getPopularity();
	        /* For Ascending order*/
	        double result = comparePopularity-this.popularity; // Descending order
	        
	        if(result == 0)
	        	return 0;
	        else if(result > 0)
	        	return 1;
	        else return -1;
	   
	    };
	
	
	static private void HandleTopic(String topic, double popularity, ArrayList<Topic> topicsArrayList)
	{
		for (Topic t : topicsArrayList)
		{
			if(t.topic.compareTo(topic) == 0)
			{
				t.popularity = t.popularity + popularity;
				return;
			}
			
		}
		
		topicsArrayList.add(new Topic(topic,popularity));
	}
	

	
	/**
	 * @return ArrayList of size 20 at most of Topics mentioned all time.
	 * @throws SQLException if SQL command syntax is not ok, or statement not valid, nullPointer...
	 * @throws NamingException if constants name are not valid.
	 **/
	static public ArrayList<Topic> GetMostPopularTopics()
	{
		try{
			//obtain  data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			ArrayList<Topic> TopicsArrayList = new ArrayList<Topic>();
			Question temp_q = null;
			double questPopularity = 0;
			PreparedStatement stmt;
			
			
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_ALL_QUESTIONS_STMT_NO_USERS);
				
				ResultSet rs = stmt.executeQuery();
				//loop over ResultSet -- 
				while (rs.next()){
					questPopularity = (0.2 * rs.getInt(7) + 0.8 * (Answer.AverageAnswersRate(Integer.parseInt(rs.getString(1)))));
					temp_q = new Question(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7),rs.getInt(8),questPopularity);
					
					HandleTopic(rs.getString(5),questPopularity, TopicsArrayList);
					}
				
				//Close Connections
				rs.close();
				stmt.close();
				conn.close();
				
				Collections.sort(TopicsArrayList);
				
				if(TopicsArrayList.size() > 20)
					TopicsArrayList = new ArrayList<Topic>(TopicsArrayList.subList(0, 20));
				
				return TopicsArrayList;
			}
			catch(SQLException e)
			{
				System.err.println(e.getMessage()); // print error to error stream
			}
			
			}
			catch (SQLException e) {
				System.err.println(e.getMessage()); // print error to error stream
	    	}
	    	catch(NamingException e)
	    	{
	    		System.err.println(e.getMessage()); // print error to error stream
	    	}
			
			return null; // if Debugger reached here means Database is still empty.

	}
	
	
	/**
	 * @return ArrayList of size 5 at most of Topics with highly rated questions & answers for user .
	 * @throws SQLException if SQL command syntax is not ok, or statement not valid, nullPointer...
	 * @throws NamingException if constants name are not valid.
	 **/
	static public ArrayList<Topic> GetExpertise(int userID)
	{
try{
			
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			ArrayList<Question> QuestsArrayList = new ArrayList<Question>();
			ArrayList<Answer> AnswersArrayList = new ArrayList<Answer>();
			ArrayList<Topic> TopicsArrayList = new ArrayList<Topic>();

			PreparedStatement stmt,pstmt;
			
			try {
				
				stmt = conn.prepareStatement(AppConstants.SELECT_ALL_ANSWERS_BY_USERID_STMT);
				stmt.setInt(1,userID);
				
				double questPopularity = 0;
				

				ResultSet rs = stmt.executeQuery();
				//loop over ResultSet and add to ArrayList
				while (rs.next()){ 
											
					int questID = rs.getInt(7); //answer_for_questID
					
					try{
						
						pstmt = conn.prepareStatement(AppConstants.SELECT_QUESTION_BY_QUESTID_STMT);
						pstmt.setInt(1,questID);
						
						ResultSet qrs = pstmt.executeQuery();
						
						while(qrs.next()){
							
							User user= new User(qrs.getString(9),qrs.getString(10),qrs.getString(11),qrs.getString(12),qrs.getString(13), qrs.getString(14));
							questPopularity = (0.2 * qrs.getInt(7) + 0.8 * (Answer.AverageAnswersRate(Integer.parseInt(qrs.getString(1)))));
							AnswersArrayList = Answer.getQuestAnswerByID(Integer.parseInt(qrs.getString(1)),Integer.parseInt(rs.getString(1)));
							QuestsArrayList.add(new Question(qrs.getString(1), qrs.getString(2), qrs.getString(3), qrs.getString(4), qrs.getString(5), qrs.getString(6), qrs.getInt(7),qrs.getInt(8),questPopularity,AnswersArrayList,user));

						}
						
						//Close Connections
						qrs.close();
						pstmt.close();
						
					}catch(SQLException e)
					{
						System.err.println(e.getMessage()); // print error to error stream
					}
					
				}
				//Close Connections
				rs.close();
				stmt.close();
				conn.close();
				
				/*
				 * loop over ArrayList & and insert highly rated topic to arrayList
				*/
				
				for (Question m : QuestsArrayList)
				{
					String topic = m.getTopic();
					double rate = 0;
					boolean flag = false;
					for(Answer q : m.getAnswersArray())	
					{
						rate += q.getRate();
					
					for(Topic t : TopicsArrayList )
					{
						if(t.getTopic().compareTo(topic) == 0)
						{
							t.setPopularity(t.getPopularity() + rate)  ;
							flag =true;
						}	
					 }
					
				}
					if(!flag){
					TopicsArrayList.add(new Topic(topic,rate));
					rate = 0;}
				}
					

				/*
				 *Sort ArrayList By Popularity (from higher to lower) CompareTo was
				 *overrided in advance to support sorting objects
				 *check if ArrayList size bigger than 5, then subList it
				 */		
				Collections.sort(TopicsArrayList);
				
				if(TopicsArrayList.size() > 5)
					TopicsArrayList = new ArrayList<Topic>(TopicsArrayList.subList(0, 5));
				
				return TopicsArrayList;
			}
			catch(SQLException e)
			{
				System.err.println(e.getMessage()); // print error to error stream
			}
			
			}
			catch (SQLException e) {
				System.err.println(e.getMessage()); // print error to error stream
	    	}
	    	catch(NamingException e)
	    	{
	    		System.err.println(e.getMessage()); // print error to error stream
	    	}
			
			return null; // if Debugger reached here means Database is still empty.
	}
	
	
	/* Getters & Setters */
	
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public double getPopularity() {
		return popularity;
	}
	public void setPopularity(double pop) {
		this.popularity = pop;
	}
	
	
	

}
