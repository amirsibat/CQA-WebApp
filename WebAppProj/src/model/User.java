package model;

import java.util.ArrayList;
import java.util.Collections;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import constants.AppConstants; // import AppConstants

import model.Answer;
import model.Question;
import model.Topic;

public class User implements Comparable<User>{
	private String userID;
	private String Username;
	private String Password;
	private String Nickname;
	private String Description;
	private String PhotoURL; // check if null return default error in getPhotoURL function
	private double rate;
	private ArrayList<Topic> expertise;
	
	
	public User(String userID, String Username, String Password, String Nickname, String Description, String PhotoURL)
	{
		this.setUserID(userID);
		this.setUsername(Username);
		this.setPassword(Password);
		this.setNickname(Nickname);
		this.setDescription(Description);
		this.setPhotoURL(PhotoURL);
		
		
	}
	
	public User(String Username, String Password, String Nickname, String Description, String PhotoURL)
	{
		this.setUsername(Username);
		this.setPassword(Password);
		this.setNickname(Nickname);
		this.setDescription(Description);
		this.setPhotoURL(PhotoURL);
		
		
	}
	
	public User(String userID,String Username, String Password, String Nickname, String Description, String PhotoURL,double rate)
	{
		this.setUserID(userID);
		this.setUsername(Username);
		this.setPassword(Password);
		this.setNickname(Nickname);
		this.setDescription(Description);
		this.setPhotoURL(PhotoURL);
		this.setRate(rate);
	}
	
	
	public User(String userID,String Username, String Password, String Nickname, String Description, String PhotoURL,double rate,ArrayList<Topic> expertise)
	{
		this.setUserID(userID);
		this.setUsername(Username);
		this.setPassword(Password);
		this.setNickname(Nickname);
		this.setDescription(Description);
		this.setPhotoURL(PhotoURL);
		this.setRate(rate);
		this.setExp(expertise);
	}
	
	
	public int compareTo (User compareuser) {
        double compareRating=((User)compareuser).getRate();
        /* For Ascending order*/
        double result = compareRating-this.rate; // Descending order
        
        if(result == 0)
        	return 0;
        else if(result > 0)
        	return 1;
        else return -1;
   
    }
	
	
	public User() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return ArrayList of all users in the Database.
	 * @throws SQLException SQL syntax error,NullPointer,...
	 * @throws NamingException if constants name are not valid.
	 **/
	public ArrayList<User> getAllUsers()
	{
		try{
		//obtain DB data source from Tomcat's context
		Context context = new InitialContext();
		
		BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
		Connection conn = ds.getConnection();
		
		ArrayList<User> UsersArrayList = new ArrayList<User>();
		PreparedStatement stmt;
		
		try {
			stmt = conn.prepareStatement(AppConstants.SELECT_ALL_USERS_STMT);
			
			ResultSet rs = stmt.executeQuery();
			//loop over ResultSet and add to ArrayList
			while (rs.next()){
				UsersArrayList.add(new User(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5), rs.getString(6)));
			}
			
			//Close Connections
			rs.close();
			stmt.close();
			conn.close();

			
			return UsersArrayList;
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
	 * @return ArrayList of top 20 rated users in the Database.
	 * @throws SQLException SQL syntax error,NullPointer,...
	 * @throws NamingException if constants name are not valid.
	 **/
	public ArrayList<User> getTopusers()
	{
		try{
		//obtain DB data source from Tomcat's context
		Context context = new InitialContext();
		
		BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
		Connection conn = ds.getConnection();
		
		ArrayList<User> UsersArrayList = new ArrayList<User>();
		PreparedStatement stmt;
		
		try {
			stmt = conn.prepareStatement(AppConstants.SELECT_ALL_USERS_STMT);
			
			double rate = 0,torate=0;
			ArrayList<Topic> exp = new ArrayList<Topic>();
			
			ResultSet rs = stmt.executeQuery();
			
			//loop over ResultSet and add to ArrayList
			while (rs.next()){
				exp = Topic.GetExpertise(Integer.parseInt(rs.getString(1)));
				torate = (0.2 * avg_user_quest(Integer.parseInt(rs.getString(1)))) + (0.8 * avg_user_answers(Integer.parseInt(rs.getString(1))));
				rate = new BigDecimal(torate).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
				UsersArrayList.add(new User(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5), rs.getString(6),rate,exp));
			}
			
			//Close Connections
			rs.close();
			stmt.close();
			conn.close();
			
			
			Collections.sort(UsersArrayList);
			if(UsersArrayList.size() > 20)
				UsersArrayList = new ArrayList<User>(UsersArrayList.subList(0, 20));
							
			
			return UsersArrayList;
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
	 * @param  userID the user id
	 * @return average of all user's rated questions.
	 * @throws SQLException SQL syntax error,NullPointer,etc
	 * @throws NamingException if constants name are not valid.
	 **/
	public double avg_user_quest(int userID){
		
		
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			
			double sum = 0,average=0,questPopularity=0;
			int count = 1; //if no answer divide by 1 still zero - no rate on answers
			
			
			ArrayList<Question> QuestsArrayList = new ArrayList<Question>();
			
			PreparedStatement stmt;
			
			
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_QUESTION_BY_USERID_STMT);
				stmt.setInt(1,userID);
				
				ResultSet rs = stmt.executeQuery();
				
				//loop over ResultSet 
				while (rs.next()){
					
					questPopularity = (0.2 * rs.getInt(7) + 0.8 * (Answer.AverageAnswersRate(Integer.parseInt(rs.getString(1)))));
					QuestsArrayList.add(new Question(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7),rs.getInt(8),questPopularity));

				}
				
				/*
				 * loop over ArrayList & sum all questions rating
				*/
				for (Question m : QuestsArrayList)
				{
					sum = sum + m.getPopularity();
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
	 * @param  userID the user id
	 * @return average of all user's rated answers.
	 * @throws SQLException SQL syntax error,NullPointer,etc
	 * @throws NamingException if constants name are not valid.
	 **/
	public double avg_user_answers(int userID){
		
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			
			double sum = 0,average=0;
			int count = 1; //if no answer divide by 1 still zero - no rate on answers
			
			
			ArrayList<Answer> AnswersArrayList = new ArrayList<Answer>();
			
			PreparedStatement stmt;
			
			
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_ANSWERS_BY_USERID_STMT);
				stmt.setInt(1,userID);
				
				ResultSet rs = stmt.executeQuery();
				
				//loop over ResultSet 
				while (rs.next()){

					AnswersArrayList.add(new Answer(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getInt(6),rs.getInt(7)));

				}
				
				/*
				 * loop over ArrayList & sum all answers rating
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
	 * @param  nickname user nickname
	 * @return a User object with all details about user with specified nickname
	 * @throws SQLException SQL syntax error,NullPointer,...
	 * @throws NamingException if constants name are not valid.
	 **/
	public User getUserByNickname(String nickname)
	{
		
		User tempUser = null;
		
		
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			PreparedStatement stmt;
			
			double torate = 0;
			ArrayList<Topic> exp = new ArrayList<Topic>();
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_USER_BY_nickname_STMT);
				stmt.setString(1,nickname);
				
				ResultSet rs = stmt.executeQuery();
				
				//loop over ResultSet - ResultSet will contain only one result since Nickname is unique in the system
				while (rs.next()){
					
					exp = Topic.GetExpertise(Integer.parseInt(rs.getString(1)));
					torate = (0.2 * avg_user_quest(Integer.parseInt(rs.getString(1)))) + (0.8 * avg_user_answers(Integer.parseInt(rs.getString(1))));
					rate = new BigDecimal(torate).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
					tempUser = new User(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5), rs.getString(6),rate,exp);
				}
				
				//Close Connections
				rs.close();
				stmt.close();
				conn.close();
				
				return tempUser;
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
		return tempUser;
	}
	

	/**
	 * @param userID user Id
	 * @return a User object with all details about user with specified userID
	 * @throws SQLException SQL syntax error,NullPointer,...
	 * @throws NamingException if constants name are not valid.
	 **/
	public User getUserByID(int userID)
	{
		
		User tempUser = null;
		
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			PreparedStatement stmt;
			
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_USER_BY_userID_STMT);
				stmt.setInt(1,userID);
				
				ResultSet rs = stmt.executeQuery();
				//loop over ResultSet
				while (rs.next()){
					
					tempUser = new User(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5), rs.getString(6));
				}
				
				//Close Connections
				rs.close();
				stmt.close();
				conn.close();
				
				return tempUser;
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
		return tempUser;
	}



	/**
	 * @param Username user name
	 * @param password Password
	 * @param Nickname nickname
	 * @param Description Description
	 * @param PhotoURL photo url
	 * @return True if user's registration done successfully.
	 * @throws SQLException SQL syntax error,NullPointer,...
	 * @throws NamingException if constants name are not valid.
	 **/
	public boolean RegisterUser(String Username, String password, String Nickname, String Description, String PhotoURL)
	{
		boolean added=false;
		
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			PreparedStatement stmt,pstmt;
			
			try {
				
				pstmt = conn.prepareStatement(AppConstants.SELECT_USER_BY_USERNAME_STMT);
				pstmt.setString(1,Username);
				pstmt.setString(2,Nickname);
				
				
				ResultSet rs = pstmt.executeQuery();
				
				// loop over ResultSet 
				if(!rs.next()){
					
					stmt = conn.prepareStatement(AppConstants.INSERT_USER_TO_USER_STMT);
					stmt.setString(1,Username);
					stmt.setString(2, password);
					stmt.setString(3, Nickname);
					stmt.setString(4, Description);
					stmt.setString(5, PhotoURL);
					
					
					//execute query commit changes and close insert statement
					stmt.executeUpdate();
					conn.commit();
					stmt.close();
					
					added = true;
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
		
		return added;
	}

	
	/**
	 * @param Username user name
	 * @param password password
	 * @return User user if exist, null otherwise
	 * @throws SQLException SQL syntax error,NullPointer,...
	 * @throws NamingException if constants name are not valid.
	 **/
	public User isUserExist(String Username, String password)
	{
		User tempUser=null;
		
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			PreparedStatement pstmt;
			
			try {
				
				pstmt = conn.prepareStatement(AppConstants.SELECT_USER_BY_USERNAME_AND_PASS_STMT);
				pstmt.setString(1,Username);
				pstmt.setString(2,password);
				
				
				ResultSet rs = pstmt.executeQuery();
				
				if(rs.next()){
					tempUser = new User(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5), rs.getString(6));
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
		
		return tempUser;

	}
	
	
	
	/* Getters & Setters */
	
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	

	public String getUsername() {
		return Username;
	}
	

	public void setUsername(String username) {
		this.Username = username;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		this.Password = password;
	}

	public String getNickname() {
		return Nickname;
	}

	public void setNickname(String nickname) {
		this.Nickname = nickname;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		this.Description = description;
	}

	public String getPhotoURL() {
		return PhotoURL;
	}

	public void setPhotoURL(String photoURL) {
		this.PhotoURL = photoURL;
	}

	public void setRate(double Rate){
		this.rate = Rate;
	}
		
	public double getRate(){
		return rate;
	}
	
	public void setExp(ArrayList<Topic> exp){
		this.expertise = exp;
	}

}
