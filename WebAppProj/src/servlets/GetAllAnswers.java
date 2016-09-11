package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Question;
import model.User;
import model.Answer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


/**
 * Servlet implementation class GetAllQuestions
 */
@SuppressWarnings("deprecation")
public class GetAllAnswers extends HttpServlet implements SingleThreadModel {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetAllAnswers() {
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
			
			AnswersArray = Answer.getAllAnswers();
			
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
