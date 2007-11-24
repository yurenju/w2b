package org.w2b.blog;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

public class WPReader {
	private String filename;
	private Document document;
	private Wordpress wordpress;
	private Namespace nwp;
	private Namespace ncontent;
	private SimpleDateFormat format;
	/**
	 * Wordpress XML Reader
	 * @param filename - XML file name
	 */
	public WPReader(String filename) {
		this.filename = filename;
		wordpress = new Wordpress();
		nwp = Namespace.getNamespace("wp", "http://wordpress.org/export/1.0/");
		ncontent = Namespace.getNamespace("content", "http://purl.org/rss/1.0/modules/content/");
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
	}
	/**
	 * Open the XML file
	 * @throws IOException
	 */
	public void open() throws IOException {
		SAXBuilder builder = new SAXBuilder();
		try {
			document = builder.build(filename);
		} catch (JDOMException e) {
			throw new IOException("XML Format Error\n"+e.getMessage());
		} 
	}
	/**
	 * Read XML to Wordpress Class
	 * @return <code>Wordpress</code>
	 * @throws IOException
	 */
	public Wordpress read() throws IOException {
		Element root = document.getRootElement();
		readcategory(root);
		readitem(root);		
		return wordpress;
	}
	private void readcategory(Element root) {
		List<Element> eCategories = root.getChild("channel").getChildren("category", nwp);
		for (Element c : eCategories) {
			wordpress.addCategory(c.getChildText("cat_name", nwp));
		}
	}
	private void readitem(Element root) throws IOException {
		List<Element> eItems = root.getChild("channel").getChildren("item");
		for (Element item : eItems) {
			WPItem wpItem = new WPItem();
			// Title
			wpItem.setTitle(item.getChildText("title"));
			// Content
			wpItem.setContent(item.getChildText("encoded", ncontent));
			// status
			if (item.getChildText("status", nwp).compareTo("publish") == 0) {
				wpItem.setStatus(WPItem.STATUS_PUBLISH);
			}
			else { // draft
				wpItem.setStatus(WPItem.STATUS_DRAFT);
			}
			
			// category			
			Vector<String> vecCategory = new Vector<String>();
			List<Element> eItemCategories = item.getChildren("category");
			String[] itemCategories;
			for (Element c : eItemCategories) {
				vecCategory.add(c.getText());
			}
			itemCategories = new String[vecCategory.size()];
			wpItem.setCategories(vecCategory.toArray(itemCategories));
			
			// postDate
			
			try {
				wpItem.setPostDate(format.parse( item.getChildText("post_date", nwp) ));
			} catch (ParseException e1) {
				throw new IOException(e1.getMessage());
			}
			readcomments(item,wpItem);
			
			wordpress.addItem(wpItem);
		}		
	}
	private void readcomments(Element item,WPItem wpItem) throws IOException {
		List<Element> eComments = item.getChildren("comment", nwp);
        Vector<WPComment> vecComments = new Vector<WPComment>();
        WPComment[] comments;
                    
        for (Element c : eComments) {
            WPComment comment = new WPComment();
            comment.setAuthor(c.getChildText("comment_author", nwp));
            comment.setContent(c.getChildText("comment_content", nwp));
            comment.setAuthorEmail(c.getChildText("comment_author_email", nwp));
            comment.setAuthorUrl(c.getChildText("comment_author_url", nwp));
            try {
				comment.setDate(format.parse( c.getChildText("comment_date", nwp) ));
			} catch (ParseException e) {
				throw new IOException(e.getMessage());
			}
            vecComments.add(comment);
        }
        comments = new WPComment[vecComments.size()];
        wpItem.setComments(vecComments.toArray(comments));
	}
}
