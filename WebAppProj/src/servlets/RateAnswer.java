package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import model.User;
import model.Answer;

/**
 * Servlet implementation class Rate Answer
 */

@SuppressWarnings("deprecation")
public class RateAnswer extends HttpServlet implements SingleThreadModel {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RateAnswer() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		try{
			String uri = request.getRequestURI();
			HttpSession session = request.getSession(true);
			
			//get session variable
			User user = (User) session.getAttribute("user");
			
			Answer answer = new Answer();
			
			
			
			String  srate = uri.substring(uri.indexOf("id") -2 , uri.indexOf("id")-1);  //Like = 1 , Dislike = 0
			int rate = Integer.parseInt(srate);
			response.setContentType("application/json; charset=UTF-8");
			PrintWriter out = response.getWriter();
			
			
			if (uri.indexOf("id") != -1 && user !=null)
			{
			String id = uri.substring(uri.indexOf("id") + 3 , uri.indexOf("id") + 6); //Posted_By_userID
			String ansid = uri.substring(uri.indexOf("answer") + 7); //ansID
			
			
			if(user.getUserID().compareTo(id) == 0){
				out.println("{ \"result\": \"fail\",\"message\":\"You can't Rate your own Answer \"}"); // can't rate yourself
				out.close();
			}
			else{
				if(rate == 1)
				{
					if(answer.LikeAnswer(Integer.parseInt(user.getUserID()),Integer.parseInt(ansid)))  
						{
							out.println("{ \"result\": \"success\",\"message\":\"Answer Rated Successfully \"}");	
								out.close();
						}
					else
						{
						out.println("{ \"result\": \"fail\",\"message\":\"You  already rated this Answer \"}"); // return message as jason
						out.close();	
						}
				}
				else{
					
					if(answer.DisLikeAnswer(Integer.parseInt(user.getUserID()),Integer.parseInt(ansid)))
						{
						out.println("{ \"result\": \"success\",\"message\":\"Answer Rated Successfully \"}");	
						out.close();
						}
					else
						{
						out.println("{ \"result\": \"fail\",\"message\":\"You  already rated this Answer \"}"); // return message as jason
						out.close();	
						}
					
				}
				
			}
			}
			else
			{
				out.println("{ \"result\": \"fail\",\"message\":\"ERROR EXCEPTION - NO USER ID \"}"); // will reach the else if id doesn't exist in URI
				out.close();	
			}
	}catch (IOException e) {  
        e.printStackTrace();  
    }
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
