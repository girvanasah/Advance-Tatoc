//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.remote.http.HttpClient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class Advance
{

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 * @throws InterruptedException 
	 */
	public static  String output; 
	public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException, IOException
	{
 		 WebDriver driver = new FirefoxDriver();
		 driver.get("http://10.0.1.86/");
		 driver.findElement(By.linkText("/tatoc")).click();
		 driver.findElement(By.xpath("html/body/div[1]/div[2]/a[2]")).click();

		// problem1
		 WebElement searchBtn = driver.findElement(By.cssSelector(".menutitle"));
		 Actions action = new Actions(driver);
		 action.moveToElement(searchBtn).perform();
		 driver.findElement(By.xpath("html/body/div/div[2]/div[2]/span[5]")).click();

		// Actions actions = new Actions(driver);
		// WebElement mainMenu =
		// driver.findElement(By.cssSelector(".menutitle"));
		// actions.moveToElement(mainMenu).perform();
		// WebElement subMenu = driver.findElement(By.linkText("Go Next"));
		// actions.moveToElement(subMenu);
		// actions.click().build().perform();

		// problem2
		 
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://10.0.1.86/tatoc", "tatocuser","tatoc01");
			Statement stmt = con.createStatement();		
			String symname = driver.findElement(By.id("symboldisplay")).getText();
			ResultSet rs2=stmt.executeQuery("select name, passkey from credentials c, identity i where i.symbol = '" + symname +"' and i.id = c.id");
			while(rs2.next())
			{
				String u = rs2.getString("Name");
				String p = rs2.getString("Passkey");
				driver.findElement(By.id("name")).sendKeys(u);
				driver.findElement(By.id("passkey")).sendKeys(p);
				driver.findElement(By.id("submit")).click();
				break;
			}
		 
			
		//problem3
			
			JavascriptExecutor js = (JavascriptExecutor)driver;
			
			//waiting for video to load
			Thread.sleep(1000);
			
			//playing the video
			js.executeScript("document.getElementsByClassName('video')[0].getElementsByTagName('object')[0].playMovie();");
			/*
			 * checking the play time of video and then waiting for that time before clicking on proceed 
			double i = (double) js.executeScript("return document.getElementsByClassName('video')[0].getElementsByTagName('object')[0].getTotalTime();");
			int j = (int) i;
			System.out.println(+i);
			System.out.println(+j);
			j=j*1000+1000;
			//Thread.sleep(j);
			driver.findElement(By.linkText("Proceed")).click();
			*/
			
			System.out.println("Started");
			//checking the status of video and clicking on proceed when it is played
			while(true)
			{
				String state = (String)js.executeScript("return document.getElementsByClassName('video')[0].getElementsByTagName('object')[0].getState();");
				System.out.println(state);
				
				if(state.equals("paused")) 
				{
					driver.findElement(By.linkText("Proceed")).click();
					break;
				}
			}
			
		
		//problem4
			
			//extracting the session id
			String id = driver.findElement(By.xpath(".//*[@id='session_id']")).getText();
			System.out.println(id);
			String[] p= id.split(": ");
			System.out.println(p[1]);
			// sending the GET request and extracting the token
			driver.get("http://10.0.1.86/tatoc/advanced/rest/service/token/"+p[1]);
			String token = driver.findElement(By.cssSelector("html>body>pre")).getText();
			String t = token.substring(10,42);
			System.out.println(t);
			
		/*	
		 * sending GET request using httpClient 
		 try 
			{
				
				// create HTTP Client
				HttpClient HttpClient = HttpClientBuilder.create().build();
	 
				// Create new getRequest with below mentioned URL
				HttpGet getRequest = new HttpGet("http://10.0.1.86/tatoc/advanced/rest/service/token/"+p[1]);
	 
				// Add additional header to getRequest which accepts application/xml data
				getRequest.addHeader("accept", "application/xml");
	 
				// Execute your request and catch response
				HttpResponse response = HttpClient.execute(getRequest);
	 
				// Check for HTTP response code: 200 = success
				if (response.getStatusLine().getStatusCode() != 200) 
				{
					throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
				}
	 
				// Get-Capture Complete application/xml body response
				BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
				
				//String t;
				//System.out.println(br);
				//System.out.println(br.readLine());
				// Simply iterate through XML response and show on console.
				while ((output = br.readLine()) != null)
				{   // t = new String(output);
					System.out.println(output);
					break;
				}
				//String t=new String(output);
				System.out.println(output);
			} catch(Exception e){} 			*/
	////////////////////////////////////////////////////////////////////////////////////////
			
			// sending the POST request
			
			URL url = new URL("http://10.0.1.86/tatoc/advanced/rest/service/register");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
		
			conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			
			String string= "id="+p[1]+"& signature="+t+"&allow_access=1";
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(string);
			wr.flush();
			wr.close();

			int responseCode = conn.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Post parameters : " + string);
			System.out.println("Response Code : " + responseCode);

////			BufferedReader in = new BufferedReader(
////			        new InputStreamReader(conn.getResponseMessage()));
//			String inputLine;
//			StringBuffer response = new StringBuffer();
//
//			while ((inputLine = in.readLine()) != null) 
//			{// TODO Auto-generated method stub
//				response.append(inputLine);
//			}
//			in.close();
//			
//			//print result
//			System.out.println(response.toString());
		    System.out.println(conn.getResponseMessage());
			conn.disconnect();
			driver.navigate().back();
			Thread.sleep(1000);
		    driver.findElement(By.linkText("Proceed")).click();
			
	  //problem5
			driver.findElement(By.linkText("Download File")).click();
			
			Thread.sleep(4000);
					BufferedReader b = null;
			List<String> strings=null;
			try {

				String s1;

				b = new BufferedReader(new FileReader("/home/girvanasahchaurasia/Downloads/file_handle_test.dat"));

				strings= new ArrayList<String>();
				
				while ((s1 = b.readLine()) != null) 
				{
					strings.add(s1);
				}

			} 
			catch (IOException e)
			{
				e.printStackTrace();
			} 
			finally 
			{
				try 
				{
					if (b != null)
						b.close();
				} 
				catch (IOException ex)
				{
					ex.printStackTrace();
				}
			}

		    String signature= strings.get(2);
		    signature= signature.substring(11);
		    driver.findElement(By.cssSelector("#signature")).sendKeys(signature);
		    driver.findElement(By.cssSelector(".submit")).click();
			
			
			}
}
			
			
			


