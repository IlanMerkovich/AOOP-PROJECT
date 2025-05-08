package Game.GUI;

import Game.Engine.GameWorld;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class StartFrame extends JFrame {
    private final JTextField rowsField;
    private final JTextField colsField;
    private final JTextField nameField;
    private final JRadioButton archerButton;
    private final JRadioButton warriorButton;
    private final JRadioButton mageButton;
    private final JButton startButton;

    public StartFrame() {
        super("Start Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //–– Bright / pastel color palette ––
        Color backgroundColor = new Color(230, 240, 255); // light sky blue
        Color panelColor = new Color(245, 245, 245); // near-white gray
        Color textColor = new Color(30, 30, 30);  // dark charcoal
        Color accentColor = new Color(100, 180, 240); // bright sky blue
        Color buttonColor = new Color(100, 200, 120); // pastel green

        // Apply to UI defaults
        UIManager.put("Panel.background", panelColor);
        UIManager.put("RadioButton.background", panelColor);
        UIManager.put("Label.foreground", textColor);
        UIManager.put("RadioButton.foreground", textColor);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", textColor);
        UIManager.put("Button.background", buttonColor);
        UIManager.put("Button.foreground", Color.WHITE);

        // Title banner
        JLabel title = new JLabel("Welcome to Your D&D Adventure", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        title.setForeground(textColor);
        title.setOpaque(true);
        title.setBackground(backgroundColor);
        title.setBorder(new CompoundBorder(
                new EmptyBorder(12, 12, 12, 12),
                new LineBorder(accentColor, 3)
        ));

        // Input fields
        rowsField = new JTextField(5);
        colsField = new JTextField(5);
        nameField = new JTextField(15);

        // Character choice buttons
        archerButton = createCharacterButton("Archer", ImageLoader.load("archer.png", 64, 64));
        warriorButton = createCharacterButton("Warrior", ImageLoader.load("figther.png", 64, 64));
        mageButton = createCharacterButton("Mage", ImageLoader.load("mage.png", 64, 64));

        ButtonGroup group = new ButtonGroup();
        group.add(archerButton);
        group.add(warriorButton);
        group.add(mageButton);

        // Start button
        startButton = new JButton("Start Game");
        startButton.setFont(startButton.getFont().deriveFont(Font.BOLD, 16f));
        startButton.addActionListener(e -> {
            try {
                int row = getRows();
                int col = getCols();
                if (row < 10 || col < 10) {
                    JOptionPane.showMessageDialog(this,
                            "Rows and Columns must each be at least 10.",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String name = nameField.getText().trim();
                String type = getSelectedCharacter();
                if (type == null) {
                    JOptionPane.showMessageDialog(this,
                            "Please select a character class.",
                            "No Character Selected",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                GameWorld gameWorld = new GameWorld(row, col, name, type);
                dispose();
                SwingUtilities.invokeLater(() -> new MainFrame(gameWorld));

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter valid integers for Rows and Columns.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Layout with GridBag
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(panelColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        content.add(title, gbc);

        // Rows label & field
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        content.add(new JLabel("Rows:"), gbc);
        gbc.gridx = 1;
        content.add(rowsField, gbc);

        // Cols label & field
        gbc.gridx = 0;
        gbc.gridy = 2;
        content.add(new JLabel("Cols:"), gbc);
        gbc.gridx = 1;
        content.add(colsField, gbc);

        // Name label & field
        gbc.gridx = 0;
        gbc.gridy = 3;
        content.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        content.add(nameField, gbc);

        // Character chooser title
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JLabel choose = new JLabel("Choose Your Class", SwingConstants.CENTER);
        choose.setFont(choose.getFont().deriveFont(Font.PLAIN, 18f));
        content.add(choose, gbc);

        // Character radio buttons
        JPanel charsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        charsPanel.setBackground(panelColor);
        charsPanel.add(archerButton);
        charsPanel.add(warriorButton);
        charsPanel.add(mageButton);
        gbc.gridy = 5;
        content.add(charsPanel, gbc);

        // Start button
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        startButton.setPreferredSize(new Dimension(140, 40));
        content.add(startButton, gbc);

        setContentPane(content);
        pack();
        setSize(600, 650);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * יוצר JRadioButton עם אייקון מעל וטקסט מתחת
     **/
    private JRadioButton createCharacterButton(String text, ImageIcon icon) {
        JRadioButton btn = new JRadioButton(text, icon, false);
        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.setFont(btn.getFont().deriveFont(Font.PLAIN, 14f));
        btn.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        btn.setPreferredSize(new Dimension(100, 120));
        return btn;
    }

    public int getRows() {
        return Integer.parseInt(rowsField.getText().trim());
    }

    public int getCols() {
        return Integer.parseInt(colsField.getText().trim());
    }

    public String getPlayerName() {
        return nameField.getText().trim();
    }

    public String getSelectedCharacter() {
        if (archerButton.isSelected())
            return "Archer";
        if (warriorButton.isSelected()) return
                "Warrior";
        if (mageButton.isSelected())
            return "Mage";
        return null;
    }
}
