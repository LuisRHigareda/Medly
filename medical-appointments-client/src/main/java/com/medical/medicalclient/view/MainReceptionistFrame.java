package com.medical.medicalclient.view;
import com.medical.medicalclient.client.AppointmentClient;
import com.medical.medicalclient.client.UserClient;
import com.medical.medicalclient.dto.ReceptionistResponse;
import javax.swing.*;
import java.awt.*;
/**
 * Main dashboard view for the Receptionist role
 * Features a tabbed layout to separate appointment management from new bookings
 * @author Yuri German Garcia López - 252583
 */
public class MainReceptionistFrame extends JFrame {
    
    private final ReceptionistResponse receptionistContext;
    private final UserClient userClient;
    private final AppointmentClient appointmentClient;
    
    private JTabbedPane tabbedPane;
    private JPanel manageAppointmentsPanel;
    private JPanel newBookingPanel;

    // Interactive UI components declared globally within the class scope
    private JTextField affiliationSearchField;
    private JButton searchPatientButton;
    
    // Patient Demographic Labels
    private JLabel lblPatientName;
    private JLabel lblPatientPhone;
    private JLabel lblPatientBirth;
    private JLabel lblPatientStatus;
    
    // Appointment Table Components
    private JTable appointmentsTable;
    private javax.swing.table.DefaultTableModel tableModel;
    private JButton confirmAppointmentButton;
    private JButton cancelAppointmentButton;

    /**
     * Constructs the main dashboard frame using the authenticated staff context
     * @param receptionist The active Receptionist profile data from the login session
     */
    public MainReceptionistFrame(ReceptionistResponse receptionist) {
        this.receptionistContext = receptionist;
        this.userClient = new UserClient();
        this.appointmentClient = new AppointmentClient();

        // Configure frame core dimensions and properties
        setTitle("Medical Appointments Grid - Reception Desk: " + receptionistContext.getClinicName());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        setMinimumSize(new Dimension(800, 500));

        initComponents();
    }

    /**
     * Initializes the root tabbed layout hierarchy and appends workspace areas
     */
    private void initComponents() {
        // Create the top context banner bar containing active staff profile details
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(230, 240, 250));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel staffLabel = new JLabel("Staff Member: " + receptionistContext.getName() + " " + receptionistContext.getLastName());
        staffLabel.setFont(new Font("Arial", Font.BOLD, 13));
        
        JLabel clinicLabel = new JLabel("Location Hub: " + receptionistContext.getClinicName(), JLabel.RIGHT);
        clinicLabel.setFont(new Font("Arial", Font.ITALIC, 13));
        
        headerPanel.add(staffLabel, BorderLayout.WEST);
        headerPanel.add(clinicLabel, BorderLayout.EAST);

        // Initialize the central container navigation system
        tabbedPane = new JTabbedPane();

        // Target clean architectural construction setups for individual tab states
        manageAppointmentsPanel = createManageAppointmentsTab();
        newBookingPanel = createNewBookingTab();

        // Append workspaces into navigation slots
        tabbedPane.addTab("Manage Current Appointments", manageAppointmentsPanel);
        tabbedPane.addTab("Book New Appointment Slot", newBookingPanel);

        // Add core components into frame layout regions
        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Builds the complete layout for the current appointment tracking tab
     * Integrates a top search bar, a dynamic sidebar for patient files, and a central grid table
     * @return Prepared JPanel workspace layout block.
     */
    private JPanel createManageAppointmentsTab() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search Bar Layout
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Patient Multi-Lookup"));
        
        searchPanel.add(new JLabel("Affiliation Number:"));
        affiliationSearchField = new JTextField(15);
        searchPanel.add(affiliationSearchField);
        
        searchPatientButton = new JButton("Search Records");
        searchPatientButton.addActionListener(new PatientSearchHandler());
        searchPanel.add(searchPatientButton);
        
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // Patient Demographic Profile Card
        JPanel profileCardPanel = new JPanel();
        profileCardPanel.setLayout(new BoxLayout(profileCardPanel, BoxLayout.Y_AXIS));
        profileCardPanel.setBorder(BorderFactory.createTitledBorder("Demographic File"));
        profileCardPanel.setPreferredSize(new Dimension(240, 0));

        profileCardPanel.add(Box.createVerticalStrut(10));
        profileCardPanel.add(lblPatientName = new JLabel("Name: ---"));
        profileCardPanel.add(Box.createVerticalStrut(12));
        profileCardPanel.add(lblPatientPhone = new JLabel("Phone: ---"));
        profileCardPanel.add(Box.createVerticalStrut(12));
        profileCardPanel.add(lblPatientBirth = new JLabel("Birthday: ---"));
        profileCardPanel.add(Box.createVerticalStrut(15));
        
        JLabel statusHeading = new JLabel("Insurance Validity Status:");
        statusHeading.setFont(new Font("Arial", Font.BOLD, 11));
        profileCardPanel.add(statusHeading);
        profileCardPanel.add(Box.createVerticalStrut(5));
        
        lblPatientStatus = new JLabel(" NO PATIENT LOADED ", JLabel.CENTER);
        lblPatientStatus.setOpaque(true);
        lblPatientStatus.setBackground(Color.LIGHT_GRAY);
        lblPatientStatus.setForeground(Color.WHITE);
        lblPatientStatus.setFont(new Font("Arial", Font.BOLD, 12));
        lblPatientStatus.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        profileCardPanel.add(lblPatientStatus);

        mainPanel.add(profileCardPanel, BorderLayout.WEST);

        // Appointments Table and Action Triggers
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBorder(BorderFactory.createTitledBorder("Scheduled Appointments Grid"));

        String[] columnNames = {"Appt ID", "Doctor", "Consulting Room", "Date", "Time", "Status"};
        tableModel = new javax.swing.table.DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        appointmentsTable = new JTable(tableModel);
        appointmentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Sub-panel for operational control actions at the bottom of the grid
        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        confirmAppointmentButton = new JButton("Confirm Arrival");
        cancelAppointmentButton = new JButton("Cancel Slot");
       
        confirmAppointmentButton.setEnabled(false);
        cancelAppointmentButton.setEnabled(false);

        actionButtonPanel.add(confirmAppointmentButton);
        actionButtonPanel.add(cancelAppointmentButton);
        centerPanel.add(actionButtonPanel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    /**
     * Factory placeholder layout setup for the scheduling form sheet tab.
     */
    private JPanel createNewBookingTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(new JLabel("New appointment booking fields will go here..."));
        return panel;
    }
    
    /**
     * Inner class action handler responsible for capturing the patient lookup trigger.
     * Populates the demographic profile card and synchronization grid with mock/live records.
     */
    private class PatientSearchHandler implements java.awt.event.ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            String affiliation = affiliationSearchField.getText().trim();

            if (affiliation.isEmpty()) {
                JOptionPane.showMessageDialog(MainReceptionistFrame.this,
                        "Please enter a valid Affiliation Number to initiate lookup.",
                        "Search Constraint", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (affiliation.equalsIgnoreCase("AFF-00123")) {
                lblPatientName.setText("Name: Luis Coronado");
                lblPatientPhone.setText("Phone: 6441234567");
                lblPatientBirth.setText("Birthday: 1995-05-15");
                
                lblPatientStatus.setText("   INSURANCE ACTIVE   ");
                lblPatientStatus.setBackground(new Color(46, 139, 87)); // SeaGreen
                
                tableModel.setRowCount(0);
                
                tableModel.addRow(new Object[]{101, "Dr. Leonardo Flores", "Consultorio A-101", "2026-05-20", "09:00:00", "TO_BE_CONFIRMED"});
                tableModel.addRow(new Object[]{102, "Dr. Leonardo Flores", "Consultorio A-101", "2026-06-14", "11:30:00", "CONFIRMED"});
                
                confirmAppointmentButton.setEnabled(true);
                cancelAppointmentButton.setEnabled(true);
                
            } else {
                lblPatientName.setText("Name: Patient Not Registered");
                lblPatientPhone.setText("Phone: ---");
                lblPatientBirth.setText("Birthday: ---");
                
                lblPatientStatus.setText("   VIGENCIA INACTIVA   ");
                lblPatientStatus.setBackground(new Color(178, 34, 34)); // FireBrick (Rojo)
                
                tableModel.setRowCount(0);
                confirmAppointmentButton.setEnabled(false);
                cancelAppointmentButton.setEnabled(false);
                
                JOptionPane.showMessageDialog(MainReceptionistFrame.this,
                        "The entered affiliation record does not match an active file.",
                        "Registry Warning", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}