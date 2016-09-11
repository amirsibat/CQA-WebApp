package constants;

public interface AppConstants {

	//derby constants
	public final String DB_NAME = "CqaDB";
	public final String DB_DATASOURCE = "java:comp/env/jdbc/CqaDatasource";
	public final String PROTOCOL = "jdbc:derby:"; 
	
	
	//SQL statements
	public final String SELECT_ALL_USERS_STMT = "SELECT * FROM USERS";
	
	public final String SELECT_USER_BY_userID_STMT = "SELECT * FROM USERS "
			+ "WHERE userID=?";
	
	public final String SELECT_USER_BY_USERNAME_STMT = "SELECT * FROM USERS "
			+ "WHERE UserName=? OR Nickname=?";
	
	public final String SELECT_USER_BY_nickname_STMT = "SELECT * FROM USERS "
			+ "WHERE Nickname=?";	
	
	public final String SELECT_USER_BY_USERNAME_AND_PASS_STMT = "SELECT * FROM USERS "
			+ "WHERE UserName=? AND Password=?";
	
	public final String SELECT_ALL_LIKEANSWERS_STMT = "SELECT * FROM LIKEANSWER";
	
	public final String SELECT_ALL_QUESTIONS_STMT = "SELECT * FROM QUESTIONS INNER JOIN USERS ON "
			+ "QUESTIONS.Posted_by_userID = USERS.userID ORDER BY QUESTIONS.questID DESC";	
	
	public final String SELECT_ALL_QUESTIONS_STMT_NO_USERS = "SELECT * FROM QUESTIONS";
	
	public final String SELECT_ALL_ANSWERS_STMT = "SELECT * FROM ANSWERS INNER JOIN USERS ON "
		+ " ANSWERS.Posted_by_userID = USERS.userID ORDER BY ANSWERS.ansID DESC";
	
	public final String SELECT_ANSWERS_OF_QUEST_STMT = "SELECT * FROM ANSWERS INNER JOIN USERS ON"
			+ " ANSWERS.Answer_for_questID =? WHERE ANSWERS.Posted_by_userID = USERS.userID ORDER BY ANSWERS.Rate DESC";
	
	public final String SELECT_ANSWER_BY_ID_OF_QUEST_STMT = "SELECT * FROM ANSWERS INNER JOIN USERS ON"
			+ " ANSWERS.Answer_for_questID =? AND ANSWERS.ansID =? WHERE ANSWERS.Posted_by_userID = USERS.userID ORDER BY ANSWERS.ansID DESC";
	
	public final String SELECT_USER_AND_QUESTION_FROM_LIKEQUEST_STMT = "SELECT * FROM LIKEQUEST "
			+ "WHERE userID=? AND Liked_questID=?";
	
	public final String SELECT_USER_AND_QUESTION_FROM_DISLIKEQUEST_STMT = "SELECT * FROM DISLIKEQUEST "
			+ "WHERE userID=? AND DisLiked_questID=?";
	
	public final String SELECT_ALL_NEWQUESTIONS_STMT = "SELECT * FROM QUESTIONS INNER JOIN USERS ON"
			+ " QUESTIONS.Posted_by_userID = USERS.userID ORDER BY QUESTIONS.questID DESC";
	
	public final String SELECT_QUESTION_BY_USERID_STMT = "SELECT * FROM QUESTIONS"
			+ " WHERE QUESTIONS.Posted_by_userID =?";

	public final String SELECT_ALL_QUESTIONS_BY_USERID_STMT = "SELECT * FROM QUESTIONS INNER JOIN USERS ON "
			+ "QUESTIONS.Posted_by_userID = USERS.userID WHERE QUESTIONS.Posted_by_userID=? ORDER BY QUESTIONS.questID DESC";
	
	public final String SELECT_ALL_ANSWERS_BY_USERID_STMT = "SELECT * FROM ANSWERS"
			+ " WHERE ANSWERS.Posted_by_userID =?";
	
	public final String SELECT_ANSWERS_BY_USERID_STMT = "SELECT * FROM ANSWERS"
			+ " WHERE ANSWERS.Posted_by_userID =?";
	
	public final String SELECT_QUESTION_BY_QUESTID_STMT = "SELECT * FROM QUESTIONS INNER JOIN USERS ON"
			+ " QUESTIONS.questID =? WHERE QUESTIONS.Posted_by_userID = USERS.userID ";
	
	public final String SELECT_USER_AND_ANSWER_FROM_LIKEANSWER_STMT = "SELECT * FROM LIKEANSWER "
			+ "WHERE userID=? AND Liked_answerID=?"; 
	
	public final String SELECT_USER_AND_ANSWER_FROM_DISLIKEANSWER_STMT = "SELECT * FROM DISLIKEANSWER "
			+ "WHERE userID=? AND DisLiked_answerID=?";
	
	
	
	
	public final String UPDATE_QUESTION_RATE_LIKE_STMT = "UPDATE QUESTIONS SET Rate= Rate+1  WHERE questID=?  ";
	
	public final String UPDATE_QUESTION_RATE_DISLIKE_STMT = "UPDATE QUESTIONS SET Rate= Rate-1  WHERE questID=? ";
	
	public final String UPDATE_QUESTION_ANSWER_COUNT_STMT = "UPDATE QUESTIONS SET Answers_count= Answers_count+1 WHERE questID=? ";
	
	public final String UPDATE_ANSWER_RATE_LIKE_STMT = "UPDATE ANSWERS SET Rate= Rate+1  WHERE ansID=? ";
	
	public final String UPDATE_ANSWER_RATE_DISLIKE_STMT = "UPDATE ANSWERS SET Rate=Rate-1  WHERE ansID=? ";
	
	
	
	
	public final String INSERT_USER_TO_USER_STMT = "INSERT INTO USERS(Username, Password, Nickname, Description, PhotoURL)"
			+ "VALUES(?,?,?,?,?)";
	
	public final String INSERT_ANS_TO_ANSWERS_STMT = "INSERT INTO ANSWERS(Posted_by_userID, Posted_by_Nickname, pubDate, Content, Rate, Answer_for_questID)"
			+ "VALUES(?,?,?,?,?,?)";
	
	public final String INSERT_QUEST_TO_QUESTIONS_STMT = "INSERT INTO QUESTIONS(Posted_by_userID, Posted_by_Nickname, pubDate, Topic, Content, Rate, Answers_count)"
			+ "VALUES(?,?,?,?,?,?,?)";
	
	public final String INSERT_USER_TO_LIKEQUEST_STMT = "INSERT INTO LIKEQUEST(userID, Liked_questID)"
			+ "VALUES(?,?)";
	
	public final String INSERT_USER_TO_DISLIKEQUEST_STMT = "INSERT INTO DISLIKEQUEST(userID, DisLiked_questID)"
			+ "VALUES(?,?)";
	
	public final String INSERT_USER_TO_LIKEANSWER_STMT = "INSERT INTO LIKEANSWER(userID, Liked_answerID)"
			+ "VALUES(?,?)";
	
	public final String INSERT_USER_TO_DISLIKEANSWER_STMT = "INSERT INTO DISLIKEANSWER(userID, DisLiked_answerID)"
			+ "VALUES(?,?)";
}
