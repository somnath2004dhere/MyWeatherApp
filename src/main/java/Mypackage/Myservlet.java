package Mypackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class Myservlet
 */
@WebServlet("/Myservlet")
public class Myservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Myservlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	
		
		//API setup
		
		String apiKey="b7beb5cb11d2bf828eaa5ff150c0d4fc";
		
		String city=request.getParameter("city");
		
		String apiUrl= "https://api.openweathermap.org/data/2.5/weather?q=" + city +"&appid="+ apiKey;
		
		URL url=new URL(apiUrl);
		
		HttpURLConnection connection =(HttpURLConnection) url.openConnection();
		
		connection.setRequestMethod("GET");
		
		//reading the data from network
		InputStream inputStrim = connection.getInputStream();
		InputStreamReader reader =new InputStreamReader(inputStrim);
		
		//want to store string
		
		StringBuilder responseContent =new StringBuilder();
		
		//Input lene ke liye from the reader ,will create scanner
		Scanner scanner =new Scanner(reader);
		
		while(scanner.hasNext()) {
			responseContent.append(scanner.nextLine());
		}
		scanner.close();
		System.out.println(responseContent);
		
		//TypeCasting =Parsing the data into JSON
		Gson gson=new Gson();
		JsonObject jsonObject = gson.fromJson(responseContent.toString(),JsonObject.class);
		System.out.println(jsonObject);
		
		//Date  & Time
		
		long dateTimestamp =jsonObject.get("dt").getAsLong()* 1000;
		String date = new Date(dateTimestamp).toString();
		
//		Temperature
		double temparatureKelvin=jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
		int temperatureCelsius =(int)(temparatureKelvin-273.15);
		
		//humidity
		int humidity =jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
		
		//Wind Speed
		double windSpeed=jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
		
		//weather Condition
		String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
		
		

		//set the data as request attributes (for sending to jsp page)
		
		request.setAttribute("date", date);
		request.setAttribute("city", city);
		request.setAttribute("temperature", temperatureCelsius);
		request.setAttribute("weatherCondition", weatherCondition);
		request.setAttribute("humidity", humidity);
		request.setAttribute("windSpeed", windSpeed);
		request.setAttribute("weatherData", responseContent.toString());
		
		connection.disconnect();
		//forward the request to the weather.jsp page file
		request.getRequestDispatcher("index.jsp").forward(request, response);
		
		
		
	}

}
