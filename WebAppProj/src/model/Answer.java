package model;

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

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import constants.AppConstants;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import model.TimeHelper;
import model.User;
import model.Question;

public class Answer {
	private String ansID;
	private String Posted_by_userID;
	private String Posted_by_Nickname;
	private String pubDate;
	private String Content;
	private int Rate;
	private int Answer_for_questID;
	
	private Question quest;
	private User user;
	
	
	
	public Answer(String ansID,String Posted_by_userID, String Posted_by_Nickname, String pubDate, String Content, int Rate)
	{
		this.setAnstID(ansID);
		this.setPosted_by_userID(Posted_by_userID);
		this.setPosted_by_Nickname(Posted_by_Nickname);
		this.setDate(pubDate);
		this.setContent(Content);
		this.setRate(Rate);
	}
	
	public Answer(String ansID,String Posted_by_userID, String Posted_by_Nickname, String pubDate, String Content, User user)
	{
		this.setAnstID(ansID);
		this.setPosted_by_userID(Posted_by_userID);
		this.setPosted_by_Nickname(Posted_by_Nickname);
		this.setDate(pubDate);
		this.setContent(Content);
		this.setUserOfAnswer(new User(user.getUsername(), user.getPassword(), user.getNickname(), user.getDescription(), user.getPhotoURL()));
	}
	
	public Answer(String ansID,String Posted_by_userID, String Posted_by_Nickname, String pubDate, String Content, int Rate, int Answer_for_questID, User user)
	{
		this.setAnstID(ansID);
		this.setPosted_by_userID(Posted_by_userID);
		this.setPosted_by_Nickname(Posted_by_Nickname);
		this.setDate(pubDate);
		this.setContent(Content);
		this.setRate(Rate);
		this.SetAnswer_for_questID(Answer_for_questID);
		this.setUserOfAnswer(new User(user.getUsername(), user.getPassword(), user.getNickname(), user.getDescription(), user.getPhotoURL()));
		
	}
	
	public Answer(String ansID,String Posted_by_userID, String Posted_by_Nickname, String pubDate, String Content, int Rate, int Answer_for_questID)
	{
		this.setAnstID(ansID);
		this.setPosted_by_userID(Posted_by_userID);
		this.setPosted_by_Nickname(Posted_by_Nickname);
		this.setDate(pubDate);
		this.setContent(Content);
		this.setRate(Rate);
		this.SetAnswer_for_questID(Answer_for_questID);		
	}
	
	
	public Answer(String ansID,String Posted_by_userID, String Posted_by_Nickname, String pubDate, String Content, int Rate, int Answer_for_questID,Question quest, User user)
	{
		this.setAnstID(ansID);
		this.setPosted_by_userID(Posted_by_userID);
		this.setPosted_by_Nickname(Posted_by_Nickname);
		this.setDate(pubDate);
		this.setContent(Content);
		this.setRate(Rate);
		this.SetAnswer_for_questID(Answer_for_questID);
		this.setQuestOfAnswer(new Question(quest.getQuestID(), quest.getPosted_by_userID(),quest.getPosted_by_Nickname(),quest.getDate(),quest.getTopic(),quest.getContent(),quest.getRate(),quest.getAnswers_count()));
		this.setUserOfAnswer(new User(user.getUsername(), user.getPassword(), user.getNickname(), user.getDescription(), user.getPhotoURL()));
		
	}
	
		
	
	public static Comparator<Answer> COMPARE_BY_ANSW_ID = new Comparator<Answer>() {
        public int compare(Answer one, Answer other) {
        	int result = Integer.parseInt(other.ansID) - Integer.parseInt(one.ansID);
            return result;
        }
    };
	
	   
    
    public Answer() {
		// TODO Auto-generated constructor stub
	}

	
    /**
	 * @param Posted_by_userID the user id
	 * @param  Nickname user nickname
	 * @param  Content answer content
	 * @param  Rate answer rating
	 * @param  Answer_for_questID id of question
	 * @return True if the answer was posted successfully.
	 * @throws SQLException SQL syntax error,NullPointer,etc
	 * @throws NamingException if constants name are not valid.
	 **/
	static public boolean PostNewAns(int Posted_by_userID, String Nickname , String Content, int Rate, int Answer_for_questID)
	{
		boolean added=false,updated=false;
		

		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			PreparedStatement stmt,fstmt;
			
			try {
					
					stmt = conn.prepareStatement(AppConstants.INSERT_ANS_TO_ANSWERS_STMT);
					
					Date dNow = new Date();
				    SimpleDateFormat ft = new SimpleDateFormat ("dd/MM/yyyy HH:mm");
					
				    stmt.setInt(1, Posted_by_userID);
				    stmt.setString(2, Nickname);
					stmt.setString(3, ft.format(dNow)); // format date to String
					stmt.setString(4, Content);
					stmt.setInt(5, Rate); //should be 0
					stmt.setInt(6,Answer_for_questID); //questID
					
					
					fstmt = conn.prepareStatement(AppConstants.UPDATE_QUESTION_ANSWER_COUNT_STMT);
					fstmt.setInt(1,Answer_for_questID);
					
					
					//execute query commit changes and close insert statement
					fstmt.executeUpdate();
					stmt.executeUpdate();
					conn.commit();
					stmt.close();
					
					added = true;
					updated = true;
				
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
		
		return (added && updated);
	}
	
	
	     /**
		 * @param questID question id
		 * @return Average of all question's answers rating
		 * @throws SQLException SQL syntax error,NullPointer,etc
		 * @throws NamingException if constants name are not valid.
		 **/
	static public double AverageAnswersRate(int questID){
		
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			
			int sum = 0;
			int count = 1; //if no answer divide by 1 still zero - no rate on answers
			double average=0;
			
			ArrayList<Answer> AnswersArrayList = new ArrayList<Answer>();
			
			PreparedStatement stmt;
			
			
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_ANSWERS_OF_QUEST_STMT);
				stmt.setInt(1,questID);
				
				ResultSet rs = stmt.executeQuery();
				
				//loop over ResultSet 
				while (rs.next()){
					
					User user= new User(rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12), rs.getString(13));
					AnswersArrayList.add(new Answer(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getInt(6),rs.getInt(7),user));
					
				}
				
				/*
				 * loop over ArrayList & calculate the sum of all 
				 * answers rating 
				*/
				for (Answer m : AnswersArrayList)
				{
					sum = sum + m.getRate();
					count = count + 1;
					
				}
				
				//Close Connections
				rs.close();
				stmt.close();
				conn.close();
				
				average = sum /count ;
				
				
				return average;
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
			
			return 0; // if Debugger reached here means Database is still empty.
			
			
	}
	
	
	/**
	 * @param userID the user id
	 * @param  ansID answer id
	 * @return True if the answer was rated successfully.
	 * @throws SQLException SQL syntax error,NullPointer,etc
	 * @throws NamingException if constants name are not valid.
	 **/
	public boolean LikeAnswer(int userID, int ansID)
	{
		boolean rated=false;
		
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			PreparedStatement stmt,pstmt,fstmt;
			
			try {
				
					pstmt = conn.prepareStatement(AppConstants.SELECT_USER_AND_ANSWER_FROM_LIKEANSWER_STMT);
					pstmt.setInt(1,userID);
					pstmt.setInt(2,ansID);
				
					
					ResultSet rs = pstmt.executeQuery();
					
					// userID didnt like ansID already then perform insert query
					if(rs.next() == false){
						
						try{
						stmt = conn.prepareStatement(AppConstants.INSERT_USER_TO_LIKEANSWER_STMT);
						stmt.setInt(1,userID);
						stmt.setInt(2,ansID);
						
						fstmt = conn.prepareStatement(AppConstants.UPDATE_ANSWER_RATE_LIKE_STMT);
						fstmt.setInt(1,ansID);
						
						//execute query commit changes and close insert statement
						
					    fstmt.executeUpdate();
						stmt.executeUpdate();
						conn.commit();
					
						
						fstmt.close();
						stmt.close();
						
						
						rated = true;
						
						}catch(SQLException e)
						{
							System.err.println(e.getMessage()); // print error to error stream
						}
						
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
	 * @param userID the user id
	 * @param  ansID answer id
	 * @return True if the answer was rated successfully.
	 * @throws SQLException SQL syntax error,NullPointer,etc
	 * @throws NamingException if constants name are not valid.
	 **/
	public boolean DisLikeAnswer(int userID,int ansID)
	{
		boolean rated=false;
		
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			PreparedStatement stmt,pstmt,fstmt;
			
			try {
				
					pstmt = conn.prepareStatement(AppConstants.SELECT_USER_AND_ANSWER_FROM_DISLIKEANSWER_STMT);
					pstmt.setInt(1,userID);
					pstmt.setInt(2, ansID);
					
					ResultSet rs = pstmt.executeQuery();
					
					// userID didn't dislike anstID already then perform insert query
					if(rs.next() == false){
					
						stmt = conn.prepareStatement(AppConstants.INSERT_USER_TO_DISLIKEANSWER_STMT);
						stmt.setInt(1,userID);
						stmt.setInt(2, ansID);
						
						fstmt = conn.prepareStatement(AppConstants.UPDATE_ANSWER_RATE_DISLIKE_STMT);
						fstmt.setInt(1,ansID);
						
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
	 * @return ArrayList of all the likes in DB
	 * @throws SQLException SQL syntax error,NullPointer,etc
	 * @throws NamingException if constants name are not valid.
	 **/
	static public ArrayList<Answer> getLikes(){
		
		try{
			
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			ArrayList<Answer> AnswersArrayList = new ArrayList<Answer>();
			PreparedStatement stmt;
			
			try {
				
				stmt = conn.prepareStatement(AppConstants.SELECT_ALL_LIKEANSWERS_STMT);
				

				ResultSet rs = stmt.executeQuery();
				//loop over ResultSet and add to ArrayList
				while (rs.next()){
					
					
					System.out.print("LikeID");
					System.out.println(rs.getInt(1));
					System.out.print("UserID");
					System.out.println(rs.getInt(2));
					System.out.print("AnswerID");
					System.out.println(rs.getInt(3));
					System.out.println("******************************************");
					
				}
				
				//Close Connections
				rs.close();
				stmt.close();
				conn.close();
				return AnswersArrayList;
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
	 * @param questID the question id
	 * @return ArrayList of all the answers of questionID.
	 * @throws SQLException SQL syntax error,NullPointer,etc
	 * @throws ParseException if Parse was failed
	 * @throws NamingException if constants name are not valid.
	 **/
	static public ArrayList<Answer> getQuestAnswers(int questID){
	
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			ArrayList<Answer> AnswersArrayList = new ArrayList<Answer>();
			PreparedStatement stmt;
			
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_ANSWERS_OF_QUEST_STMT);
				stmt.setInt(1,questID);
				
				ResultSet rs = stmt.executeQuery();
				//loop over ResultSet 
				while (rs.next()){
				
					User user= new User(rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12), rs.getString(13));
					AnswersArrayList.add(new Answer(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getInt(6),rs.getInt(7),user));
					
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
				for (Answer m : AnswersArrayList)
				{
					try{
					DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
					Date postDate = formatter.parse(m.getDate());
					diffInMillis = dNow.getTime() - postDate.getTime(); // calculate difference in MilliSeconds relative to 1970..
					m.setDate( TimeHelper.millisToLongDHMS(diffInMillis,m.getDate()) ); // Call Time Helper and set the new date in the array
					}catch(ParseException ex)
					{
				System.err.println(ex.getMessage()); // print error to error stream
					}
			}
				
				
				return AnswersArrayList;
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
	 * @param  questID the question id
	 * @return ArrayList of answers By Id of questionID.
	 * @throws SQLException SQL syntax error,NullPointer,etc
	 * @throws ParseException if Parse was failed
	 * @throws NamingException if constants name are not valid.
	 **/
	static public ArrayList<Answer> getQuestAnswerByID(int questID,int ansID){
		
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			ArrayList<Answer> AnswersArrayList = new ArrayList<Answer>();
			PreparedStatement stmt;
			
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_ANSWER_BY_ID_OF_QUEST_STMT);
				stmt.setInt(1,questID);
				stmt.setInt(2,ansID);
				
				ResultSet rs = stmt.executeQuery();
				//loop over ResultSet 
				while (rs.next()){
				
					User user= new User(rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12), rs.getString(13));
					AnswersArrayList.add(new Answer(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getInt(6),rs.getInt(7),user));
					
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
				for (Answer m : AnswersArrayList)
				{
					try{
					DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
					Date postDate = formatter.parse(m.getDate());
					diffInMillis = dNow.getTime() - postDate.getTime(); // calculate difference in MilliSeconds relative to 1970..
					m.setDate( TimeHelper.millisToLongDHMS(diffInMillis,m.getDate()) ); // Call Time Helper and set the new date in the array
					}catch(ParseException ex)
					{
				System.err.println(ex.getMessage()); // print error to error stream
					}
			}
				
				
				return AnswersArrayList;
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
	 * @return ArrayList of all the answers in DB.
	 * @throws SQLException SQL syntax error,NullPointer,etc
	 * @throws ParseException if Parse was failed
	 * @throws NamingException if constants name are not valid.
	 **/
	static public ArrayList<Answer> getAllAnswers()
	{
		try{
			
		//obtain DB data source from Tomcat's context
		Context context = new InitialContext();
		
		BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
		Connection conn = ds.getConnection();
		
		ArrayList<Answer> AnswersArrayList = new ArrayList<Answer>();
		PreparedStatement stmt;
		
		try {
			
			stmt = conn.prepareStatement(AppConstants.SELECT_ALL_ANSWERS_STMT);

			ResultSet rs = stmt.executeQuery();
			
			//loop over ResultSet and add to ArrayList
			while (rs.next()){
				
				User user= new User(rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12), rs.getString(13));
				AnswersArrayList.add(new Answer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),rs.getInt(6),rs.getInt(7),user));
				
			}
			
			//Close Connections
			rs.close();
			stmt.close();
			conn.close();
			return AnswersArrayList;
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
	
	public void setAnstID(String ansID) {
		this.ansID = ansID;
	}

	public String getansID() {
		return ansID;
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

	public User getUserOfAnswer() {
		return user;
	}

	public void setUserOfAnswer(User user) {
		this.user = user;
	}

	public void SetAnswer_for_questID(int questID){
		this.Answer_for_questID = questID;
	}
	
	public void setQuestOfAnswer(Question quest){
		this.quest = quest;
	}
	
	
	public int getRate() {
		return Rate;
	}

	public void setRate(int rate) {
		Rate = rate;
	}	
	
}
