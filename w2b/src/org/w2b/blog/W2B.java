package org.w2b.blog;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.jdom.JDOMException;

import com.google.gdata.client.GoogleService;
import com.google.gdata.data.Category;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.Entry;
import com.google.gdata.data.Feed;
import com.google.gdata.data.Link;
import com.google.gdata.data.Person;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class W2B {
	private String username;
	private String password;
	private String blogUrl;
	private String postUrl;
	private GoogleService service;
	private Person author;
	
	public W2B(String username, String password, String blogUrl) {
		super();
		this.username = username;
		this.password = password;
		this.blogUrl = blogUrl;
	}
	
	public boolean connect() throws IOException, ServiceException {
		service = new GoogleService("blogger", "Wordpress2Blogger");
		Feed resultFeed;
		
		try {
			service.setUserCredentials(username, password);		
		}
		catch (AuthenticationException ae) {
			return false;
		}
		getPostUrl();
		resultFeed = service.getFeed(new URL("http://www.blogger.com/feeds/default/blogs"), Feed.class);
		author = resultFeed.getAuthors().get(0);

		return true;
	}
	
	public Feed getPosts() throws IOException, ServiceException {
		URL feedUrl = new URL(blogUrl + "feeds/posts/default");
		return service.getFeed(feedUrl, Feed.class);	
	}
	
	public boolean deletePost(String editLink) {
		try {
			service.delete(new URL(editLink));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void addPost(WPItem wpItem) throws MalformedURLException, IOException {
		Entry entry = new Entry();
		Entry resultEntry = new Entry();
		
		entry.getAuthors().add(author);
		entry.setTitle(new PlainTextConstruct(wpItem.getTitle()));
		entry.setContent(new PlainTextConstruct(wpItem.getContent()));
		entry.setPublished(new DateTime(wpItem.getPostDate(), TimeZone.getDefault()));
		if (wpItem.getStatus() == WPItem.STATUS_DRAFT)
			entry.setDraft(true);
		else
			entry.setDraft(false);
		
		for (String category : wpItem.getCategories()) {
			Category c = new Category();
			c.setTerm(category);
			c.setScheme("http://www.blogger.com/atom/ns#");
			entry.getCategories().add(c);
		}
		
		try {
			resultEntry = service.insert(new URL(postUrl), entry);
		}
		catch (ServiceException se) {
			System.err.println("Post comment error.");
			System.err.println("title: " + wpItem.getTitle());
			return;
		}
		
		if (wpItem.getStatus() != WPItem.STATUS_DRAFT && wpItem.getComments().length > 0) {
			String[] sp = resultEntry.getId().split("-");
			String postId = sp[sp.length-1];
			URL commentUrl = new URL(blogUrl + "feeds/" + postId + "/comments/default");
			
			for (WPComment wpComment : wpItem.getComments()) {
				// FIXME ??Yuren's Info Area ‰∏äÁ? Spam
				if (wpComment.getContent().contains("pretty good!"))
					continue;
				
				Entry comment = new Entry();
				
//				Blogger API ?ÉÂøΩ?•Ê? Comment ?? AuthorÔºåÊ?‰∏ãÈù¢?ôÊÆµ?°‰ªª‰ΩïÊ???
//				Person commentAuthor = new Person(wpComment.getAuthor());
//				if (wpComment.getAuthorEmail() != null)
//					commentAuthor.setEmail(wpComment.getAuthorEmail());
//				if (wpComment.getAuthorUrl() != null)
//					commentAuthor.setUri(wpComment.getAuthorUrl());
//				comment.getAuthors().add(commentAuthor);
				
				StringBuffer sbContent = new StringBuffer();
				sbContent.append("Author: ");
				if (wpComment.getAuthorUrl() != null)
					sbContent.append(String.format("<a href='%s'>%s</a>", wpComment.getAuthorUrl(), wpComment.getAuthor()));
				else
					sbContent.append(wpComment.getAuthor());
				sbContent.append("\n\n");
				sbContent.append(wpComment.getContent());
				
				comment.setContent(new PlainTextConstruct(sbContent.toString()));
				comment.setPublished(new DateTime(wpComment.getDate(), TimeZone.getDefault()));
				try {
					service.insert(commentUrl, comment);
				}
				catch (ServiceException se) {
					System.err.println("Post comment error.");
					System.err.println("PostId: " + postId + ", title: " + wpItem.getTitle() + ", comment author: " + wpComment.getAuthor());
				}
			}
		}		
	}
	
	private void getPostUrl() throws IOException, ServiceException {
		URL feedUrl = new URL("http://www.blogger.com/feeds/default/blogs");
		Feed feed = service.getFeed(feedUrl, Feed.class);
		List<Entry> entries = feed.getEntries();
		for (Entry entry : entries) {
			if (entry.getLink("alternate", "text/html").getHref().compareTo(blogUrl) == 0) {
				Link link = entry.getLink("http://schemas.google.com/g/2005#post", "application/atom+xml");
				postUrl = link.getHref();
			}			
		}		
	}
	
	public static void main(String[] args) {
		String wpXml = args[0];
		Scanner scanner = new Scanner(System.in);
		System.out.print("Blog URL: ");
		String blogUrl = scanner.next();
		System.out.print("username: ");
		String username = scanner.next();
		System.out.print("password: ");
		String password = scanner.next();
		Wordpress w = new Wordpress();
		WPReader reader = new WPReader(wpXml);
		try {
			reader.open();
			w = reader.read();
		} catch (ParseException e) {
			// TODO ?™Â??¢Á? catch ???
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO ?™Â??¢Á? catch ???
			e.printStackTrace();
		} catch (IOException e) {
			// TODO ?™Â??¢Á? catch ???
			e.printStackTrace();
		}
		W2B w2b = new W2B(username, password, blogUrl);
		try {
			w2b.connect();
		} catch (IOException e1) {
			// TODO ?™Â??¢Á? catch ???
			e1.printStackTrace();
		} catch (ServiceException e1) {
			// TODO ?™Â??¢Á? catch ???
			e1.printStackTrace();
		}
		
		for (int i = 0; i < w.getItems().length; i++) {
			System.out.println( (i+1) + ": " + w.getItems()[i].getTitle());
			try {
				w2b.addPost(w.getItems()[i]);
			} catch (MalformedURLException e) {
				// TODO ?™Â??¢Á? catch ???
				e.printStackTrace();
			} catch (IOException e) {
				// TODO ?™Â??¢Á? catch ???
				e.printStackTrace();
			}
		}
	}
	
	
}
