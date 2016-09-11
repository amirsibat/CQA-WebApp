/* CREATE TABLES STATEMENT, The Strings of each SQL statements in AppConstants.java explains the use of each statement as well */


/* CREATE TABLES STATEMENT */


/*
* CREATE USERS table userID is a PRIMARY increasing key (no duplicates ids)
*/
CREATE TABLE USERS( userID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    		 UserName varchar(255) NOT NULL, Password varchar(255) NOT NULL, Nickname varchar(255) NOT NULL, Description varchar(32672),PhotoURL varchar(255), PRIMARY KEY (userID) )

/*
* CREATE QUESTIONS table questID is a PRIMARY increasing key (no duplicates ids)
*/			 
CREATE TABLE QUESTIONS( questID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
    		 Posted_by_userID INTEGER NOT NULL, Posted_by_Nickname varchar(255) NOT NULL, pubDate varchar(255) NOT NULL,Topic varchar(255) NOT NULL,Content varchar(255) NOT NULL,Rate INTEGER NOT NULL ,Answers_count INTEGER NOT NULL, PRIMARY KEY (questID) )
/*
* CREATE ANSWERS table ansID is a PRIMARY increasing key (no duplicates ids)
*/	
CREATE TABLE ANSWERS( ansID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
    		 Posted_by_userID INTEGER NOT NULL, Posted_by_Nickname varchar(255) NOT NULL, pubDate varchar(255) NOT NULL,Content varchar(255) NOT NULL,Rate INTEGER NOT NULL,Answer_for_questID INTEGER NOT NULL, PRIMARY KEY (ansID) )
 /*
* CREATE LIKEQUEST table ID is a PRIMARY increasing key (no duplicates ids)
*/	  		
			 
CREATE TABLE LIKEQUEST( ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
    		 userID INTEGER NOT NULL,Liked_questID INTEGER NOT NULL, PRIMARY KEY (ID) )
			 
/*
* CREATE DISLIKEQUEST table ID is a PRIMARY increasing key (no duplicates ids)
*/			 
CREATE TABLE DISLIKEQUEST( ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    		userID INTEGER NOT NULL,DisLiked_questID INTEGER NOT NULL, PRIMARY KEY (ID) )

/*
* CREATE LIKEANSWER table ID is a PRIMARY increasing key (no duplicates ids)
*/				
CREATE TABLE LIKEANSWER( ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
    		 userID INTEGER NOT NULL,Liked_answerID INTEGER NOT NULL, PRIMARY KEY (ID) )
/*
* CREATE DISLIKEANSWER table ID is a PRIMARY increasing key (no duplicates ids)
*/	    			
CREATE TABLE DISLIKEANSWER( ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    		 userID INTEGER NOT NULL,DisLiked_answerID INTEGER NOT NULL, PRIMARY KEY (ID) )

			 


			 
/* SELECT, UPDATE, INSERT SQL STATEMENTS USED TO MANIPULATE APACHE DERBY DB */

SELECT * FROM USERS
	
SELECT * FROM USERS WHERE userID=?
	
SELECT * FROM USERS WHERE UserName=? OR Nickname=?
	
SELECT * FROM USERS WHERE Nickname=?
	
SELECT * FROM USERS WHERE UserName=? AND Password=?
	
SELECT * FROM LIKEANSWER
	
SELECT * FROM QUESTIONS INNER JOIN USERS ON QUESTIONS.Posted_by_userID = USERS.userID ORDER BY QUESTIONS.questID DESC	
	
SELECT * FROM QUESTIONS

/*
* Select all answers ordered by ansID descending, INNER JOIN with users to get info of the answer's author from USERS table by id
*/
SELECT * FROM ANSWERS INNER JOIN USERS ON ANSWERS.Posted_by_userID = USERS.userID ORDER BY ANSWERS.ansID DESC

/*
* Select all answers of specific question id ordered by Rate descending, INNER JOIN with users to get info of the answer's author from USERS table by id
*/		
SELECT * FROM ANSWERS INNER JOIN USERS ON ANSWERS.Answer_for_questID =? WHERE ANSWERS.Posted_by_userID = USERS.userID ORDER BY ANSWERS.Rate DESC

/*
* Select a specific answer of a specific question id ordered by Rate descending, INNER JOIN with users to get info of the answer's author from USERS table by id
*/		
SELECT * FROM ANSWERS INNER JOIN USERS ON ANSWERS.Answer_for_questID =? AND ANSWERS.ansID =? WHERE ANSWERS.Posted_by_userID = USERS.userID ORDER BY ANSWERS.ansID DESC
	

/*
* Select all questions ordered by questID descending, INNER JOIN with users to get info of the question's author from USERS table by id
*/
SELECT * FROM QUESTIONS INNER JOIN USERS ON QUESTIONS.Posted_by_userID = USERS.userID ORDER BY QUESTIONS.questID DESC
	
SELECT * FROM QUESTIONS WHERE QUESTIONS.Posted_by_userID =?

/*
* Select all questions ordered by quest descending of specific user, INNER JOIN with users to get info of the question's author from USERS table by id
*/
SELECT * FROM QUESTIONS INNER JOIN USERS ON QUESTIONS.Posted_by_userID = USERS.userID WHERE QUESTIONS.Posted_by_userID=? ORDER BY QUESTIONS.questID DESC

SELECT * FROM ANSWERS WHERE ANSWERS.Posted_by_userID =?
	
SELECT * FROM ANSWERS WHERE ANSWERS.Posted_by_userID =?
	
/*
* Select a specific question , INNER JOIN with users to get info of the question's author from USERS table by id
*/
SELECT * FROM QUESTIONS INNER JOIN USERS ON QUESTIONS.questID =? WHERE QUESTIONS.Posted_by_userID = USERS.userID 
	
SELECT * FROM LIKEANSWER WHERE userID=? AND Liked_answerID=? 
	
SELECT * FROM DISLIKEANSWER WHERE userID=? AND DisLiked_answerID=?

SELECT * FROM LIKEQUEST WHERE userID=? AND Liked_questID=?
	
SELECT * FROM DISLIKEQUEST WHERE userID=? AND DisLiked_questID=?

UPDATE QUESTIONS SET Rate= Rate1  WHERE questID=?  
	
UPDATE QUESTIONS SET Rate= Rate-1  WHERE questID=? 
	
UPDATE QUESTIONS SET Answers_count= Answers_count1 WHERE questID=? 
	
UPDATE ANSWERS SET Rate= Rate1  WHERE ansID=? 
	
UPDATE ANSWERS SET Rate=Rate-1  WHERE ansID=? 

INSERT INTO USERS(Username, Password, Nickname, Description, PhotoURL) VALUES(?,?,?,?,?)
	
INSERT INTO QUESTIONS(Posted_by_userID, Posted_by_Nickname, pubDate, Topic, Content, Rate, Answers_count) VALUES(?,?,?,?,?,?,?)
	
INSERT INTO ANSWERS(Posted_by_userID, Posted_by_Nickname, pubDate, Content, Rate, Answer_for_questID) VALUES(?,?,?,?,?,?)
		
INSERT INTO LIKEQUEST(userID, Liked_questID) VALUES(?,?)
	
INSERT INTO DISLIKEQUEST(userID, DisLiked_questID) VALUES(?,?)
	
INSERT INTO LIKEANSWER(userID, Liked_answerID) VALUES(?,?)
	
INSERT INTO DISLIKEANSWER(userID, DisLiked_answerID) VALUES(?,?)			 
