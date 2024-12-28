import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

abstract class SmartDevice {
    private final String name;

    public SmartDevice(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract JPanel getControlPanel();
}

class Light extends SmartDevice {
    private boolean isOn;
    private int brightness;

    public Light(String name) {
        super(name);
        isOn = false;
        brightness = 50;
    }

    public JPanel getControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel(getName()));
        JButton onButton = new JButton("Turn On");
        JButton offButton = new JButton("Turn Off");
        JSlider brightnessSlider = new JSlider(0, 100, brightness);

        onButton.addActionListener(e -> isOn = true);
        offButton.addActionListener(e -> isOn = false);
        brightnessSlider.addChangeListener(e -> brightness = brightnessSlider.getValue());

        panel.add(onButton);
        panel.add(offButton);
        panel.add(brightnessSlider);
        return panel;
    }
}

class Thermostat extends SmartDevice {
    private int temperature;

    public Thermostat(String name) {
        super(name);
        temperature = 70;
    }

    public JPanel getControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel(getName()));
        JButton increaseTempButton = new JButton("Increase Temp");
        JButton decreaseTempButton = new JButton("Decrease Temp");
        JSlider temperatureSlider = new JSlider(60, 80, temperature);

        increaseTempButton.addActionListener(e -> temperature++);
        decreaseTempButton.addActionListener(e -> temperature--);
        temperatureSlider.addChangeListener(e -> temperature = temperatureSlider.getValue());

        panel.add(increaseTempButton);
        panel.add(decreaseTempButton);
        panel.add(temperatureSlider);
        return panel;
    }
}

class SecurityCamera extends SmartDevice {
    private boolean isActivated;

    public SecurityCamera(String name) {
        super(name);
        isActivated = false;
    }

    public JPanel getControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel(getName()));
        JButton activateButton = new JButton("Activate");
        JButton deactivateButton = new JButton("Deactivate");

        activateButton.addActionListener(e -> isActivated = true);
        deactivateButton.addActionListener(e -> isActivated = false);

        panel.add(activateButton);
        panel.add(deactivateButton);
        return panel;
    }
}

public class SmartHomeAutomationSystem {
    private final JFrame frame;
    private final JPanel mainPanel;
    private final List<SmartDevice> devices;
    private final List<String> scheduledTasks;
    private final List<String> definedRules;

    public SmartHomeAutomationSystem() {
        devices = new ArrayList<>();
        devices.add(new Light("Living Room Light"));
        devices.add(new Thermostat("Bedroom Thermostat"));
        devices.add(new SecurityCamera("Front Door Camera"));
        scheduledTasks = new ArrayList<>();
        definedRules = new ArrayList<>();

        frame = new JFrame("Smart Home Automation System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        frame.setJMenuBar(createMenuBar());
        mainPanel = createHouseLayoutPanel();
        frame.add(mainPanel);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SmartHomeAutomationSystem::new);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu automationMenu = new JMenu("Automation");
        JMenuItem scheduleMenuItem = new JMenuItem("Schedule Task");
        JMenuItem rulesMenuItem = new JMenuItem("Define Rules");
        JMenuItem viewSchedulesMenuItem = new JMenuItem("View Schedules");
        JMenuItem viewRulesMenuItem = new JMenuItem("View Rules");

        scheduleMenuItem.addActionListener(e -> showScheduleDialog());
        rulesMenuItem.addActionListener(e -> showRulesDialog());
        viewSchedulesMenuItem.addActionListener(e -> showScheduledTasks());
        viewRulesMenuItem.addActionListener(e -> showDefinedRules());

        automationMenu.add(scheduleMenuItem);
        automationMenu.add(rulesMenuItem);
        automationMenu.add(viewSchedulesMenuItem);
        automationMenu.add(viewRulesMenuItem);

        menuBar.add(automationMenu);

        return menuBar;
    }

    private JPanel createHouseLayoutPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawHouseLayout(g);
            }
        };
        panel.setLayout(null);

        for (SmartDevice device : devices) {
            JPanel controlPanel = device.getControlPanel();
            controlPanel.setBounds(getDeviceBounds(device));
            panel.add(controlPanel);
        }

        return panel;
    }

    private void drawHouseLayout(Graphics g) {
        // Draw the layout of the house (basic example)
        g.drawRect(50, 50, 300, 200); // Living Room
        g.drawString("Living Room", 150, 45);
        g.drawRect(400, 50, 300, 200); // Bedroom
        g.drawString("Bedroom", 550, 45);
        g.drawRect(50, 300, 300, 200); // Front Door
        g.drawString("Front Door", 150, 295);
    }

    private Rectangle getDeviceBounds(SmartDevice device) {
        // Set device panel bounds based on the device type (example positions)
        if (device instanceof Light) {
            return new Rectangle(60, 70, 280, 80); // Living Room Light
        } else if (device instanceof Thermostat) {
            return new Rectangle(410, 70, 280, 80); // Bedroom Thermostat
        } else if (device instanceof SecurityCamera) {
            return new Rectangle(60, 320, 280, 80); // Front Door Camera
        }
        return new Rectangle(0, 0, 100, 100);
    }

    private void showScheduleDialog() {
        JDialog dialog = new JDialog(frame, "Schedule Task", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(4, 2));

        JLabel selectDeviceLabel = new JLabel("Select Device:");
        JComboBox<String> deviceComboBox = new JComboBox<>();
        for (SmartDevice device : devices) {
            deviceComboBox.addItem(device.getName());
        }

        JLabel timeLabel = new JLabel("Set Time (HH:MM):");
        String[] times = {"06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00"};
        JComboBox<String> timeComboBox = new JComboBox<>(times);

        JLabel actionLabel = new JLabel("Action:");
        JTextField actionField = new JTextField();

        JButton scheduleButton = new JButton("Schedule");
        scheduleButton.addActionListener(e -> {
            String selectedDevice = Objects.requireNonNull(deviceComboBox.getSelectedItem()).toString();
            String time = Objects.requireNonNull(timeComboBox.getSelectedItem()).toString();
            String action = actionField.getText().trim();

            if (action.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Store scheduling info
                scheduledTasks.add("Task Scheduled for " + selectedDevice + " at " + time + " to " + action);
                JOptionPane.showMessageDialog(dialog, "Task Scheduled for " + selectedDevice + " at " + time + " to " + action);
                dialog.dispose();
            }
        });

        dialog.add(selectDeviceLabel);
        dialog.add(deviceComboBox);
        dialog.add(timeLabel);
        dialog.add(timeComboBox);
        dialog.add(actionLabel);
        dialog.add(actionField);
        dialog.add(new JLabel());
        dialog.add(scheduleButton);

        dialog.setVisible(true);
    }

    private void showRulesDialog() {
        JDialog dialog = new JDialog(frame, "Define Rules", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(5, 2));

        JLabel conditionDeviceLabel = new JLabel("Condition Device:");
        JComboBox<String> conditionDeviceComboBox = new JComboBox<>();
        for (SmartDevice device : devices) {
            conditionDeviceComboBox.addItem(device.getName());
        }

        JLabel conditionLabel = new JLabel("Condition:");
        JTextField conditionField = new JTextField();

        JLabel actionDeviceLabel = new JLabel("Action Device:");
        JComboBox<String> actionDeviceComboBox = new JComboBox<>();
        for (SmartDevice device : devices) {
            actionDeviceComboBox.addItem(device.getName());
        }

        JLabel actionLabel = new JLabel("Action:");
        JTextField actionField = new JTextField();

        JButton defineRuleButton = new JButton("Define Rule");
        defineRuleButton.addActionListener(e -> {
            String conditionDevice = Objects.requireNonNull(conditionDeviceComboBox.getSelectedItem()).toString();
            String condition = conditionField.getText().trim();
            String actionDevice = Objects.requireNonNull(actionDeviceComboBox.getSelectedItem()).toString();
            String action = actionField.getText().trim();

            if (condition.isEmpty() || action.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Store rule definition info
                definedRules.add("If " + conditionDevice + " " + condition + ", then " + actionDevice + " " + action);
                JOptionPane.showMessageDialog(dialog, "Rule Defined: If " + conditionDevice + " " + condition + ", then " + actionDevice + " " + action);
                dialog.dispose();
            }
        });

        dialog.add(conditionDeviceLabel);
        dialog.add(conditionDeviceComboBox);
        dialog.add(conditionLabel);
        dialog.add(conditionField);
        dialog.add(actionDeviceLabel);
        dialog.add(actionDeviceComboBox);
        dialog.add(actionLabel);
        dialog.add(actionField);
        dialog.add(new JLabel());
        dialog.add(defineRuleButton);

        dialog.setVisible(true);
    }

    private void showScheduledTasks() {
        JDialog dialog = new JDialog(frame, "Scheduled Tasks", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        for (String task : scheduledTasks) {
            textArea.append(task + "\n");
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        dialog.add(scrollPane, BorderLayout.CENTER);

        dialog.setVisible(true);
    }

    private void showDefinedRules() {
        JDialog dialog = new JDialog(frame, "Defined Rules", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        for (String rule : definedRules) {
            textArea.append(rule + "\n");
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        dialog.add(scrollPane, BorderLayout.CENTER);

        dialog.setVisible(true);
    }
}