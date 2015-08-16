package org.yuriak.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.SwingWorker;

import org.yuriak.bean.StockBean;
import org.yuriak.crawler.StockIDCrawler;
import org.yuriak.crawler.StockInfoCrawler;
import org.yuriak.dao.StockDao;
import org.yuriak.util.MyFileUtil;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JLabel;

public class MainWindow {

	private JFrame frame;
	JLabel Label = new JLabel("");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton StartButton = new JButton("Start");
		StartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new SwingWorker<Void, Void>() {

					@Override
					protected Void doInBackground() throws Exception {
						// TODO Auto-generated method stub
						Thread thread=new Thread(new Runnable() {
							
							@Override
							public void run() {
								try {
									System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
									StockIDCrawler crawler=new StockIDCrawler("data");
									StockInfoCrawler infoCrawler=new StockInfoCrawler("data");
									ArrayList<StockBean> stocks=infoCrawler.getStockInfo(crawler.getAllStock());
									Collections.sort(stocks);
									MyFileUtil.writeInfoToFile(stocks);
								} catch (Exception e2) {
									e2.printStackTrace();
								}
								// TODO Auto-generated method stub
							}
						});
						thread.start();
						while (thread.isAlive()) {
							Label.setText("running...");
						}
						Label.setText("done");
						return null;
					}
				};
			}
		});
		StartButton.setBounds(81, 129, 93, 23);
		frame.getContentPane().add(StartButton);
		
		JLabel Label = new JLabel("");
		Label.setBounds(184, 133, 54, 15);
		frame.getContentPane().add(Label);
	}
}
