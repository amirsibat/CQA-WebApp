package model;

import java.math.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


import java.util.LinkedHashSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import constants.AppConstants;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import model.TimeHelper;
import model.User;
import model.Question;


public class Question implements Comparable<Question> {
	private String questID;
	private String Posted_by_userID;
	private String Posted_by_Nickname;
	private String pubDate;
	private String Topic;
	private String Content;
	private int Rate;
	private int Answers_count;
	private double popularity;
	
	private ArrayList<Answer> AnswersArray ;
	
	private User user;
	public Question(String questID,String Posted_by_userID, String Posted_by_Nickname, String pubDate,String Topic, String Content,int Rate, int Answers_count)
	{
		this.setQuestID(questID);
		this.setPosted_by_userID(Posted_by_userID);
		this.setPosted_by_Nickname(Posted_by_Nickname);
		this.setDate(pubDate);
		this.setTopic(Topic);
		this.setContent(Content);
		this.setRate(Rate);
		this.setAnswers_count(Answers_count);		
	}
	
	public Question(String questID,String Posted_by_userID, String Posted_by_Nickname, String pubDate,String Topic, String Content,int Rate, int Answers_count,double popularity)
	{
		this.setQuestID(questID);
		this.setPosted_by_userID(Posted_by_userID);
		this.setPosted_by_Nickname(Posted_by_Nickname);
		this.setDate(pubDate);
		this.setTopic(Topic);
		this.setContent(Content);
		this.setRate(Rate);
		this.setAnswers_count(Answers_count);
		this.setPopularity(popularity);
	}
	
	public Question(String questID,String Posted_by_userID, String Posted_by_Nickname, String pubDate,String Topic, String Content,int Rate, int Answers_count, User user)
	{
		this.setQuestID(questID);
		this.setPosted_by_userID(Posted_by_userID);
		this.setPosted_by_Nickname(Posted_by_Nickname);
		this.setDate(pubDate);
		this.setTopic(Topic);
		this.setContent(Content);
		this.setRate(Rate);
		this.setAnswers_count(Answers_count);
		this.setUserOfQuestion(new User(user.getUsername(), user.getPassword(), user.getNickname(), user.getDescription(), user.getPhotoURL()));
	}
	
	public Question(String questID,String Posted_by_userID, String Posted_by_Nickname, String pubDate,String Topic, String Content,int Rate, int Answers_count, double popularity, User user)
	{
		this.setQuestID(questID);
		this.setPosted_by_userID(Posted_by_userID);
		this.setPosted_by_Nickname(Posted_by_Nickname);
		this.setDate(pubDate);
		this.setTopic(Topic);
		this.setContent(Content);
		this.setRate(Rate);
		this.setAnswers_count(Answers_count);
		this.setPopularity(popularity);
		this.setUserOfQuestion(new User(user.getUsername(), user.getPassword(), user.getNickname(), user.getDescription(), user.getPhotoURL()));	
	}
	
	
	public Question(String questID,String Posted_by_userID, String Posted_by_Nickname, String pubDate,String Topic, String Content,int Rate, int Answers_count,double popularity,ArrayList<Answer> AnswersArray, User user)
	{
		this.setQuestID(questID);
		this.setPosted_by_userID(Posted_by_userID);
		this.setPosted_by_Nickname(Posted_by_Nickname);
		this.setDate(pubDate);
		this.setTopic(Topic);
		this.setContent(Content);
		this.setRate(Rate);
		this.setAnswers_count(Answers_count);
		this.setPopularity(popularity);
		this.setAnswersArray(AnswersArray);
		this.setUserOfQuestion(new User(user.getUsername(), user.getPassword(), user.getNickname(), user.getDescription(), user.getPhotoURL()));	
	}
	

	
	public static Comparator<Question> COMPARE_BY_QUEST_ID = new Comparator<Question>() {
        public int compare(Question one, Question other) {
        	int result = Integer.parseInt(other.questID) - Integer.parseInt(one.questID);
            return result;
        }
    };
	
	
    public int compareTo(Question comparequest) {
        double comparePopularity=((Question)comparequest).getPopularity();
        
        double result = comparePopularity-this.popularity; // Descending order
        
        if(result == 0)
        	return 0;
        else if(result > 0)
        	return 1;
        else return -1;
    }
    
    public Question() {
		// TODO Auto-generated constructor stub
	}

    
	/**
	 * @param Posted_by_userID the user id
	 * @param  Nickname user nickname
	 * @param  Topic question topic
	 * @param  Content question content
	 * @param  Rate question rating
	 * @param  answers_count question's answers count
	 * @return True if the question was posted successfully.
	 * @throws SQLException SQL syntax error,NullPointer,etc
	 * @throws NamingException if constants name are not valid.
	 **/
	static public boolean PostNewQuest(int Posted_by_userID, String Nickname ,String Topic, String Content,int Rate, int answers_count)
	{
		boolean added=false;
		
		
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			PreparedStatement stmt;
			
			try {
				
					
					stmt = conn.prepareStatement(AppConstants.INSERT_QUEST_TO_QUESTIONS_STMT);
					
					Date dNow = new Date();
				    SimpleDateFormat ft = new SimpleDateFormat ("dd/MM/yyyy HH:mm");
					
				    stmt.setInt(1, Posted_by_userID);
				    stmt.setString(2, Nickname);
					stmt.setString(3, ft.format(dNow)); // format date to String
					stmt.setString(4, Topic);
					stmt.setString(5, Content);
					stmt.setInt(6, Rate); //should be 0
					stmt.setInt(7, answers_count); //should be 0
					
					
					
					//execute query commit changes and close insert statement
					stmt.executeUpdate();
					conn.commit();
					stmt.close();
					
					added = true;
				
				//close connection with DB
				conn.close();
				
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
		
		return added;
	}
	

	
	
	/**
	 * @return ArrayList of last 20 questions posted by all users.
	 * @throws ParseException if Parse was failed
	 * @throws SQLException SQL syntax error,NullPointer,etc
	 * @throws NamingException if constants name are not valid.
	 **/
	static public ArrayList<Question> getTop20NewQuestions(){
		
		try{
			
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			ArrayList<Question> QuestsArrayList = new ArrayList<Question>();
			PreparedStatement stmt;
			
			try {
				
				stmt = conn.prepareStatement(AppConstants.SELECT_ALL_NEWQUESTIONS_STMT);
				stmt.setMaxRows(20); //we need top 20 by date
				
				double questPopularity = 0;
				ResultSet rs = stmt.executeQuery();
				
				
				//loop over ResultSet and add to ArrayList
				while (rs.next()){
						if( rs.getInt(8) == 0)
							{
								User user= new User(rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13), rs.getString(14));
								QuestsArrayList.add(new Question(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7),rs.getInt(8),questPopularity, user));
							}
						}
				
				//Close Connections
				rs.close();
				stmt.close();
				conn.close();
				
				Date dNow = new Date(); // DateTime Now
				long diffInMillis;
				
				/*
				 * loop over ArrayList & parse Date according to how long ago the question
				 * was posted using a class TimeHelper.
				*/
				
				for (Question q : QuestsArrayList)
				{
					DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				try{
					Date postDate = formatter.parse(q.getDate());
					diffInMillis = dNow.getTime() - postDate.getTime(); // calculate difference in MilliSeconds relative to 1970..
					q.setDate( TimeHelper.millisToLongDHMS(diffInMillis,q.getDate()) ); // Call Time Helper and set the new date in the array
				
					}catch(ParseException ex)
						{
					System.err.println(ex.getMessage()); // print error to error stream
						}
					
				}
				
				
				if(QuestsArrayList.size() > 20)
					QuestsArrayList = new ArrayList<Question>(QuestsArrayList.subList(0, 20));
				
				
				return QuestsArrayList;
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
	
	
	
	// call this func to Add rating to question
		/**
		 * @param userID user user Id
		 * @param questID question id
		 * @return True if question was rated successfully.
		 * @throws SQLException SQL syntax error,NullPointer,...
		 * @throws NamingException if constants name are not valid.
		 **/

	public boolean LikeQuestion(int userID, int questID)
	{
		boolean rated=false;
		
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			PreparedStatement stmt,pstmt,fstmt;
			
			try {
				
					pstmt = conn.prepareStatement(AppConstants.SELECT_USER_AND_QUESTION_FROM_LIKEQUEST_STMT);
					pstmt.setInt(1,userID);
					pstmt.setInt(2, questID);
					
					ResultSet rs = pstmt.executeQuery();
					
					// userID didnt like questID already then perform insert query
					if(rs.next() == false){
					
						stmt = conn.prepareStatement(AppConstants.INSERT_USER_TO_LIKEQUEST_STMT);
						stmt.setInt(1,userID);
						stmt.setInt(2,questID);
						
						fstmt = conn.prepareStatement(AppConstants.UPDATE_QUESTION_RATE_LIKE_STMT);
						fstmt.setInt(1,questID);
						
						//execute query commit changes and close insert statement
						
						fstmt.executeUpdate();
						stmt.executeUpdate();
						conn.commit();
					
						
						fstmt.close();
						stmt.close();
						
						
						rated = true;
					}
				
					//release rs object close select statement and close connection with DB
					
					rs.close();
					pstmt.close();
					conn.close();
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
		
		return (rated);
		
	}
	
	
	/**
	 * @param userID user user Id
	 * @param questID question id
	 * @return True if question was rated successfully.
	 * @throws SQLException SQL syntax error,NullPointer,...
	 * @throws NamingException if constants name are not valid.
	 **/
	public boolean DisLikeQuestion(int userID,int questID)
	{
		boolean rated=false;
		
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			PreparedStatement stmt,pstmt,fstmt;
			
			try {
				
					pstmt = conn.prepareStatement(AppConstants.SELECT_USER_AND_QUESTION_FROM_DISLIKEQUEST_STMT);
					pstmt.setInt(1,userID);
					pstmt.setInt(2, questID);
					
					ResultSet rs = pstmt.executeQuery();
					
					// userID didnt dislike questID already then perform insert query
					if(rs.next() == false){
					
						stmt = conn.prepareStatement(AppConstants.INSERT_USER_TO_DISLIKEQUEST_STMT);
						stmt.setInt(1,userID);
						stmt.setInt(2, questID);
						
						fstmt = conn.prepareStatement(AppConstants.UPDATE_QUESTION_RATE_DISLIKE_STMT);
						fstmt.setInt(1,questID);
						
						//execute query commit changes and close insert statement
						fstmt.executeUpdate();
						stmt.executeUpdate();
						conn.commit();
												
						fstmt.close();
						stmt.close();
						
						rated = true;
					}
					
					//release rs object close select statement and close connection with DB
					rs.close();
					pstmt.close();
					conn.close();
				
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
		
		return (rated);
		
	}
	
	/**
	 * @param quest_topic question topic
	 * @param topic topic to compare
	 * @return true if topic exist, false otherwise
	 **/
	static private boolean isTopicExist(String quest_topic, String topic)
	{

				return quest_topic.equals(topic);
			
	}
	
	/**
	 * @param  topic question topic
	 * @return ArrayList of size 20 Highly Rated Questions containing the Topic
	 * @throws SQLException if SQL command syntax is not ok, or statement not valid, nullPointer
	 * @throws ParseException if Parse was failed
	 * @throws NamingException if constants name are not valid.
	 **/
	static public ArrayList<Question> GetQuestionsByTopic(String topic)
	{
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			ArrayList<Question> QuestsArrayList = new ArrayList<Question>();
			ArrayList<Answer> AnswersArrayList = new ArrayList<Answer>();
			PreparedStatement stmt;
			
			
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_ALL_QUESTIONS_STMT);
				
				ResultSet rs = stmt.executeQuery();
				
				double questPopularity = 0;
				//loop over ResultSet -- INNER JOIN
				while (rs.next()){
					if(isTopicExist(rs.getString(5),topic))
					{
						User user= new User(rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13), rs.getString(14));
						questPopularity = (0.2 * rs.getInt(7) + 0.8 * (Answer.AverageAnswersRate(Integer.parseInt(rs.getString(1)))));
						AnswersArrayList = Answer.getQuestAnswers(Integer.parseInt(rs.getString(1)));
						QuestsArrayList.add(new Question(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7),rs.getInt(8),questPopularity,AnswersArrayList ,user));
					}
					}
				
				//Close Connections
				rs.close();
				stmt.close();
				conn.close();
				
				Date dNow = new Date(); // DateTime Now
				long diffInMillis;
				
				//loop over ArrayList
				for (Question m : QuestsArrayList)
				{
					DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
					Date postDate = formatter.parse(m.getDate());
					diffInMillis = dNow.getTime() - postDate.getTime(); // calculate difference in MilliSeconds relative to 1970..
					m.setDate( TimeHelper.millisToLongDHMS(diffInMillis,m.getDate()) ); // Call Time Helper and set the new date in the array
				}
				
				/*
				 *Sort ArrayList By Popularity (from higher to lower) CompareTo was
				 *overrided in advance to support sorting objects
				 *check if ArrayList size bigger than 20, then subList it
				 */
				Collections.sort(QuestsArrayList);
				if(QuestsArrayList.size() > 20)
					QuestsArrayList = new ArrayList<Question>(QuestsArrayList.subList(0, 20));
				
				return QuestsArrayList;
			}
			catch(SQLException e)
			{
				System.err.println(e.getMessage()); // print error to error stream
			} catch (ParseException e) {
				System.err.println(e.getMessage());
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
	 * @return ArrayList of all questions in the Database.
	 * @throws SQLException SQL syntax error,NullPointer
	 * @throws ParseException if Parse was failed
	 * @throws NamingException if constants name are not valid.
	 **/
	static public ArrayList<Question> getAllQuestions()
	{
		
		try{
			
		//obtain DB data source from Tomcat's context
		Context context = new InitialContext();
		
		BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
		Connection conn = ds.getConnection();
		
		ArrayList<Question> QuestsArrayList = new ArrayList<Question>();
		ArrayList<Answer> AnswersArrayList = new ArrayList<Answer>();
		
		PreparedStatement stmt;
		
		try {
			double questPopularity = 0,to_pop=0;
			stmt = conn.prepareStatement(AppConstants.SELECT_ALL_QUESTIONS_STMT);
			
			

			ResultSet rs = stmt.executeQuery();
			//loop over ResultSet and add to ArrayList
			while (rs.next()){ 

				User user= new User(rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13), rs.getString(14));
				to_pop = (0.2 * rs.getInt(7) + 0.8 * (Answer.AverageAnswersRate(Integer.parseInt(rs.getString(1)))));
				questPopularity = new BigDecimal(to_pop).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
				AnswersArrayList = Answer.getQuestAnswers(Integer.parseInt(rs.getString(1)));
				QuestsArrayList.add(new Question(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7),rs.getInt(8),questPopularity,AnswersArrayList ,user));
				
				
			}
			//Close Connections
			rs.close();
			stmt.close();
			conn.close();
			
			Date dNow = new Date(); // DateTime Now
			long diffInMillis;
			
			/*
			 * loop over ArrayList & parse Date according to how long ago the question
			 * was posted using a class TimeHelper.
			*/
			for (Question m : QuestsArrayList)
			{
			 try{
				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				Date postDate = formatter.parse(m.getDate());
				diffInMillis = dNow.getTime() - postDate.getTime(); // calculate difference in MilliSeconds relative to 1970..
				m.setDate( TimeHelper.millisToLongDHMS(diffInMillis,m.getDate()) ); // Call Time Helper and set the new date in the array
			 }catch(ParseException e){
				 
				 System.err.println(e.getMessage()); // print error to error stream
			 }
			 }
			
			/*
			 *Sort ArrayList By Popularity (from higher to lower) CompareTo was
			 *overrided in advance to support sorting objects
			 *check if ArrayList size bigger than 20, then subList it
			 */
			
			Collections.sort(QuestsArrayList);
			if(QuestsArrayList.size() > 20)
				QuestsArrayList = new ArrayList<Question>(QuestsArrayList.subList(0, 20));
			
			
			
			
			return QuestsArrayList;
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
	 * @param  userID id of user.
	 * @return ArrayList of Top 5 Rated question posted by user.
	 * @throws SQLException SQL syntax error,NullPointer
	 * @throws ParseException if Parse was failed
	 * @throws NamingException if constants name are not valid.
	 **/
	static public ArrayList<Question> getTop5QuestsOfUserID(int userID)
	{
		
				try{
					
				//obtain DB data source from Tomcat's context
				Context context = new InitialContext();
				
				BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
				Connection conn = ds.getConnection();
				
				ArrayList<Question> QuestsArrayList = new ArrayList<Question>();
				ArrayList<Answer> AnswersArrayList = new ArrayList<Answer>();
				
				PreparedStatement stmt;
				
				try {
					
					stmt = conn.prepareStatement(AppConstants.SELECT_ALL_QUESTIONS_BY_USERID_STMT);
					stmt.setInt(1,userID);
					
					double questPopularity = 0;
					

					ResultSet rs = stmt.executeQuery();
					//loop over ResultSet and add to ArrayList
					while (rs.next()){ 

						User user= new User(rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13), rs.getString(14));
						questPopularity = (0.2 * rs.getInt(7) + 0.8 * (Answer.AverageAnswersRate(Integer.parseInt(rs.getString(1)))));
						AnswersArrayList = Answer.getQuestAnswers(Integer.parseInt(rs.getString(1)));
						QuestsArrayList.add(new Question(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7),rs.getInt(8),questPopularity,AnswersArrayList,user));
						
						
					}
					//Close Connections
					rs.close();
					stmt.close();
					conn.close();
					
					//remove duplicates
					QuestsArrayList = new ArrayList<Question>(new LinkedHashSet<Question>(QuestsArrayList));
							
							
					Date dNow = new Date(); // DateTime Now
					long diffInMillis;
					
					/*
					 * loop over ArrayList & parse Date according to how long ago the question
					 * was posted using a class TimeHelper.
					*/
					for (Question m : QuestsArrayList)
					{
					 try{
						DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
						Date postDate = formatter.parse(m.getDate());
						diffInMillis = dNow.getTime() - postDate.getTime(); // calculate difference in MilliSeconds relative to 1970..
						m.setDate( TimeHelper.millisToLongDHMS(diffInMillis,m.getDate()) ); // Call Time Helper and set the new date in the array
					 }catch(ParseException e){
						 
						 System.err.println(e.getMessage()); // print error to error stream
					 }
					 }
					
					
					/*
					 *Sort ArrayList By Popularity (from higher to lower) CompareTo was
					 *overrided in advance to support sorting objects
					 *check if ArrayList size bigger than 5, then subList it
					 */
					
					Collections.sort(QuestsArrayList);
					
					if(QuestsArrayList.size() > 5)
						QuestsArrayList = new ArrayList<Question>(QuestsArrayList.subList(0, 5));

					
					return QuestsArrayList;
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
	 * @param  userID id of user.
	 * @return ArrayList of Top 5 Rated answers posted by user.
	 * @throws SQLException SQL syntax error,NullPointer
	 * @throws ParseException if Parse was failed
	 * @throws NamingException if constants name are not valid.
	 **/
	static public ArrayList<Question> getTop5AnswersOfUserID(int userID){
		
		try{
			
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			ArrayList<Question> QuestsArrayList = new ArrayList<Question>();
			ArrayList<Answer> AnswersArrayList = new ArrayList<Answer>();
			
			PreparedStatement stmt,pstmt;
			
			try {
				
				stmt = conn.prepareStatement(AppConstants.SELECT_ALL_ANSWERS_BY_USERID_STMT);
				stmt.setInt(1,userID);
				
				double questPopularity = 0,to_pop=0;
				

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
							to_pop = (0.2 * qrs.getInt(7) + 0.8 * (Answer.AverageAnswersRate(Integer.parseInt(qrs.getString(1)))));
							questPopularity = new BigDecimal(to_pop).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
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
				
				Date dNow = new Date(); // DateTime Now
				long diffInMillis;
				
				/*
				 * loop over ArrayList & parse Date according to how long ago the question
				 * was posted using a class TimeHelper.
				*/
				for (Question m : QuestsArrayList)
				{
				 try{
					DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
					Date postDate = formatter.parse(m.getDate());
					diffInMillis = dNow.getTime() - postDate.getTime(); // calculate difference in MilliSeconds relative to 1970..
					m.setDate( TimeHelper.millisToLongDHMS(diffInMillis,m.getDate()) ); // Call Time Helper and set the new date in the array
				 }catch(ParseException e){
					 
					 System.err.println(e.getMessage()); // print error to error stream
				 }
				 }
				
				/*
				 *Sort ArrayList By Popularity (from higher to lower) CompareTo was
				 *overrided in advance to support sorting objects
				 *check if ArrayList size bigger than 5, then subList it
				 */
				
				Collections.sort(QuestsArrayList);
				if(QuestsArrayList.size() > 5)
					QuestsArrayList = new ArrayList<Question>(QuestsArrayList.subList(0, 5));
				
			
				
				
				return QuestsArrayList;
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

	public void setQuestID(String questID) {
		this.questID = questID;
	}

	public String getQuestID() {
		return questID;
	}
	
	public String getPosted_by_userID() {
		return Posted_by_userID;
	}


	
	public void setPosted_by_userID(String posted_by_userID) {
		Posted_by_userID = posted_by_userID;
	}


	
	public String getContent() {
		return Content;
	}


	
	public void setContent(String content) {
		Content = content;
	}

	public String getTopic() {
		return Topic;
	}
	
	
	
	public void setTopic(String topic) {
		Topic = topic;
	}
	


	public String getDate() {
		return pubDate;
	}

	public void setDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getPosted_by_Nickname() {
		return Posted_by_Nickname;
	}

	public void setPosted_by_Nickname(String posted_by_Nickname) {
		Posted_by_Nickname = posted_by_Nickname;
	}

	public User getUserOfQuestion() {
		return user;
	}

	public void setUserOfQuestion(User user) {
		this.user = user;
	}

	public double getPopularity() {
		return popularity;
	}

	public void setPopularity(double popularity) {
		this.popularity = popularity;
	}
	
	public void setAnswersArray(ArrayList<Answer> AnswersArray){
		this.AnswersArray = AnswersArray;
	}
	
	public ArrayList<Answer> getAnswersArray(){
		return AnswersArray;
	}
	
	public void setAnswers_count(int answers_count) {
		this.Answers_count = answers_count;
	}
	
	public int getAnswers_count() {
		return Answers_count;
	}

	public int getRate() {
		return Rate;
	}

	public void setRate(int rate) {
		this.Rate = rate;
	}
	
}
