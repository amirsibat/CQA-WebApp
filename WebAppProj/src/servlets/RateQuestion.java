package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Question;
import model.User;

/**
 * Servlet implementation class Rate Question
 */

@SuppressWarnings("deprecation")
public class RateQuestion extends HttpServlet implements SingleThreadModel {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RateQuestion() {
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
			
			
			Question quest = new Question();
			
			
			String  srate = uri.substring(uri.indexOf("id") -2 , uri.indexOf("id")-1);  //Like = 1 , Dislike = 0
			int rate = Integer.parseInt(srate);
			response.setContentType("application/json; charset=UTF-8");
			PrintWriter out = response.getWriter();
			
			
			if (uri.indexOf("id") != -1 && user !=null)
			{
			String id = uri.substring(uri.indexOf("id") + 3 , uri.indexOf("id") + 6); //Posted_By_userID
			String qid = uri.substring(uri.indexOf("quest") + 6); //questID
			
			
			if(user.getUserID().compareTo(id) == 0){
				out.println("{ \"result\": \"fail\",\"message\":\"You can't Rate your own question \"}"); // can't rate yourself
				out.close();
			}
			else{
				if(rate == 1)
				{
					if(quest.LikeQuestion(Integer.parseInt(user.getUserID()),Integer.parseInt(qid)))  
						{
							out.println("{ \"result\": \"success\",\"message\":\"Question Rated Successfully \"}");	
								out.close();
						}
					else
						{
						out.println("{ \"result\": \"fail\",\"message\":\"You  already rated this question \"}"); // return message as jason
						out.close();	
						}
				}
				else{
					
					if(quest.DisLikeQuestion(Integer.parseInt(user.getUserID()),Integer.parseInt(qid)))
						{
						out.println("{ \"result\": \"success\"}");	
						out.close();
						}
					else
						{
						out.println("{ \"result\": \"fail\",\"message\":\"You  already rated this question \"}"); // return message as jason
						out.close();	
						}
					
				}
				
			}
			}
			else
			{
				out.println("{ \"result\": \"fail\"}"); // will reach the else if id doesn't exist in URI
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
