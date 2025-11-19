package Game.GUI;

import Game.Builders.PlayerCharacterBuilder;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;

public class AttributeCustomizationPanel extends JPanel {
    private final JSpinner healthSpinner, powerSpinner;
    private final JLabel statusLabel;
    private int defaultHealth, defaultPower;
    private final JRadioButton boost1, boost2, boost3;

    public AttributeCustomizationPanel(String playerClass) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Customize Attributes"));
        setBackground(new Color(250, 250, 255));

        defaultHealth = 100;
        defaultPower = 10;

        SpinnerNumberModel healthModel = new SpinnerNumberModel(defaultHealth, Math.max(0, defaultHealth - 2), defaultHealth + 3, 1);
        SpinnerNumberModel powerModel = new SpinnerNumberModel(defaultPower, Math.max(0, defaultPower - 3), defaultPower + 3, 1);

        healthSpinner = new JSpinner(healthModel);
        powerSpinner = new JSpinner(powerModel);
        statusLabel = new JLabel(" Total = 0 | OK");
        statusLabel.setForeground(Color.GREEN);

        ChangeListener updateStatus = e -> updateDeltaLabel();
        healthSpinner.addChangeListener(updateStatus);
        powerSpinner.addChangeListener(updateStatus);
        updateDeltaLabel();

        boost1 = new JRadioButton("Boosted Attack");
        boost2 = new JRadioButton("Boosted Agility");
        boost3 = new JRadioButton("Boosted Shield");

        ButtonGroup boostGroup = new ButtonGroup();
        boostGroup.add(boost1);
        boostGroup.add(boost2);
        boostGroup.add(boost3);

        // Top panel for stats
        JPanel statsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        statsPanel.add(new JLabel("Health:"), gbc);
        gbc.gridx = 1;
        statsPanel.add(healthSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        statsPanel.add(new JLabel("Power:"), gbc);
        gbc.gridx = 1;
        statsPanel.add(powerSpinner, gbc);

        // Middle panel for boosts
        JPanel boostPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        boostPanel.setBorder(BorderFactory.createTitledBorder("Special Boosts"));
        boostPanel.add(boost1);
        boostPanel.add(boost2);
        boostPanel.add(boost3);

        // Bottom panel for status
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.add(statusLabel);

        add(statsPanel, BorderLayout.NORTH);
        add(boostPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
    }

    private void updateDeltaLabel() {
        int health = (Integer) healthSpinner.getValue();
        int power = (Integer) powerSpinner.getValue();
        int deltaHealth = health - defaultHealth;
        int deltaPower = power - defaultPower;
        int sum = deltaHealth + deltaPower;

        boolean valid = deltaHealth >= -2 && deltaHealth <= 3 && deltaPower >= -3 && deltaPower <= 3 && sum == 0;

        String msg = " Total = " + sum;
        if (valid) {
            msg += " | OK";
            statusLabel.setForeground(Color.GREEN.darker());
        } else {
            msg += " | ERROR";
            statusLabel.setForeground(Color.RED);
        }
        statusLabel.setText(msg);
    }

    // Main method for Builder Pattern
    public void applyAttributesTo(PlayerCharacterBuilder builder) {
        builder.setHealth((Integer) healthSpinner.getValue())
                .setPower((Integer) powerSpinner.getValue());
    }

    // Alternative methods for new Builder Pattern
    public int getHealthValue() {
        return (Integer) healthSpinner.getValue();
    }

    public int getPowerValue() {
        return (Integer) powerSpinner.getValue();
    }

    public void updateForClass(String playerClass) {
        defaultHealth = 100;
        defaultPower = 10;
        healthSpinner.setValue(defaultHealth);
        powerSpinner.setValue(defaultPower);
        updateDeltaLabel();
    }

    public ArrayList<String> getSelectedBoosts() {
        ArrayList<String> selected = new ArrayList<>();
        if (boost1.isSelected()) selected.add("attack");
        if (boost2.isSelected()) selected.add("agility");
        if (boost3.isSelected()) selected.add("shield");
        return selected;
    }

    // Additional helper method for validation
    public boolean isValidConfiguration() {
        int health = getHealthValue();
        int power = getPowerValue();
        int deltaHealth = health - defaultHealth;
        int deltaPower = power - defaultPower;
        return (deltaHealth + deltaPower == 0)
                && deltaHealth >= -2 && deltaHealth <= 3
                && deltaPower >= -3 && deltaPower <= 3;
    }
}