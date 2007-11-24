package org.w2b.blog;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.TimeZone;

import com.google.gdata.client.*;
import com.google.gdata.data.*;
import com.google.gdata.util.*;

public class W2B {
	private String username;
	private String password;
	private String blogUrl;
	private String postUrl;
	private GoogleService service;
	private Person author;
	private Wordpress wordpress;
	private WPReader reader;
	/**
	 * <p>w2b base information</p>
	 * @param username <code>String</code> - Google Account username
	 * @param passoword <code>String</code> - Google Account password
	 * @param blogUrl <code>String</code> - Blog's URL
	 * @param XML <code>String</code> - XML File
	 * 
	 */
	public W2B(String username, String password, String blogUrl,String XML) {
		this.username = username;
		this.password = password;
		this.blogUrl = blogUrl;
		this.wordpress = new Wordpress();
		this.reader = new WPReader(XML);
	}
	/**
	 * <p>Connent to Google Blogger Server</p>
	 * @throws <code>ServiceException</code> - Service Connent Error
	 */
	public void connect() throws ServiceException, IOException {
		service = new GoogleService("blogger", "Wordpress2Blogger");
		
		try {
			service.setUserCredentials(username, password);
		} catch (AuthenticationException e) {
			throw new ServiceException("Connent Google Account Error.\n Please Check username and password.\n");
		}
		getPostUrl();
	}
	/**
	 * Get Posts Feed
	 * @return <code>Feed</code>
	 * @throws IOException
	 * @throws ServiceException
	 */
	public Feed getPosts() throws IOException, ServiceException {
		return service.getFeed(new URL(blogUrl + "feeds/posts/default"), Feed.class);	
	}
	/**
	 * Delete Post on Server
	 * @param editLink <code>String</code> - Delete Post Link
	 * @throws ServiceException - Server Exception 
	 * @throws IOException - IO Exception
	 */
	public void deletePost(String editLink)throws ServiceException, IOException {
		service.delete(new URL(editLink));
	}

	public Entry updatePostTitle(
		      Entry entryToUpdate, String newTitle) throws ServiceException,
		      IOException {
		    entryToUpdate.setTitle(new PlainTextConstruct(newTitle));
		    URL editUrl = new URL(entryToUpdate.getEditLink().getHref());
		    return service.update(editUrl, entryToUpdate);
	}
	/**
	 * Post Item to Blogger
	 * @param wpItem <code>WPItem</code> - Wordpress Item
	 * @throws IOException
	 */
	public void addPost(WPItem wpItem) throws IOException {
		Entry entry = new Entry();
		Entry resultEntry = new Entry();
		/* Set Authors */
		entry.getAuthors().add(author);
		/* Set Title */
		entry.setTitle(new PlainTextConstruct(wpItem.getTitle()));
		/* Set Content */
		entry.setContent(new PlainTextConstruct(wpItem.getContent()));
		/* Set Time */
		entry.setPublished(new DateTime(wpItem.getPostDate(), TimeZone.getDefault()));
		/* Set Ssatus */
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
		// try to Post
		try {
			resultEntry = service.insert(new URL(postUrl), entry);
		}
		catch (ServiceException se) {
			throw new IOException("Post error.\nTitle: "+ wpItem.getTitle());
		}
		// Post comment
		postcomment(wpItem,resultEntry);
		
	}
	private void postcomment(WPItem wpItem,Entry resultEntry) throws IOException {
		if (wpItem.getStatus() != WPItem.STATUS_DRAFT && wpItem.getComments().length > 0) {
			String[] sp = resultEntry.getId().split("-");
			String postId = sp[sp.length-1];
			URL commentUrl = new URL(blogUrl + "feeds/" + postId + "/comments/default");
			
			for (WPComment wpComment : wpItem.getComments()) {
				Entry comment = new Entry();
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
					throw new IOException(
							"Post comment error.\nPostId: "+ postId 
							+ ", title: " + wpItem.getTitle() 
							+ ", comment author: " + wpComment.getAuthor()
							);
				}
			}
		}
	}
	
	private void getPostUrl() throws IOException, ServiceException {
		Feed feed = service.getFeed(new URL("http://www.blogger.com/feeds/default/blogs"), Feed.class);
		/* Set author */
		this.author = feed.getAuthors().get(0);
		/* Set Post Url */
		List<Entry> entries = feed.getEntries();
		for (Entry entry : entries) {
			if (entry.getLink("alternate", "text/html").getHref().compareTo(blogUrl) == 0) {
				Link link = entry.getLink("http://schemas.google.com/g/2005#post", "application/atom+xml");
				postUrl = link.getHref();
			}			
		}		
	}
	/**
	 * <p>Read XML File</p>
	 * @throws <code>IOException</code> - Open,Read XML File Error 
	 */
	public void read() throws IOException{
		reader.open();
		wordpress = reader.read();		
	}
	/**
	 * Post Wordpress Items to Blogger
	 * @throws IOException
	 */
	public void post() throws IOException{
		for (int i = 0; i < count(); i++) {
			System.out.println( (i+1) + "/" + count() + ": " + wordpress.getItems()[i].getTitle());
			addPost(wordpress.getItems()[i]); 
		}
	}
	public WPItem[] getItems() {
		return wordpress.getItems();
	}
	/**
	 * Item Count
	 * @return <code>int</code> - Wordpress Item count
	 */
	public int count() {
		return wordpress.getItems().length;
	}
}
