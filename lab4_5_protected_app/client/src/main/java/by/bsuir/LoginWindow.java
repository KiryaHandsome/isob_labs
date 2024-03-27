package by.bsuir;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.StringEntity;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


public class LoginWindow extends JFrame {

    private final Gson gson = new Gson();
    private Role userRole = null;
    private java.util.List<Resource> resourceList;

    public LoginWindow() {
        // Set the title of the window
        setTitle("Main Window");

        // Create a panel to hold the components
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5); // Padding

        // Username Label
        JLabel usernameLabel = new JLabel("Username:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(usernameLabel, constraints);

        // Username TextField
        JTextField usernameField = new JTextField(20);
        usernameField.setDocument(new LengthRestrictedDocument(20));
        constraints.gridx = 1;
        constraints.gridy = 0;
        panel.add(usernameField, constraints);

        // Password Label
        JLabel passwordLabel = new JLabel("Password:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(passwordLabel, constraints);

        // Password TextField
        JPasswordField passwordField = new JPasswordField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        passwordField.setDocument(new LengthRestrictedDocument(20));
        panel.add(passwordField, constraints);


        JButton submitButton = getSubmitButton(usernameField, passwordField);
        constraints.gridx = 1;
        constraints.gridy = 2;
        panel.add(submitButton, constraints);

        // Add the panel to the content pane
        add(panel);

        // Set the default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Pack the components in the window
        pack();

        // Center the window on the screen
        setLocationRelativeTo(null);
    }

    private JButton getSubmitButton(JTextField usernameField, JPasswordField passwordField) {
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try {
                sendLoginRequest(username, password);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(LoginWindow.this, "Failed to send request", "Error", JOptionPane.ERROR_MESSAGE);
            }

            System.out.println("Entered values: Username=" + username + ", Password=" + password);
        });
        return submitButton;
    }

    private void sendLoginRequest(String username, String password) throws IOException {
        String json = gson.toJson(new LoginRequest(username, password));
        final HttpPost httpPost = new HttpPost("http://localhost:8080/auth/login");

        final StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = (CloseableHttpResponse) client.execute(httpPost, httpResponse -> {
                 HttpEntity responseEntity = httpResponse.getEntity();
                 System.out.println("Response status code=" + httpResponse.getCode());
                 String jsonResponse = readInputStream(responseEntity.getContent());
                 if (httpResponse.getCode() == HttpStatus.SC_OK) {
                     userRole = gson.fromJson(jsonResponse, Role.class);
                     System.out.println("Response role=" + userRole);

                     JOptionPane.showMessageDialog(this, "Successful login. Your role is " + userRole.name(),
                             "Success", JOptionPane.INFORMATION_MESSAGE);
                     openNewPage();
                 } else {
                     JOptionPane.showMessageDialog(this, "Error occurred. Message: " + jsonResponse,
                             "Success", JOptionPane.ERROR_MESSAGE);
                     System.out.println("ERROR Message: " + jsonResponse);
                 }

                 return httpResponse;
             })) {
        }
    }

    private void openNewPage() {
        final HttpGet httpGet = new HttpGet("http://localhost:8080/resource");
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("role", userRole.name());

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = (CloseableHttpResponse) client.execute(httpGet, httpResponse -> {
                 HttpEntity responseEntity = httpResponse.getEntity();
                 System.out.println("Response status code=" + httpResponse.getCode());
                 var json = readInputStream(responseEntity.getContent());
                 if (httpResponse.getCode() == HttpStatus.SC_OK) {
                     resourceList = gson.fromJson(json, new TypeToken<List<Resource>>() {
                     }.getType());
                     System.out.println("Data received: " + resourceList);
                 } else {
                     System.out.println("ERROR message: " + json);
                 }
                 return httpResponse;
             })) {
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addDataList();
    }

    private void addDataList() {
        setTitle("Hello, " + userRole.name() + "!");
        getContentPane().removeAll();
        String[] columnNames = {"Id", "Data"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Resource resource : resourceList) {
            Object[] rowData = {resource.id(), resource.data()};
            tableModel.addRow(rowData);
        }
        var table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel(new FlowLayout());
        JLabel label = new JLabel("Enter text:");
        var textField = new JTextField(20);
        textField.setDocument(new LengthRestrictedDocument(256));
        JButton submitButton = new JButton("Submit");

        // Add action listener to the submit button
        submitButton.addActionListener(listener -> {
            if (Role.ADMIN.equals(userRole)) {
                String data = textField.getText();
                try {
                    createNewDataRow(data);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                JOptionPane.showMessageDialog(this, "You don't have access to create data",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add components to the panel
        panel.add(label);
        panel.add(textField);
        panel.add(submitButton);

        // Add the panel to the content pane
        getContentPane().add(panel, BorderLayout.NORTH);


        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    final class LengthRestrictedDocument extends PlainDocument {

        private final int limit;

        public LengthRestrictedDocument(int limit) {
            this.limit = limit;
        }

        @Override
        public void insertString(int offs, String str, AttributeSet a)
                throws BadLocationException {
            if (str == null)
                return;

            if ((getLength() + str.length()) <= limit) {
                super.insertString(offs, str, a);
            }
        }
    }

    private void createNewDataRow(String data) throws IOException {
        String json = gson.toJson(new ResourceCreateRequest(data));
        final HttpPost httpPost = new HttpPost("http://localhost:8080/resource");

        final StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("role", userRole.name());
        httpPost.setHeader("Content-type", "application/json");

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = (CloseableHttpResponse) client.execute(httpPost, httpResponse -> {
                 HttpEntity responseEntity = httpResponse.getEntity();
                 System.out.println("Response status code=" + httpResponse.getCode());
                 String jsonResponse = readInputStream(responseEntity.getContent());
                 if (httpResponse.getCode() == HttpStatus.SC_OK) {
                     getContentPane().removeAll();
                     addDataList();
                     openNewPage();
                 } else {
                     System.out.println("ERROR Message: " + jsonResponse);
                 }
                 return httpResponse;
             })) {
        }
    }

    private static String readInputStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }

        return stringBuilder.toString();
    }


    public static void main(String[] args) {
        LoginWindow loginWindow = new LoginWindow();

        loginWindow.setVisible(true);
    }
}