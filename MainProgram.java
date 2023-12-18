import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import org.jdesktop.swingx.JXDatePicker;
import java.util.Calendar;
public class MainProgram {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new MainFrame();
            frame.setSize(600, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}


class MainFrame extends JFrame {
    private JTable table;
    private PatientTableModel model;

    public MainFrame() {

        List<Patient> patients = new ArrayList<>();

        model = new PatientTableModel(patients);

        table = new JTable(model);

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        Patient selectedPatient = patients.get(selectedRow);
                        showPatientDetailsDialog(selectedPatient);
                    }
                }
            }
        });


        JButton addButton = new JButton("Dodaj pacienta");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddPatientDialog();
            }
        });

        JButton removeButton = new JButton("Odstrani pacienta");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelectedPatient();
            }
        });

        JButton detailsButton = new JButton("Podrobnosti pacienta");
        detailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    Patient selectedPatient = patients.get(selectedRow);
                    showPatientDetailsDialog(selectedPatient);
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(detailsButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);


        setTitle("Podrobnosti pacienta");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void showPatientDetailsDialog(Patient patient) {
        JDialog dialog = new JDialog(this, "Podrobnosti pacienta", true);
        dialog.setLayout(new GridLayout(3, 2));

        dialog.add(new JLabel("Višina(cm):"));
        dialog.add(new JLabel(String.valueOf(patient.getHeight())));

        dialog.add(new JLabel("Teža (kg):"));
        dialog.add(new JLabel(String.valueOf(patient.getWeight())));

        JButton closeButton = new JButton("Zapri");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        dialog.add(closeButton);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showAddPatientDialog() {
        AddPatientDialog addPatientDialog = new AddPatientDialog(this,true);
        addPatientDialog.setVisible(true);

        if (addPatientDialog.isPatientAdded()) {
            // If a new patient is added, update the table model
            Patient newPatient = addPatientDialog.getNewPatient();
            model.addPatient(newPatient);
        }
    }

    private void removeSelectedPatient() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            model.removePatient(selectedRow);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame();
            }
        });
    }
}


class AddPatientDialog extends JDialog {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField genderField;
    private JTextField dobField;
    private JTextField bloodTypeField;
    private JTextField heightField;
    private JTextField weightField;
    private JTextField allergiesField;
    private JButton addButton;
    private JButton cancelButton;
    private JXDatePicker dobPicker;
    private boolean patientAdded;
    private Patient newPatient;

    public AddPatientDialog(Frame owner, boolean modal) {
        super(owner, modal);
        initializeComponents();
        setLocationRelativeTo(owner);
        setSize(300, 300);
          
    }

    private void initializeComponents() {
    setTitle("Dodaj pacienta");

    firstNameField = new JTextField();
    lastNameField = new JTextField();
    genderField = new JTextField();
    dobPicker = new JXDatePicker();
    bloodTypeField = new JTextField();
    heightField = new JTextField();
    weightField = new JTextField();
    allergiesField = new JTextField();

    addButton = new JButton("Add");
    addButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (validateInput()) {
                newPatient = createPatient();
                patientAdded = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(AddPatientDialog.this, "Neveljavni vnos prosim vpiši vse podatke.");
            }
        }
    });

    cancelButton = new JButton("Prekliči");
    cancelButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    });

    setLayout(new GridLayout(9, 2));
    add(new JLabel("Prvo ime:"));
    add(firstNameField);
    add(new JLabel("Zadnje ime:"));
    add(lastNameField);
    add(new JLabel("Spol:"));
    add(genderField);
    add(new JLabel("Datum rojstva (dd/MM/yyyy):"));
    add(dobPicker);
    add(new JLabel("Krvna vrsta:"));
    add(bloodTypeField);
    add(new JLabel("Višina (cm):"));
    add(heightField);
    add(new JLabel("Teža (kg):"));
    add(weightField);
    add(new JLabel("Alergije:"));
    add(allergiesField);
    add(addButton);
    add(cancelButton);
}

    public boolean isPatientAdded() {
        return patientAdded;
    }

    public Patient getNewPatient() {
        return newPatient;
    }

    private boolean validateInput() {
        // You can add more sophisticated validation logic here
        return !firstNameField.getText().isEmpty()
                && !lastNameField.getText().isEmpty()
                && !genderField.getText().isEmpty()
                && !dobField.getText().isEmpty()
                && !bloodTypeField.getText().isEmpty()
                && !heightField.getText().isEmpty()
                && !weightField.getText().isEmpty();
    }

    private Patient createPatient() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String gender = genderField.getText();
        String dob = dobField.getText();
        String bloodType = bloodTypeField.getText();
        double height = Double.parseDouble(heightField.getText());
        double weight = Double.parseDouble(weightField.getText());
        String allergies = allergiesField.getText();

        return new Patient(firstName, lastName, gender, dob, bloodType, allergies, height, weight);
    }
}
class PatientTableModel extends AbstractTableModel {
    private static final int COLUMN_COUNT = 9;

    private List<Patient> patients;

    public PatientTableModel(List<Patient> patients) {
        this.patients = patients;
    }

    @Override
    public int getRowCount() {
        return patients.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Patient patient = patients.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return patient.getFirstName();
            case 1:
                return patient.getLastName();
            case 2:
                return patient.getGender();
            case 3:
                return patient.getDateOfBirth();
            case 4:
                return patient.getBloodType();
            case 5:
                return patient.getHeight();
            case 6:
                return patient.getWeight();
            case 7:
                return patient.getAllergies();
            case 8:
                return patient.getAge();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Prvo Ime";
            case 1:
                return "Zadnje ime";
            case 2:
                return "Spol";
            case 3:
                return "Datum rojstva";
            case 4:
                return "Krvna vrsta";
            case 5:
                return "Višina";
            case 6:
                return "Teža";
            case 7:
                return "Alergije";
            case 8:
                return "Starost";
            default:
                return null;
        }
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
        fireTableRowsInserted(patients.size() - 1, patients.size() - 1);
    }

    public void removePatient(int rowIndex) {
        patients.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}

class Patient {
    private String firstName;
    private String lastName;
    private String gender;
    private String dateOfBirth;
    private String bloodType;
    private String allergies;
    private double height; // in centimeters
    private double weight; // in kilograms

    public Patient(String firstName, String lastName, String gender, String dateOfBirth, String bloodType, String allergies, double height, double weight) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.bloodType = bloodType;
        this.allergies = allergies;
        this.height = height;
        this.weight = weight;
    }

    
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getAllergies() {
        return allergies;
    }

    // Setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }
    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }


    public int getAge() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd,MM,yyyy");
        try {
            Date dob = sdf.parse(dateOfBirth);
            Calendar dobCal = Calendar.getInstance();
            dobCal.setTime(dob);

            Calendar currentCal = Calendar.getInstance();

            int age = currentCal.get(Calendar.YEAR) - dobCal.get(Calendar.YEAR);

            // Adjust age if the birthdate has not occurred yet this year
            if (currentCal.get(Calendar.DAY_OF_YEAR) < dobCal.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }

            return age;
        } catch (ParseException e) {
            e.printStackTrace(); 
            return -1; 
        }
    }
}
