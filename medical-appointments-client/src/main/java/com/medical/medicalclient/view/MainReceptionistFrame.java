package com.medical.medicalclient.view;

import com.medical.medicalclient.client.AppointmentClient;
import com.medical.medicalclient.client.UserClient;
import com.medical.medicalclient.dto.AppointmentResponse;
import com.medical.medicalclient.dto.ReceptionistResponse;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
/**
 * Main dashboard view for the Receptionist role
 * Features a tabbed layout to separate appointment management from new bookings
 * Includes a graphical calendar date selector integration
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
    
    // Interactive form components
    private JTextField bookPatientAffiliationField;
    private JComboBox<String> doctorComboBox;
    private JTextField roomField;
    private JDateChooser dateChooser;
    private JComboBox<String> timeComboBox;
    private JButton submitBookingButton;

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
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 500));

        initComponents();
        loadDoctorCatalogPipeline();
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
     * @return Prepared JPanel workspace layout block
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
       
        confirmAppointmentButton.addActionListener(new ConfirmAppointmentHandler());
        cancelAppointmentButton.addActionListener(new CancelAppointmentHandler());
        
        confirmAppointmentButton.setEnabled(false);
        cancelAppointmentButton.setEnabled(false);

        actionButtonPanel.add(confirmAppointmentButton);
        actionButtonPanel.add(cancelAppointmentButton);
        centerPanel.add(actionButtonPanel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    /**
     * Builds the clean grid layout form sheet for scheduling new medical appointments
     * @return Prepared JPanel form setup component
     */
    private JPanel createNewBookingTab() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Appointment Booking Specification"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 12, 8, 12); 
        
        // Patient Affiliation Index Entry
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Patient Affiliation:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        bookPatientAffiliationField = new JTextField(15);
        formPanel.add(bookPatientAffiliationField, gbc);

        // Doctor Catalog Dropdown
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Assigned Doctor:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        String[] mockDoctors = {"-- Select Doctor --", "Dr. Leonardo Flores (Medicina General)"};
        doctorComboBox = new JComboBox<>(mockDoctors);
        formPanel.add(doctorComboBox, gbc);

        // Consulting Room Context
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Consulting Room Location:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        roomField = new JTextField("Consultorio A-101");
        roomField.setEditable(false);
        roomField.setBackground(new Color(245, 245, 245));
        formPanel.add(roomField, gbc);

        // Date Entry Binding with JDateChooser
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Target Date Select:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setDate(new Date());
        dateChooser.setPreferredSize(new Dimension(150, 22));
        formPanel.add(dateChooser, gbc);

        // Time Block Selector
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Target Shift Time:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 4;
        String[] availableTimeSlots = {"08:00:00", "08:30:00", "09:00:00", "09:30:00", "10:00:00", "10:30:00", "11:00:00"};
        timeComboBox = new JComboBox<>(availableTimeSlots);
        formPanel.add(timeComboBox, gbc);

        // Action Submission Row Button
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 12, 10, 12);
        submitBookingButton = new JButton("Schedule Appointment Block");
        submitBookingButton.addActionListener(new ScheduleAppointmentHandler());
        submitBookingButton.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(submitBookingButton, gbc);

        // Outer wrapper frame to center the form cleanly
        JPanel outerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 30));
        outerWrapper.add(formPanel);

        return outerWrapper;
    }
    
    /**
     * Inner class action handler responsible for capturing the patient lookup trigger
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

            try {
                var patient = userClient.getPatientByAffiliationNumber(affiliation);

                if (patient != null) {
                    lblPatientName.setText("Name: " + patient.getName() + " " + patient.getLastName());
                    lblPatientPhone.setText("Phone: " + patient.getPhone());
                    lblPatientBirth.setText("Birthday: " + (patient.getBirthday() != null ? patient.getBirthday().toString() : "---"));
                    
                    if (patient.getValidity() != null && patient.getValidity()) {
                        lblPatientStatus.setText("   INSURANCE ACTIVE   ");
                        lblPatientStatus.setBackground(new Color(46, 139, 87)); 
                    } else {
                        lblPatientStatus.setText("   INSURANCE INACTIVE   ");
                        lblPatientStatus.setBackground(new Color(178, 34, 34));
                    }
                    
                    tableModel.setRowCount(0);
                    
                    var appointments = appointmentClient.getAppointmentsByPatientId(patient.getId());
                    
                    if (appointments != null && !appointments.isEmpty()) {
                        for (var appt : appointments) {
                            String dateStr = (appt.getDate() != null) ? appt.getDate().toString() : "---";
                            String timeStr = (appt.getTime() != null) ? appt.getTime().toString() : "---";
                            
                            tableModel.addRow(new Object[]{
                                appt.getId(),
                                appt.getDoctorName() != null ? appt.getDoctorName() : "Doctor ID: " + appt.getDoctorId(),
                                appt.getConsultingRoomName() != null ? appt.getConsultingRoomName() : "Room ID: " + appt.getConsultingRoomId(),
                                dateStr,
                                timeStr,
                                appt.getStatus()
                            });
                        }
                    }
                    
                    confirmAppointmentButton.setEnabled(true);
                    cancelAppointmentButton.setEnabled(true);
                    
                } else {
                    resetDemographicCard();
                    JOptionPane.showMessageDialog(MainReceptionistFrame.this,
                            "No active patient profile found matching the provided affiliation key.",
                            "Registry Notice", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                resetDemographicCard();
                JOptionPane.showMessageDialog(MainReceptionistFrame.this,
                        "Network handshake failure while contacting User/Appointment Cluster.\nDetails: " + ex.getMessage(),
                        "Distributed System Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        private void resetDemographicCard() {
            lblPatientName.setText("Name: Patient Not Found");
            lblPatientPhone.setText("Phone: ---");
            lblPatientBirth.setText("Birthday: ---");
            lblPatientStatus.setText("   NO DATA LOADED   ");
            lblPatientStatus.setBackground(Color.LIGHT_GRAY);
            tableModel.setRowCount(0);
            confirmAppointmentButton.setEnabled(false);
            cancelAppointmentButton.setEnabled(false);
        }
    }
    
    /**
     * Inner class action handler managing the arrival confirmation state machine logic.
     * Routes directly to PUT /api/appointments/confirm/{id}
     */
    private class ConfirmAppointmentHandler implements java.awt.event.ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            int selectedRow = appointmentsTable.getSelectedRow();
            if (selectedRow == -1) return;

            Integer appointmentId = (Integer) tableModel.getValueAt(selectedRow, 0);
            String currentStatus = (String) tableModel.getValueAt(selectedRow, 5);

            if ("CONFIRMED".equalsIgnoreCase(currentStatus)) {
                JOptionPane.showMessageDialog(MainReceptionistFrame.this, "This slot is already CONFIRMED.", "Notice", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int confirmChoice = JOptionPane.showConfirmDialog(MainReceptionistFrame.this,
                    "Confirm patient arrival for Appointment ID: " + appointmentId + "?",
                    "PUT - Confirm Handshake", JOptionPane.YES_NO_OPTION);

            if (confirmChoice == JOptionPane.YES_OPTION) {
                try {
                    boolean success = appointmentClient.confirmAppointment(appointmentId);
                    if (success) {
                        tableModel.setValueAt("CONFIRMED", selectedRow, 5);
                        JOptionPane.showMessageDialog(MainReceptionistFrame.this, "Status shifted to CONFIRMED in database!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainReceptionistFrame.this, "Network Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Inner class action handler managing transaction slot cancellation boundaries.
     * Routes directly to PUT /api/appointments/cancel/{id}
     */
    private class CancelAppointmentHandler implements java.awt.event.ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            int selectedRow = appointmentsTable.getSelectedRow();
            if (selectedRow == -1) return;

            Integer appointmentId = (Integer) tableModel.getValueAt(selectedRow, 0);
            String currentStatus = (String) tableModel.getValueAt(selectedRow, 5);

            if ("CANCELED".equalsIgnoreCase(currentStatus)) {
                JOptionPane.showMessageDialog(MainReceptionistFrame.this, "This entry is already CANCELED.", "Notice", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int confirmChoice = JOptionPane.showConfirmDialog(MainReceptionistFrame.this,
                    "Are you sure you want to CANCEL Appointment ID: " + appointmentId + "?",
                    "PUT - Cancel Request", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirmChoice == JOptionPane.YES_OPTION) {
                try {
                    // LLAMADA HTTP PUT REAL AL SERVIDOR
                    boolean success = appointmentClient.cancelAppointment(appointmentId);
                    if (success) {
                        tableModel.setValueAt("CANCELED", selectedRow, 5);
                        JOptionPane.showMessageDialog(MainReceptionistFrame.this, "The appointment slot has been successfully revoked.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainReceptionistFrame.this, "Network Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    /**
     * Inner class action handler responsible for capturing new scheduling submissions.
     * Maps real UI selections into a clean JSON structure without hardcoded IDs.
     */
    private class ScheduleAppointmentHandler implements java.awt.event.ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            String affiliation = bookPatientAffiliationField.getText().trim();
            int selectedDoctorIndex = doctorComboBox.getSelectedIndex();
            java.util.Date selectedDate = dateChooser.getDate();
            String targetTime = (String) timeComboBox.getSelectedItem();

            if (affiliation.isEmpty() || selectedDoctorIndex == 0 || selectedDate == null) {
                JOptionPane.showMessageDialog(MainReceptionistFrame.this,
                        "Please satisfy all required fields before committing the booking.",
                        "Validation Constraint", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // 1. OBTENER EL PACIENTE REAL (Para quitar el ID quemado)
                var patient = userClient.getPatientByAffiliationNumber(affiliation);
                if (patient == null) {
                    JOptionPane.showMessageDialog(MainReceptionistFrame.this,
                            "Cannot book appointment: Patient affiliation registry not found.",
                            "Payload Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 2. OBTENER EL DOCTOR REAL (De la lista descargada del backend)
                var doctorsList = userClient.findAllDoctors();
                // Restamos 1 porque el índice 0 del combo es el mensaje "-- Select Doctor --"
                var selectedDoctor = doctorsList.get(selectedDoctorIndex - 1);

                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                String targetDate = sdf.format(selectedDate);

                int operationChoice = JOptionPane.showConfirmDialog(MainReceptionistFrame.this,
                        "Authorize routing this new appointment allocation to the backend production database?",
                        "Authorize Transaction", JOptionPane.YES_NO_OPTION);

                if (operationChoice == JOptionPane.YES_OPTION) {
                    // 3. CONSTRUCCIÓN DEL JSON REAL MEDIANTE TU DTO
                    AppointmentResponse newBooking = new AppointmentResponse();
                    
                    newBooking.setPatientId(patient.getId()); // ID Real del paciente consultado
                    newBooking.setDoctorId(selectedDoctor.getId()); // ID Real del doctor
                    newBooking.setDoctorName(selectedDoctor.getName() + " " + selectedDoctor.getLastName());
                    newBooking.setConsultingRoomId(selectedDoctor.getConsultingRoomId()); // ID Real del consultorio del doctor
                    newBooking.setConsultingRoomName(selectedDoctor.getConsultingRoom());
                    
                    newBooking.setDate(java.time.LocalDate.parse(targetDate));
                    newBooking.setTime(java.time.LocalTime.parse(targetTime));
                    newBooking.setStatus("TO_BE_CONFIRMED");

                    // Enviar POST real al microservicio
                    boolean creationSuccess = appointmentClient.bookAppointment(newBooking);

                    if (creationSuccess) {
                        JOptionPane.showMessageDialog(MainReceptionistFrame.this,
                                "Excellent! The new medical appointment was successfully provisioned into MySQL.",
                                "Booking Secured", JOptionPane.INFORMATION_MESSAGE);
                        
                        bookPatientAffiliationField.setText("");
                        doctorComboBox.setSelectedIndex(0);
                        dateChooser.setDate(new java.util.Date());
                    } else {
                        JOptionPane.showMessageDialog(MainReceptionistFrame.this,
                                "The distributed cluster rejected the insertion slot.",
                                "Transaction Failure", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(MainReceptionistFrame.this,
                        "Network payload allocation failure:\n" + ex.getMessage(),
                        "Distributed System Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Queries the UserClient microservice cluster to populate the doctor dropdown roster dynamically
     */
    private void loadDoctorCatalogPipeline() {
        try {
            var doctors = userClient.findAllDoctors();
            if (doctors != null && !doctors.isEmpty()) {
                doctorComboBox.removeAllItems();
                doctorComboBox.addItem("-- Select Doctor --");
                for (var doc : doctors) {
                    doctorComboBox.addItem("Dr. " + doc.getName() + " " + doc.getLastName() + " (" + doc.getSpecialization()+ ")");
                }
            }
        } catch (Exception e) {
            System.err.println("Fallback trigger activated: Core doctor catalogue connection unreachable. " + e.getMessage());
        }
    }
}