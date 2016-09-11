package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import model.Answer;

/**
 * Servlet implementation class GetAnswersLikes
 */
public class GetAnswersLikes extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetAnswersLikes() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	try{
		ArrayList<Answer> AnswersArray = new ArrayList<Answer>();
		
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		AnswersArray = Answer.getLikes();
		
		Gson gson = new Gson();
		
		//convert from answers ArrayList to json
    	String userJsonResult = gson.toJson(AnswersArray, new TypeToken<ArrayList<Answer>>() {}.getType());
    	out.println(userJsonResult);
    	out.close();
    	
	}catch (IOException e) {  
        e.printStackTrace();  
	}
}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
