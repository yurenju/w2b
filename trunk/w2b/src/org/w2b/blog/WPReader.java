package org.w2b.blog;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import com.google.gdata.util.ServiceException;

public class WPReader {
	private String filename;
	private Document document;
	
	WPReader(String filename) {
		this.filename = filename;
	}
	
	public void open() throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		document = builder.build(filename);
	}
	
	public Wordpress read() throws ParseException {
		Wordpress w = new Wordpress();
		Namespace nwp = Namespace.getNamespace("wp", "http://wordpress.org/export/1.0/");
		Namespace ncontent = Namespace.getNamespace("content", "http://purl.org/rss/1.0/modules/content/");
		Element root = document.getRootElement();
		
		// category
		List<Element> eCategories = root.getChild("channel").getChildren("category", nwp);
		for (Element c : eCategories) {
			w.addCategory(c.getChildText("category_nicename", nwp));
		}
		
		// item
		List<Element> eItems = root.getChild("channel").getChildren("item");
		for (Element item : eItems) {
			WPItem wpItem = new WPItem();
			
			wpItem.setTitle(item.getChildText("title"));
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
			Element ePostDate = item.getChild("post_date", nwp);
			String strDate = ePostDate.getText();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
			Date date = format.parse(strDate);
			wpItem.setPostDate(date);
			
			// comments
			List<Element> eComments = item.getChildren("comment", nwp);
            Vector<WPComment> vecComments = new Vector<WPComment>();
            WPComment[] comments;
            
            for (Element c : eComments) {
                WPComment comment = new WPComment();
                comment.setAuthor(c.getChildText("comment_author", nwp));
                comment.setContent(c.getChildText("comment_content", nwp));
                comment.setAuthorEmail(c.getChildText("comment_author_email", nwp));
                comment.setAuthorUrl(c.getChildText("comment_author_url", nwp));
                comment.setDate(format.parse(c.getChildText("comment_date", nwp)));
                vecComments.add(comment);
            }
            comments = new WPComment[vecComments.size()];
            wpItem.setComments(vecComments.toArray(comments));
			
			w.addItem(wpItem);
		}
		
		return w;
	}
}
