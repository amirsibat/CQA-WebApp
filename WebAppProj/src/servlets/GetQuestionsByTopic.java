package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.net.*;

import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Question;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class GetQuestionsByTopic
 */
public class GetQuestionsByTopic extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetQuestionsByTopic() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
try{
			
			String uri = request.getRequestURI();
			
			
			ArrayList<Question> QuestsArray = new ArrayList<Question>();
			
			response.setContentType("application/json; charset=UTF-8");
			PrintWriter out = response.getWriter();
			
			// if topic exists in url
			if (uri.indexOf("topic") != -1)
			{
				String topic = uri.substring(uri.indexOf("topic") + 6);
				String decoded = URLDecoder.decode(topic, "UTF-8");
				
					if(decoded != ""){
						QuestsArray = Question.GetQuestionsByTopic(decoded); // get all questions with this topic
					}
			}
			
			Gson gson = new Gson();
			
	    	//convert from question ArrayList to json
	    	String questsJsonResult = gson.toJson(QuestsArray, new TypeToken<ArrayList<Question>>() {}.getType());
	    	out.println(questsJsonResult);
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
