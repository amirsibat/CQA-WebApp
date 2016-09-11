package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Topic;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class GetMostPopularTopics
 */
public class GetMostPopularTopics extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetMostPopularTopics() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<Topic> TopicsArray = new ArrayList<Topic>();
		
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		TopicsArray = Topic.GetMostPopularTopics();
		
		Gson gson = new Gson();
		
    	//convert from topics ArrayList to json
    	String TopicsJsonResult = gson.toJson(TopicsArray, new TypeToken<ArrayList<Topic>>() {}.getType());
    	out.println(TopicsJsonResult);
    	out.close();
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
