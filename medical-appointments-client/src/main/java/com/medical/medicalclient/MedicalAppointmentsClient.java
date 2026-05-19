package com.medical.medicalclient;
import com.medical.medicalclient.view.LoginFrame;
import javax.swing.SwingUtilities;
/**
 * @author Yuri German Garcia López - 252583
 */
public class MedicalAppointmentsClient {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
