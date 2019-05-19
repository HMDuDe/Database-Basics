package studentDB;

import java.awt.GridLayout;
import java.awt.event.*;
import java.sql.*;
import java.util.logging.*;

import javax.swing.*;

public class Student extends JFrame implements ActionListener{

	Connection conn = null;
	
	//Panels
	JPanel main = new JPanel(new GridLayout(2,1));
	JPanel topPanel = new JPanel(new GridLayout(4,2));
	JPanel bottomPanel = new JPanel(new GridLayout(2,2));
	JPanel connTestPanel = new JPanel(new GridLayout(2,1));
	JPanel connTestResultPanel = new JPanel(new GridLayout(2,1));
	JPanel connPanelBtns = new JPanel(new GridLayout(1,2));
	JPanel viewStudentsPanel = new JPanel();
	
	//Labels
	JLabel nameLbl = new JLabel("Name ");
	JLabel surnameLbl = new JLabel("Surname ");
	JLabel ageLbl = new JLabel("Age ");
	JLabel courseLbl = new JLabel("Course ");
	JLabel connectionTest = new JLabel("Connection Test Result: ");
	JLabel connTestresult = new JLabel("");
	
	//text fields
	JTextField nameTxt = new JTextField(25);
	JTextField surnameTxt = new JTextField(25);
	JTextField ageTxt = new JTextField(25);
	JTextField courseTxt = new JTextField(25);
	
	//Buttons for bottom panel
	JButton addStudentBtn = new JButton("Add Student");
	JButton cancelBtn = new JButton("Cancel");
	JButton viewStudentsBtn = new JButton("Next Student");
	JButton backToAddBtn = new JButton("Back");
	JButton closeBtn = new JButton("Close");
	JButton testConnBtn = new JButton("Test Connection");
	
	public Student() {
		
		super("Students");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500,600);
		
		add(main);
		
		main.add(topPanel);
		main.add(bottomPanel);
		
		//Configuring topPanel
		topPanel.add(nameLbl);
		topPanel.add(nameTxt);
		
		topPanel.add(surnameLbl);
		topPanel.add(surnameTxt);
		
		topPanel.add(ageLbl);
		topPanel.add(ageTxt);
		
		topPanel.add(courseLbl);
		topPanel.add(courseTxt);
		
		//Configuring bottomPanel
		bottomPanel.add(addStudentBtn);
		bottomPanel.add(viewStudentsBtn);
		bottomPanel.add(testConnBtn);
		bottomPanel.add(cancelBtn);
		
		addStudentBtn.addActionListener(this);
		viewStudentsBtn.addActionListener(this);
		testConnBtn.addActionListener(this);
		cancelBtn.addActionListener(this);
		
		//Configuring connTestPanel
		connTestResultPanel.add(connectionTest);
		connTestResultPanel.add(connTestresult);
		connTestPanel.add(connTestResultPanel);
		
		connPanelBtns.add(backToAddBtn);
		connPanelBtns.add(closeBtn);
		connTestPanel.add(connPanelBtns);
		
		closeBtn.addActionListener(this);
		backToAddBtn.addActionListener(this);
		
		setVisible(true);
	}
	
	public void testConn() throws ClassNotFoundException, SQLException {
		
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:MySQL://127.0.0.1/student", "root", "");
			connTestresult.setText("" + conn);
	}
	
	public void addStudent() {
		
	}
	public static void main(String[] args) {
		new Student();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		Object source = e.getSource();
		//If Test Connection button is pressed:
		if(source.equals(testConnBtn)) {
			main.remove(topPanel);
			main.remove(bottomPanel);
			main.add(connTestPanel);
			
			try {
				testConn();
			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}
			
		}else if(source.equals(cancelBtn)) {
			System.exit(0);
			
		}else if(source.equals(closeBtn)) {
			System.exit(0);
		}else if(source.equals(backToAddBtn)) {
			main.remove(connTestPanel);
			main.add(topPanel);
			main.add(bottomPanel);
		}
		
		//If Next Student button is pressed:
		else if(source.equals(viewStudentsBtn)) {
			if(conn == null) {
				
				try {
					Class.forName("com.mysql.jdbc.Driver");
					conn = DriverManager.getConnection("jdbc:MySQL://127.0.0.1/student", "root", "");
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
			}
			
			String selectQuery = "SELECT * FROM `student`";
			
			try {
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(selectQuery);
				
				//Unable to get this part to work
				if(rs.next()) {
					nameTxt.setText(rs.getString("Name"));
					surnameTxt.setText(rs.getString("Surname"));
					ageTxt.setText(rs.getString("Age"));
					courseTxt.setText(rs.getString("Course"));
				}
				
			}catch(SQLException sql) {
				sql.printStackTrace();
			}
		}
		
		//If Add Student button is pressed:
		else if(source.equals(addStudentBtn)) {
			if(conn == null) {
				
				try {
					Class.forName("com.mysql.jdbc.Driver");
					conn = DriverManager.getConnection("jdbc:MySQL://127.0.0.1/student", "root", "");
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
			}
			
			String insertQuery = "INSERT INTO `student`(`Name`, `Surname`, `Age`, `Course`) VALUES (?, ?, ?, ?);";
			
			try {
				Statement st = conn.createStatement();
				PreparedStatement insertStudent = conn.prepareStatement(insertQuery);
				
				insertStudent.setString(1, nameTxt.getText());
				insertStudent.setString(2, surnameTxt.getText());
				insertStudent.setInt(3, Integer.parseInt(ageTxt.getText()));
				insertStudent.setString(4, courseTxt.getText());
				
				insertStudent.executeUpdate();
				
			}catch(SQLException sql) {
				sql.printStackTrace();
			}
		}
	}
}
