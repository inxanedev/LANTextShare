package com.inxane.lantextshare;

import java.awt.Component;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Main {
	
	static JFrame frame;
	static JLabel status;
	static JTextArea input;
	
	static String toShare;
	
	static ServerSocket server;
	static int port = 80;
	
	private static void setupUI() {
		frame = new JFrame("LANTextShare Server");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 300);
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		status = new JLabel("Server initializing...");
		status.setAlignmentX(Component.CENTER_ALIGNMENT);
		status.setBorder(new EmptyBorder(10, 0, 10, 0));
		status.setFont(new Font("Arial", Font.PLAIN, 25));
		
		frame.add(status);
		
		input = new JTextArea();
		input.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				toShare = input.getText();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				toShare = input.getText();
				
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// never fires
			}
		
		});
		input.setFont(new Font("Arial", Font.PLAIN, 15));
		
		JScrollPane pane = new JScrollPane(input);
		
		frame.add(pane);
		
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		setupUI();
		
		try {
			server = new ServerSocket(port);
			status.setText(InetAddress.getLocalHost().getHostAddress() + ":" + port);
			while (true) {
				Socket client = server.accept();
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
				
				String data = input.getText();
				
				writer.write("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + data.length() + "\r\n\r\n" + data);
				writer.flush();
				
				client.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

}
