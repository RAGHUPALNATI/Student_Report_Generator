import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class StudentReportGeneratorGUI extends JFrame {
    // Configuration constants
    private static final int MAX_STUDENTS = 50;
    private static final int MAX_COURSES = 5;
    private static final String DATA_FILE = "student_records.txt";
    
    // Arrays to store student data
    private String[] studentNames = new String[MAX_STUDENTS];
    private String[] studentIDs = new String[MAX_STUDENTS];
    private String[][] courseNames = new String[MAX_STUDENTS][MAX_COURSES];
    private double[][] courseMarks = new double[MAX_STUDENTS][MAX_COURSES];
    private int studentCount = 0;
    
    // UI Components
    private JTabbedPane tabbedPane;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextArea reportArea;
    private JLabel statusLabel;
    
    public StudentReportGeneratorGUI() {
        // Setup main window
        super("Student Report Generator");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create UI components
        createUI();
        
        // Load data from file
        loadDataFromFile();
        
        // Display window
        setVisible(true);
    }
    
    private void createUI() {
        // Create main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(30, 50, 90));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("STUDENT REPORT GENERATOR");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Create tabbed interface
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Tab 1: Dashboard
        JPanel dashboardPanel = createDashboardPanel();
        tabbedPane.addTab("Dashboard", new ImageIcon(), dashboardPanel, "System Overview");
        
        // Tab 2: Add Student
        JPanel addStudentPanel = createAddStudentPanel();
        tabbedPane.addTab("Add Student", new ImageIcon(), addStudentPanel, "Add New Student");
        
        // Tab 3: View Students
        JPanel viewStudentsPanel = createViewStudentsPanel();
        tabbedPane.addTab("View Students", new ImageIcon(), viewStudentsPanel, "View Student Records");
        
        // Tab 4: Generate Report
        JPanel reportPanel = createReportPanel();
        tabbedPane.addTab("Generate Report", new ImageIcon(), reportPanel, "Generate Student Report");
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Status bar
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(new MatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        statusPanel.setBackground(new Color(240, 240, 240));
        
        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
        statusPanel.add(statusLabel, BorderLayout.WEST);
        
        JLabel versionLabel = new JLabel("v1.0 - Student Report Generator");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        versionLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
        versionLabel.setForeground(Color.DARK_GRAY);
        statusPanel.add(versionLabel, BorderLayout.EAST);
        
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
    }
    
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBorder(new EmptyBorder(0, 0, 30, 0));
        statsPanel.setBackground(Color.WHITE);
        
        statsPanel.add(createStatCard("Total Students", String.valueOf(studentCount), new Color(70, 130, 180)));
        statsPanel.add(createStatCard("Max Capacity", String.valueOf(MAX_STUDENTS), new Color(46, 139, 87)));
        statsPanel.add(createStatCard("Courses per Student", String.valueOf(MAX_COURSES), new Color(186, 85, 211)));
        statsPanel.add(createStatCard("Data File", DATA_FILE, new Color(220, 20, 60)));
        
        // Quick actions panel
        JPanel actionsPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        actionsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        actionsPanel.setBackground(Color.WHITE);
        
        actionsPanel.add(createActionButton("Add New Student", new Color(50, 150, 200), e -> tabbedPane.setSelectedIndex(1)));
        actionsPanel.add(createActionButton("View Students", new Color(100, 180, 100), e -> tabbedPane.setSelectedIndex(2)));
        actionsPanel.add(createActionButton("Generate Report", new Color(200, 120, 50), e -> tabbedPane.setSelectedIndex(3)));
        
        // Layout
        panel.add(statsPanel, BorderLayout.CENTER);
        panel.add(actionsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(new CompoundBorder(
            new LineBorder(color.darker(), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        card.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(color.darker());
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(color);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JButton createActionButton(String text, Color color, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(15, 10, 15, 10));
        button.addActionListener(action);
        return button;
    }
    
    private JPanel createAddStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        // Main form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        
        // Student info section
        JPanel infoPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        infoPanel.setBorder(new TitledBorder(
            new LineBorder(new Color(180, 180, 180)), 
            "Student Information", 
            TitledBorder.LEFT, 
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            new Color(70, 70, 70)
        ));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel("Student Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField nameField = new JTextField(20);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField idField = new JTextField(20);
        idField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        infoPanel.add(nameLabel);
        infoPanel.add(nameField);
        infoPanel.add(idLabel);
        infoPanel.add(idField);
        
        // Course info section
        JPanel coursePanel = new JPanel(new BorderLayout());
        coursePanel.setBorder(new TitledBorder(
            new LineBorder(new Color(180, 180, 180)), 
            "Course Information", 
            TitledBorder.LEFT, 
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            new Color(70, 70, 70)
        ));
        coursePanel.setBackground(Color.WHITE);
        
        // Course form
        JPanel courseFormPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        courseFormPanel.setBackground(Color.WHITE);
        courseFormPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel courseNameLabel = new JLabel("Course Name:");
        courseNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField courseNameField = new JTextField();
        courseNameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JLabel courseMarkLabel = new JLabel("Course Mark (0-100):");
        courseMarkLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField courseMarkField = new JTextField();
        courseMarkField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        courseFormPanel.add(courseNameLabel);
        courseFormPanel.add(courseNameField);
        courseFormPanel.add(courseMarkLabel);
        courseFormPanel.add(courseMarkField);
        
        // Courses list
        JTextArea coursesList = new JTextArea(5, 30);
        coursesList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        coursesList.setEditable(false);
        JScrollPane coursesScroll = new JScrollPane(coursesList);
        coursesScroll.setBorder(new TitledBorder("Added Courses"));
        
        // Add course button
        JButton addCourseButton = new JButton("Add Course");
        addCourseButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addCourseButton.setBackground(new Color(70, 130, 180));
        addCourseButton.setForeground(Color.WHITE);
        addCourseButton.setFocusPainted(false);
        addCourseButton.setBorder(new EmptyBorder(8, 20, 8, 20));
        
        addCourseButton.addActionListener(e -> {
            String name = courseNameField.getText().trim();
            String markStr = courseMarkField.getText().trim();
            
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Course name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                double mark = Double.parseDouble(markStr);
                if (mark < 0 || mark > 100) {
                    JOptionPane.showMessageDialog(this, "Mark must be between 0 and 100", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                coursesList.append(String.format(" - %s: %.2f\n", name, mark));
                courseNameField.setText("");
                courseMarkField.setText("");
                courseNameField.requestFocus();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid mark format. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(addCourseButton);
        
        coursePanel.add(courseFormPanel, BorderLayout.NORTH);
        coursePanel.add(buttonPanel, BorderLayout.CENTER);
        coursePanel.add(coursesScroll, BorderLayout.SOUTH);
        
        // Save button
        JButton saveButton = new JButton("Save Student Record");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        saveButton.setBackground(new Color(50, 150, 50));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorder(new EmptyBorder(12, 30, 12, 30));
        
        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String id = idField.getText().trim();
            String courses = coursesList.getText().trim();
            
            if (name.isEmpty() || id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Student name and ID are required", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (courses.isEmpty()) {
                JOptionPane.showMessageDialog(this, "At least one course is required", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (studentCount >= MAX_STUDENTS) {
                JOptionPane.showMessageDialog(this, "Maximum student capacity reached!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Add student to arrays
            studentNames[studentCount] = name;
            studentIDs[studentCount] = id;
            
            // Parse courses
            String[] courseEntries = courses.split("\n");
            int coursesAdded = Math.min(courseEntries.length, MAX_COURSES);
            
            for (int i = 0; i < coursesAdded; i++) {
                String entry = courseEntries[i].substring(2); // Remove the " - " prefix
                String[] parts = entry.split(":");
                if (parts.length == 2) {
                    courseNames[studentCount][i] = parts[0].trim();
                    try {
                        courseMarks[studentCount][i] = Double.parseDouble(parts[1].trim());
                    } catch (NumberFormatException ex) {
                        courseMarks[studentCount][i] = 0;
                    }
                }
            }
            
            studentCount++;
            
            // Update UI
            updateStudentTable();
            nameField.setText("");
            idField.setText("");
            coursesList.setText("");
            
            JOptionPane.showMessageDialog(this, "Student record added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            statusLabel.setText("Added student: " + name + " (" + id + ")");
        });
        
        // Layout
        formPanel.add(infoPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(coursePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(saveButton);
        
        panel.add(formPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    private JPanel createViewStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);
        
        // Create table model
        String[] columnNames = {"ID", "Name", "Courses", "Average", "Grade"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        // Create table
        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(30);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        studentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Center align columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < studentTable.getColumnCount(); i++) {
            studentTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(new TitledBorder("Student Records"));
        
        // Populate table
        updateStudentTable();
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        // Delete button
        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        deleteButton.setBackground(new Color(200, 60, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorder(new EmptyBorder(8, 20, 8, 20));
        
        deleteButton.addActionListener(e -> {
            int selectedRow = studentTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a student to delete", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete this student?", 
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                // Shift all subsequent students up
                for (int i = selectedRow; i < studentCount - 1; i++) {
                    studentNames[i] = studentNames[i + 1];
                    studentIDs[i] = studentIDs[i + 1];
                    courseNames[i] = courseNames[i + 1];
                    courseMarks[i] = courseMarks[i + 1];
                }
                
                // Clear last student
                studentNames[studentCount - 1] = null;
                studentIDs[studentCount - 1] = null;
                courseNames[studentCount - 1] = new String[MAX_COURSES];
                courseMarks[studentCount - 1] = new double[MAX_COURSES];
                
                studentCount--;
                updateStudentTable();
                JOptionPane.showMessageDialog(this, "Student deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                statusLabel.setText("Deleted student record");
            }
        });
        
        buttonPanel.add(deleteButton);
        
        // Layout
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void updateStudentTable() {
        // Clear table
        tableModel.setRowCount(0);
        
        // Add students to table
        for (int i = 0; i < studentCount; i++) {
            int courseCount = 0;
            double total = 0;
            
            for (int j = 0; j < MAX_COURSES; j++) {
                if (courseNames[i][j] != null && !courseNames[i][j].isEmpty()) {
                    courseCount++;
                    total += courseMarks[i][j];
                }
            }
            
            double average = courseCount > 0 ? total / courseCount : 0;
            String grade = getGrade(average);
            
            tableModel.addRow(new Object[]{
                studentIDs[i],
                studentNames[i],
                courseCount + " courses",
                String.format("%.2f%%", average),
                grade
            });
        }
    }
    
    private JPanel createReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        // Input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Generate Student Report");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JLabel instructionLabel = new JLabel("Select a student to generate their academic report:");
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JComboBox<String> studentComboBox = new JComboBox<>();
        studentComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        studentComboBox.setMaximumSize(new Dimension(400, 35));
        for (int i = 0; i < studentCount; i++) {
            studentComboBox.addItem(studentIDs[i] + " - " + studentNames[i]);
        }
        studentComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton generateButton = new JButton("Generate Report");
        generateButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        generateButton.setBackground(new Color(70, 130, 180));
        generateButton.setForeground(Color.WHITE);
        generateButton.setFocusPainted(false);
        generateButton.setBorder(new EmptyBorder(10, 30, 10, 30));
        generateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        inputPanel.add(titleLabel);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        inputPanel.add(instructionLabel);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        inputPanel.add(studentComboBox);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        inputPanel.add(generateButton);
        
        // Report area
        reportArea = new JTextArea(10, 50);
        reportArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        reportArea.setEditable(false);
        JScrollPane reportScroll = new JScrollPane(reportArea);
        reportScroll.setBorder(new TitledBorder("Academic Report"));
        reportScroll.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        // Generate action
        generateButton.addActionListener(e -> {
            int selectedIndex = studentComboBox.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(this, "No student selected", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            generateReport(selectedIndex);
            statusLabel.setText("Generated report for: " + studentNames[selectedIndex]);
        });
        
        // Layout
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(reportScroll, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void generateReport(int studentIndex) {
        StringBuilder report = new StringBuilder();
        report.append("                STUDENT REPORT\n");
        report.append("==================================================\n");
        report.append(String.format("  %-15s: %s\n", "Name", studentNames[studentIndex]));
        report.append(String.format("  %-15s: %s\n", "ID", studentIDs[studentIndex]));
        report.append("==================================================\n");
        report.append("  COURSE                          MARK      GRADE\n");
        report.append("--------------------------------------------------\n");
        
        double total = 0;
        int courseCount = 0;
        
        for (int j = 0; j < MAX_COURSES; j++) {
            if (courseNames[studentIndex][j] != null && !courseNames[studentIndex][j].isEmpty()) {
                String courseName = courseNames[studentIndex][j];
                double mark = courseMarks[studentIndex][j];
                String grade = getGrade(mark);
                
                report.append(String.format("  %-30s %6.2f%%    %s\n", courseName, mark, grade));
                total += mark;
                courseCount++;
            }
        }
        
        if (courseCount > 0) {
            double average = total / courseCount;
            String overallGrade = getGrade(average);
            
            report.append("--------------------------------------------------\n");
            report.append(String.format("  %-30s %6.2f%%    %s\n", "OVERALL AVERAGE", average, overallGrade));
        } else {
            report.append("  No course records available\n");
        }
        
        report.append("==================================================\n");
        
        reportArea.setText(report.toString());
    }
    
    private String getGrade(double mark) {
        if (mark >= 90) return "A+";
        if (mark >= 85) return "A";
        if (mark >= 80) return "A-";
        if (mark >= 75) return "B+";
        if (mark >= 70) return "B";
        if (mark >= 65) return "B-";
        if (mark >= 60) return "C+";
        if (mark >= 55) return "C";
        if (mark >= 50) return "C-";
        return "F";
    }
    
    private void saveDataToFile() {
        try (PrintWriter writer = new PrintWriter(DATA_FILE)) {
            for (int i = 0; i < studentCount; i++) {
                writer.println(studentNames[i] + "," + studentIDs[i]);
                
                for (int j = 0; j < MAX_COURSES; j++) {
                    if (courseNames[i][j] != null && !courseNames[i][j].isEmpty()) {
                        writer.println(courseNames[i][j] + "," + courseMarks[i][j]);
                    }
                }
                writer.println("--");  // Student separator
            }
            System.out.println("Data saved successfully to " + DATA_FILE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            int currentStudent = -1;
            
            while ((line = reader.readLine()) != null) {
                if (line.equals("--")) {
                    currentStudent = -1;
                    continue;
                }
                
                if (currentStudent == -1) {
                    if (studentCount >= MAX_STUDENTS) {
                        System.out.println("Maximum students reached. Some data not loaded.");
                        break;
                    }
                    currentStudent = studentCount;
                    studentCount++;
                    
                    String[] studentInfo = line.split(",", 2);
                    if (studentInfo.length < 2) {
                        System.out.println("Invalid student record: " + line);
                        continue;
                    }
                    studentNames[currentStudent] = studentInfo[0];
                    studentIDs[currentStudent] = studentInfo[1];
                } else {
                    String[] courseInfo = line.split(",", 2);
                    if (courseInfo.length < 2) {
                        System.out.println("Invalid course record: " + line);
                        continue;
                    }
                    
                    for (int j = 0; j < MAX_COURSES; j++) {
                        if (courseNames[currentStudent][j] == null) {
                            courseNames[currentStudent][j] = courseInfo[0];
                            try {
                                courseMarks[currentStudent][j] = Double.parseDouble(courseInfo[1]);
                            } catch (NumberFormatException e) {
                                courseMarks[currentStudent][j] = 0;
                                System.out.println("Invalid mark format: " + courseInfo[1]);
                            }
                            break;
                        }
                    }
                }
            }
            System.out.println("Data loaded successfully from " + DATA_FILE);
            statusLabel.setText("Loaded " + studentCount + " student records from " + DATA_FILE);
        } catch (FileNotFoundException e) {
            System.out.println("No existing data file found. Starting fresh.");
            statusLabel.setText("No data file found - starting fresh");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Main method to start the application
    public static void main(String[] args) {
        // Use SwingUtilities to ensure thread safety
        SwingUtilities.invokeLater(() -> {
            StudentReportGeneratorGUI app = new StudentReportGeneratorGUI();
            
            // Add window listener to save data on close
            app.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    int confirm = JOptionPane.showConfirmDialog(app, 
                            "Save data before exiting?", 
                            "Confirm Exit", JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        app.saveDataToFile();
                    }
                }
            });
        });
    }
}