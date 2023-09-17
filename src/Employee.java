import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Employee {

    private JTextField textName;
    private JTextField textPassowrd;
    private JTextField textPhoneNumber;
    private JTextField textAddress;
    private JTextField textDateOf;
    private JTextField textStatus;
    private JButton saveButton;
    private JTable table1;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton searchButton;
    private JTextField textId;
    private JPanel Main;


    public static void main(String[] args) {
        JFrame frame = new JFrame("Employee");
        frame.setContentPane(new Employee().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    Connection con;
    PreparedStatement pst;

    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/mydb", "root", "Root");
            System.out.println("Successs");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void table_load() {
        try {
            pst = con.prepareStatement("select * from UserMaster");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Employee() {
        connect();
        table_load();
        saveButton.addActionListener(new ActionListener() { //save button
            @Override
            public void actionPerformed(ActionEvent e) {
                String UserName, UserPassword, UserPhoneNumber, Address, DateOfRegistration, Status;
                UserName = textName.getText();
                UserPassword = textPassowrd.getText();
                UserPhoneNumber = textPhoneNumber.getText();
                Address = textAddress.getText();
                DateOfRegistration = textDateOf.getText();
                Status = textStatus.getText();

                try {
                    // Parse the DateOfRegistration string into a java.sql.Date
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    java.util.Date parsedDate = dateFormat.parse(DateOfRegistration);
                    java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());

                    pst = con.prepareStatement("insert into UserMaster(UserName, UserPassword, UserPhoneNumber, Address, DateOfRegistration, Status) values(?,?,?,?,?,?)");
                    pst.setString(1, UserName);
                    pst.setString(2, UserPassword);
                    pst.setString(3, UserPhoneNumber);
                    pst.setString(4, Address);
                    pst.setDate(5, sqlDate); // Use setDate to insert a date value
                    pst.setString(6, Status);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Added!!!!!");
                    textName.setText("");
                    textPassowrd.setText("");
                    textPhoneNumber.setText("");
                    textAddress.setText("");
                    textDateOf.setText("");
                    textStatus.setText("");
                    textName.requestFocus();
                } catch (ParseException | SQLException e1) {
                    e1.printStackTrace();
                }


            }
        });
        searchButton.addActionListener(new ActionListener() { //search button
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    String UserID = textId.getText();

                    pst = con.prepareStatement("select UserName, UserPassword, UserPhoneNumber, Address, DateOfRegistration, Status from UserMaster where UserID = ?");
                    pst.setString(1, UserID);
                    ResultSet rs = pst.executeQuery();

                    if (rs.next() == true) {
                        String UserName = rs.getString(1);
                        String UserPassword = rs.getString(2);
                        String UserPhoneNumber = rs.getString(3);
                        String Address = rs.getString(4);
                        String DateOfRegistration = rs.getString(5);
                        String Status = rs.getString(6);


                        textName.setText(UserName);
                        textPassowrd.setText(UserPassword);
                        textPhoneNumber.setText(UserPhoneNumber);
                        textAddress.setText(Address);
                        textDateOf.setText(DateOfRegistration);
                        textStatus.setText(Status);


                    } else {
                        textName.setText("");
                        textPassowrd.setText("");
                        textPhoneNumber.setText("");
                        textAddress.setText("");
                        textDateOf.setText("");
                        textStatus.setText("");

                        JOptionPane.showMessageDialog(null, "Invalid Employee No");

                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String UserName, UserPassword, UserPhoneNumber, Address, DateOfRegistration, Status, UserID;
                UserName = textName.getText();
                UserPassword = textPassowrd.getText();
                UserPhoneNumber = textPhoneNumber.getText();
                Address = textAddress.getText();
                DateOfRegistration = textDateOf.getText();
                Status = textStatus.getText();
                UserID = textId.getText();



                try {
                    pst = con.prepareStatement("update UserMaster set UserName = ?, UserPassword = ?, UserPhoneNumber = ?, Address = ?, DateOfRegistration = ?, Status = ? where UserID = ?");
                    pst.setString(1, UserName);
                    pst.setString(2, UserPassword);
                    pst.setString(3, UserPhoneNumber);
                    pst.setString(4, Address);
                    pst.setString(5, DateOfRegistration);
                    pst.setString(6, Status);
                    pst.setString(7, UserID);

                    System.out.println("Status" + Status);

                    try {
                        pst.executeUpdate();
                        System.out.println("UserID: " + UserID);
                        JOptionPane.showMessageDialog(null, "Record Updateee!!!!!");
                        table_load();
                        textId.setText("");
                        textName.setText("");
                        textPassowrd.setText("");
                        textPhoneNumber.setText("");
                        textAddress.setText("");
                        textDateOf.setText("");
                        textStatus.setText("");
                        textName.requestFocus();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                        System.out.println("SQL Exception: " + e1.getMessage()); // Print SQL exception details
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });


        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String UserID;
                UserID = textId.getText();

                try {
                    pst = con.prepareStatement("delete from UserMaster  where UserID = ?");

                    pst.setString(1, UserID);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Deleteeeeee!!!!!");
                    table_load();
                    textName.setText("");
                    textPassowrd.setText("");
                    textPhoneNumber.setText("");
                    textAddress.setText("");
                    textDateOf.setText("");
                    textStatus.setText("");

                    textName.requestFocus();
                } catch (SQLException e1) {

                    e1.printStackTrace();
                }
            }

        });
    }
}