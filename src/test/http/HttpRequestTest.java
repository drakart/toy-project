package test.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kh.toy.common.http.HttpConnector;
import com.kh.toy.common.http.RequestParams;

/**
 * Servlet implementation class HttpRequestTest
 */
@WebServlet("/test/http/*")
public class HttpRequestTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HttpRequestTest() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] uriArr = request.getRequestURI().split("/");
		switch (uriArr[uriArr.length-1]) {
		case "connect":
			testHttpUrlConnection();
			break;
		case "kakao-test":
			testKakao();
			break;
		case "naver-test":
			testNaver();
			break;

		default:
			break;
		}
	}

	private void testNaver() {
		HttpConnector conn = new HttpConnector();
		Gson gson = new Gson();
		
		String url = "https://openapi.naver.com/v1/datalab/search";
		
		Map<String,String>headers = new HashMap<String,String>();
		headers.put("X-Naver-Client-Id","jjPKCM2GfQ5VfUbB8Put");
		headers.put("X-Naver-Client-Secret","sSJGzKtruS");
		headers.put("Content-Type","application/json");
		
		List<String> javaKeywords = new ArrayList<String>();
		javaKeywords.add("스프링");
		javaKeywords.add("스프링부트");
		
		List<String> pythonKeywords = new ArrayList<String>();
		pythonKeywords.add("장고");
		pythonKeywords.add("플러터");
		
		Map<String,Object>javaGroup = new LinkedHashMap<String, Object>();
		javaGroup.put("groupName", "자바");
		javaGroup.put("keywords", javaKeywords);

		Map<String,Object>pythonGroup = new LinkedHashMap<String, Object>();
		pythonGroup.put("groupName", "파이썬");
		pythonGroup.put("keywords", pythonKeywords);
		
		List<Map<String,Object>> keywordGroups = new ArrayList<Map<String,Object>>();
		keywordGroups.add(javaGroup);
		keywordGroups.add(pythonGroup);
		
		String[] ages = {"3","4","5","6","7","8","9","10"};
		
		Map<String,Object> datas = new LinkedHashMap<String, Object>();
		datas.put("startDate", "2016-01-01");
		datas.put("endDate", "2021-01-01");
		datas.put("timeUnit", "month");
		datas.put("keywordGroups", keywordGroups);
		datas.put("ages",ages);
		
		String requestBody = gson.toJson(datas);
		String resposeBody = conn.post(url,headers,requestBody);
		System.out.println(resposeBody);
		
	}

	private void testKakao() {
		
		HttpConnector conn = new HttpConnector();
		//Map<String,String>params = new HashMap<String,String>();
		//params.put("query","자바");
		//params.put("sort","latest");
		RequestParams params = RequestParams.builder()
				.param("query","자바")
				.param("sort","latest").build();
		
		
		String queryString = conn.urlEncodedForm(params);
		System.out.println(queryString);
		
		String url = "https://dapi.kakao.com/v3/search/book?"+queryString;
		Map<String,String>headers = new HashMap<String,String>();
		headers.put("Authorization","KakaoAK 493277f3168f7288fd7d30afe0b82c9a");
		
		
		JsonObject datas = conn.getAsJson(url,headers).getAsJsonObject();
		JsonArray documents = datas.getAsJsonArray("documents");
		//System.out.println(response);
		
		//Gson gson = new Gson();
		//JsonObject datas = gson.fromJson(response,JsonElement.class).getAsJsonObject();
		//JsonArray documents = datas.getAsJsonArray("documents");
		for (JsonElement jsonElement : documents) {
			JsonObject e = jsonElement.getAsJsonObject();
			System.out.println("authors : "+e.getAsJsonArray("authors"));
			System.out.println("title : "+e.getAsJsonPrimitive("title"));
		}
		//System.out.println(datas);
		
		//Map<String,Object> datas = gson.fromJson(response,Map.class);
		//for (String key : datas.keySet()) {
			//System.out.println(key+" : "+datas.get(key));
		//}
		//System.out.println(datas.getClass());
		//Map<String,Object>meta = (Map<String, Object>) datas.get("meta");
		//System.out.println(meta.get("total_count").getClass());
		//authors, title
		/*
		 * List<Map<String,Object>> documents = (List<Map<String, Object>>)
		 * datas.get("documents"); for (Map<String,Object> document : documents) {
		 * System.out.println(document.get("authors"));
		 * System.out.println(document.get("title"));
		 * System.out.println("===============================");
		 * 
		 * }
		 */
	
	}

	private void testHttpUrlConnection() {
		
		try {
			URL url = new URL("http://localhost:9090/mail?mailTemplate=join-auth-mail");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			StringBuffer responseBody = new StringBuffer();
			
			try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))){
				String line = null;
				while((line=br.readLine())!= null) {
					responseBody.append(line);
				}
			}
			System.out.println(responseBody);
		}catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
