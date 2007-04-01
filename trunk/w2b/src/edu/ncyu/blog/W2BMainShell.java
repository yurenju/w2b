package edu.ncyu.blog;

import java.io.IOException;
import java.text.ParseException;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Link;
import org.jdom.JDOMException;

import com.google.gdata.data.Feed;
import com.google.gdata.util.ServiceException;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class W2BMainShell {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private Label label = null;
	private Label label1 = null;
	private Text blogUrl = null;
	private Label label2 = null;
	private Text username = null;
	private Label label3 = null;
	private Text password = null;
	private Label label4 = null;
	private Label label5 = null;
	private Button buttonEnter = null;
	private ProgressBar progressBar = null;
	private Link link = null;
	private Text filename = null;
	private Button button = null;
	private Display display = null;  //  @jve:decl-index=0:
	private Button delete = null;
	private Table table = null;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/* Before this is run, be sure to set up the launch configuration (Arguments->VM Arguments)
		 * for the correct SWT library path in order to run with the SWT dlls. 
		 * The dlls are located in the SWT plugin jar.  
		 * For example, on Windows the Eclipse SWT 3.1 plugin jar is:
		 *       installation_directory\plugins\org.eclipse.swt.win32_3.1.0.jar
		 */
		Display display = Display.getDefault();
		W2BMainShell thisClass = new W2BMainShell();
		thisClass.createSShell();
		thisClass.sShell.open();
		thisClass.setDisplay(display);

		while (!thisClass.sShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		GridData gridData13 = new GridData();
		gridData13.heightHint = 120;
		gridData13.horizontalSpan = 4;
		gridData13.widthHint = 315;
		GridData gridData41 = new GridData();
		gridData41.horizontalSpan = 2;
		GridData gridData32 = new GridData();
		gridData32.horizontalSpan = 2;
		GridData gridData22 = new GridData();
		gridData22.horizontalSpan = 2;
		GridData gridData12 = new GridData();
		gridData12.horizontalSpan = 2;
		GridData gridData6 = new GridData();
		gridData6.horizontalSpan = 2;
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData5 = new GridData();
		gridData5.horizontalSpan = 2;
		GridData gridData4 = new GridData();
		gridData4.widthHint = 160;
		GridData gridData21 = new GridData();
		gridData21.horizontalSpan = 4;
		gridData21.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData21.widthHint = 320;
		GridData gridData11 = new GridData();
		gridData11.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		GridData gridData3 = new GridData();
		gridData3.widthHint = 200;
		gridData3.horizontalSpan = 2;
		GridData gridData2 = new GridData();
		gridData2.widthHint = 200;
		gridData2.horizontalSpan = 2;
		GridData gridData1 = new GridData();
		gridData1.horizontalSpan = 2;
		GridData gridData = new GridData();
		gridData.widthHint = 200;
		gridData.horizontalSpan = 2;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		sShell = new Shell();
		sShell.setText("Shell");
		sShell.setLayout(gridLayout);
		sShell.setSize(new Point(357, 383));
		label = new Label(sShell, SWT.NONE);
		label.setText("Wordpress Export XML:");
		label.setLayoutData(gridData41);
		filename = new Text(sShell, SWT.BORDER);
		filename.setLayoutData(gridData4);
		button = new Button(sShell, SWT.NONE);
		button.setText("Open");
		button.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				System.out.println("widgetSelected()"); // TODO Auto-generated Event stub widgetSelected()
				FileDialog dialog = new FileDialog(sShell, SWT.OPEN);
				dialog.setFilterNames(new String[] {"XML File (*.xml)"});
				dialog.setFilterExtensions(new String[] {"*.xml"});
				String strFilename = dialog.open();
				filename.setText(strFilename);				
			}
		});
		label1 = new Label(sShell, SWT.NONE);
		label1.setText("Blogger Blog URL:");
		label1.setLayoutData(gridData32);
		blogUrl = new Text(sShell, SWT.BORDER);
		blogUrl.setLayoutData(gridData);
		Label filler2 = new Label(sShell, SWT.NONE);
		Label filler5 = new Label(sShell, SWT.NONE);
		label2 = new Label(sShell, SWT.NONE);
		label2.setText("ex: http://yurinfore.blogspot.com/");
		label2.setFont(new Font(Display.getDefault(), "\u65b0\u7d30\u660e\u9ad4", 9, SWT.BOLD));
		label2.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		label2.setLayoutData(gridData1);
		label4 = new Label(sShell, SWT.NONE);
		label4.setText("Username:");
		label4.setLayoutData(gridData22);
		username = new Text(sShell, SWT.BORDER);
		username.setLayoutData(gridData3);
		Label filler1 = new Label(sShell, SWT.NONE);
		Label filler3 = new Label(sShell, SWT.NONE);
		label5 = new Label(sShell, SWT.NONE);
		label5.setText("ex: yurenju@gmail.com");
		label5.setFont(new Font(Display.getDefault(), "\u65b0\u7d30\u660e\u9ad4", 9, SWT.BOLD));
		label5.setLayoutData(gridData5);
		label5.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		label3 = new Label(sShell, SWT.NONE);
		label3.setText("Password:");
		label3.setLayoutData(gridData12);
		password = new Text(sShell, SWT.BORDER | SWT.PASSWORD);
		password.setLayoutData(gridData2);
		progressBar = new ProgressBar(sShell, SWT.NONE);
		progressBar.setLayoutData(gridData21);
		table = new Table(sShell, SWT.BORDER);
		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setWidth(40);
		tableColumn.setText("No.");
		TableColumn tableColumn1 = new TableColumn(table, SWT.NONE);
		tableColumn1.setWidth(285);
		tableColumn1.setText("Title");
		buttonEnter = new Button(sShell, SWT.NONE);
		buttonEnter.setText("Import");
		buttonEnter.setLayoutData(gridData11);
		delete = new Button(sShell, SWT.NONE);
		delete.setText("Delete All");
		delete.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				final String strUsername = username.getText();
				final String strPassword = password.getText();
				final String strBlogUrl = blogUrl.getText();
				final Display d = display;

				switchEnable();
				
				new Thread() {
					public void run() {
						W2B w2b = new W2B(strUsername, strPassword, strBlogUrl);
						try {
							w2b.connect();
							final Feed feed = w2b.getPosts();
							
							d.asyncExec(new Runnable() {
								public void run() {
								if (table.isDisposed ()) return;
								table.clearAll();
									for (int i = 0; i < feed.getEntries().size(); i++) {
										TableItem item = new TableItem(table, SWT.NONE);
										item.setText(0, Integer.toString(i+1));
										item.setText(1, feed.getEntries().get(i).getTitle().getPlainText());
									}
								}
							});
							
							for (final int[] i = new int[1]; i[0] < feed.getEntries().size(); i[0]++) {
								w2b.deletePost(feed.getEntries().get(i[0]).getEditLink().getHref());
								
								d.asyncExec(new Runnable() {
									public void run() {
									if (progressBar.isDisposed ()) return;
										progressBar.setSelection(i[0]/feed.getEntries().size()*100);
										TableItem tableItem = table.getItem(i[0]);
										tableItem.setFont(new Font(Display.getDefault(), "\u65b0\u7d30\u660e\u9ad4", 9, SWT.BOLD));
									}
								});
							}
							d.asyncExec(new Runnable() {
								public void run() {
								if (delete.isDisposed ()) return;
									switchEnable();
								}
							});
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ServiceException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
					}
				}.start();
				
			}
		});
		buttonEnter.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				final String strUsername = username.getText();
				final String strPassword = password.getText();
				final String strBlogUrl = blogUrl.getText();
				final Display d = display;
				
				switchEnable();
				final WPReader reader = new WPReader(filename.getText());
				
				try {
					reader.open();
				} catch (JDOMException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				new Thread() {
					public void run() {
						try {
							final Wordpress w = reader.read();
							W2B w2b = new W2B(strUsername, strPassword, strBlogUrl);
							w2b.connect();

							d.asyncExec(new Runnable() {
								public void run() {
								if (table.isDisposed ()) return;
								table.clearAll();
									for (int i = 0; i < w.getItems().length; i++) {
										TableItem item = new TableItem(table, SWT.NONE);
										item.setText(0, Integer.toString(i+1));
										item.setText(1, w.getItems()[i].getTitle());
									}
								}
							});
							
							for (final int[] i = new int[1]; i[0] < w.getItems().length; i[0]++) {
								w2b.addPost(w.getItems()[i[0]]);
								
								d.asyncExec(new Runnable() {
									public void run() {
									if (progressBar.isDisposed ()) return;
										progressBar.setSelection(i[0]/w.getItems().length*10);
										TableItem tableItem = table.getItem(i[0]);
										tableItem.setFont(new Font(Display.getDefault(), "\u65b0\u7d30\u660e\u9ad4", 9, SWT.BOLD));
									}
								});
							}
							d.asyncExec(new Runnable() {
								public void run() {
								if (progressBar.isDisposed ()) return;
									switchEnable();
								}
							});
							
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (ServiceException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}.start();
			}
		});
		link = new Link(sShell, SWT.NONE);
		link.setText("<a>Yuren's Info Area</a>");
		link.setLayoutData(gridData6);
		table.setHeaderVisible(true);
		table.setLayoutData(gridData13);
		table.setLinesVisible(true);
	}

	public Display getDisplay() {
		return display;
	}

	public void setDisplay(Display display) {
		this.display = display;
	}
	
	private void switchEnable() {
		filename.setEnabled(!filename.isEnabled());
		blogUrl.setEnabled(!blogUrl.isEnabled());
		username.setEnabled(!username.isEnabled());
		button.setEnabled(!button.isEnabled());
		buttonEnter.setEnabled(!buttonEnter.isEnabled());
		delete.setEnabled(!delete.isEnabled());
		password.setEnabled(!password.isEnabled());
	}

}
