
import java.util.Scanner;

import org.w2b.blog.W2B;

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
		
		W2B w2b = new W2B(username, password, blogUrl,wpXml);
		try {
		System.out.println("Reading...");
		w2b.read();
		System.out.println("Read "+w2b.count()+" items...");
		
		System.out.println("Connect to Blogger...");
		w2b.connect();

		System.out.println("Posting...");
		w2b.post();
		
		System.out.println("All Done...");
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
}
