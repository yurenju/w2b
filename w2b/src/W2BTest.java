

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.Scanner;

import org.jdom.JDOMException;
import org.w2b.blog.W2B;
import org.w2b.blog.WPReader;
import org.w2b.blog.Wordpress;

import com.google.gdata.util.ServiceException;

public class W2BTest {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Wordpress to Blogger(w2b)");
		System.out.print("Xml File: ");
		String wpXml = scanner.next();
		System.out.print("Blog URL: ");
		String blogUrl = scanner.next();
		System.out.print("username: ");
		String username = scanner.next();
		System.out.print("password: ");
		String password = scanner.next();
		
		Wordpress w = new Wordpress();
		WPReader reader = new WPReader(wpXml);
		System.out.println("Reading...");
		try {
			reader.open();
			w = reader.read();
		} catch (JDOMException e) {
			e.printStackTrace();
//			return;
		} catch (IOException e) {
			e.printStackTrace();
//			return;
		}
		System.out.println("Read XML Done...");
		
		System.out.println("Connect to Blogger...");
		W2B w2b = new W2B(username, password, blogUrl);
		try {
			w2b.connect();
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println(e1.getMessage());
			return;
		} catch (ServiceException e1) {
			e1.printStackTrace();
			System.out.println(e1.getMessage());
			return;
		}
		System.out.println("Posting...");
		for (int i = 0; i < w.getItems().length; i++) {
			System.out.println( (i+1) + ": " + w.getItems()[i].getTitle());
			try {
				w2b.addPost(w.getItems()[i]);
			} 
//			catch (MalformedURLException e) {
//				e.printStackTrace();
//				System.err.println(e.getMessage());
//				return;
//			} 
			catch (IOException e) {
				e.printStackTrace();
				System.err.println(e.getMessage());
				return;
			} 
		}
				
		System.out.println("All Done...");
	}
}
