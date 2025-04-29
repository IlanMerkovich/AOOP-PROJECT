package Game.GUI;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;

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

        Color backgroundColor = new Color(34, 34, 59);
        Color panelColor = new Color(44, 44, 79);
        Color textColor = new Color(220, 220, 240);
        Color accentColor = new Color(100, 150, 240);
        Color buttonColor = new Color(80, 120, 200);
        UIManager.put("Panel.background", panelColor);
        UIManager.put("RadioButton.background", panelColor);
        UIManager.put("Label.foreground", textColor);
        UIManager.put("RadioButton.foreground", textColor);
        UIManager.put("TextField.background", new Color(60,60,80));
        UIManager.put("TextField.foreground", textColor);
        UIManager.put("Button.background", buttonColor);
        UIManager.put("Button.foreground", Color.WHITE);

        JLabel title = new JLabel("Welcome To D&D Based Game", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setForeground(textColor);
        title.setOpaque(true);
        title.setBackground(backgroundColor);
        title.setBorder(new CompoundBorder(
                new EmptyBorder(10, 10, 10, 10),
                new LineBorder(accentColor, 2)
        ));

        rowsField = new JTextField(5);
        colsField = new JTextField(5);
        nameField = new JTextField(15);

        archerButton = createCharacterButton("Archer",ImageLoader.load("archer.png", 64,64));
        warriorButton = createCharacterButton("Warrior",ImageLoader.load("figther.png",64,64));
        mageButton = createCharacterButton("Mage",ImageLoader.load("Mage.png", 64,64));

        ButtonGroup group = new ButtonGroup();
        group.add(archerButton);
        group.add(warriorButton);
        group.add(mageButton);

        startButton = new JButton("Start Game");

        // בניית ה־layout
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(panelColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.weightx= 1;

        // Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        content.add(title, gbc);

        // Rows
        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel rowsLabel = new JLabel("Rows");
        rowsLabel.setForeground(textColor);
        content.add(rowsLabel, gbc);
        gbc.gridx = 1;
        content.add(rowsField, gbc);

        // Cols
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel colsLabel = new JLabel("Cols");
        colsLabel.setForeground(textColor);
        content.add(colsLabel, gbc);
        gbc.gridx = 1;
        content.add(colsField, gbc);

        // Name
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setForeground(textColor);
        content.add(nameLabel, gbc);
        gbc.gridx = 1;
        content.add(nameField, gbc);

        // Choose your character
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        JLabel choose = new JLabel("Choose Your Character", SwingConstants.CENTER);
        choose.setFont(choose.getFont().deriveFont(Font.PLAIN, 16f));
        choose.setForeground(textColor);
        content.add(choose, gbc);

        // Character buttons panel
        JPanel charsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        charsPanel.setBackground(panelColor);
        charsPanel.add(archerButton);
        charsPanel.add(warriorButton);
        charsPanel.add(mageButton);

        gbc.gridy = 5;
        content.add(charsPanel, gbc);

        // Start button
        gbc.gridy = 6;
        startButton.setPreferredSize(new Dimension(120, 30));
        content.add(startButton, gbc);

        // הצגה
        setContentPane(content);
        pack();
        setSize(600, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /** יוצר JRadioButton עם אייקון מעל וטקסט מתחת **/
    private JRadioButton createCharacterButton(String text, ImageIcon icon) {
        JRadioButton btn = new JRadioButton(text, icon, false);
        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.setBorder(new LineBorder(Color.GRAY, 1));
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
        if (archerButton.isSelected())  return "Archer";
        if (warriorButton.isSelected()) return "Warrior";
        if (mageButton.isSelected())    return "Mage";
        return null;
    }

    public void setStartAction(ActionListener al) {
        startButton.addActionListener(al);
    }
}
