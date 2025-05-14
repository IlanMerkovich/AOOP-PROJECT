package Game.GUI;

import Game.Engine.GameWorld;

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
    private final JLabel abilityLabel;
    private final int iconSize = 64;

    public StartFrame() {
        super("Start Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Color backgroundColor = new Color(230, 240, 255);
        Color panelColor = new Color(245, 245, 245);
        Color textColor = new Color(30, 30, 30);
        Color accentColor = new Color(100, 180, 240);
        Color buttonColor = new Color(100, 200, 120);

        UIManager.put("Panel.background", panelColor);
        UIManager.put("RadioButton.background", panelColor);
        UIManager.put("Label.foreground", textColor);
        UIManager.put("RadioButton.foreground", textColor);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", textColor);
        UIManager.put("Button.background", buttonColor);
        UIManager.put("Button.foreground", Color.WHITE);

        JLabel title = new JLabel("Welcome to Your D&D Adventure", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        title.setForeground(textColor);
        title.setOpaque(true);
        title.setBackground(backgroundColor);
        title.setBorder(new CompoundBorder(new EmptyBorder(12, 12, 12, 12), new LineBorder(accentColor, 10)));

        rowsField = new JTextField(3);
        colsField = new JTextField(3);
        nameField = new JTextField(15);

        Dimension small = new Dimension(50, rowsField.getPreferredSize().height);
        rowsField.setPreferredSize(small);
        colsField.setPreferredSize(small);

        archerButton  = createCharacterButton("Archer",  ImageLoader.load("archer.png", iconSize, iconSize));
        warriorButton = createCharacterButton("Warrior", ImageLoader.load("figther.png", iconSize, iconSize));
        mageButton    = createCharacterButton("Mage",    ImageLoader.load("mage.png",    iconSize, iconSize));

        abilityLabel = new JLabel("Select a class to see its abilities.", SwingConstants.CENTER);
        abilityLabel.setFont(abilityLabel.getFont().deriveFont(Font.BOLD, 14f));
        abilityLabel.setBorder(new EmptyBorder(8, 8, 8, 8));

        ButtonGroup group = new ButtonGroup();
        group.add(archerButton);
        group.add(warriorButton);
        group.add(mageButton);

        ActionListener updateAbility = e -> {
            String sel = getSelectedCharacter();
            if (sel==null){
                abilityLabel.setText("Select a class to see its abilities");
            }
            else{
                if (sel.equals("Archer")){
                    abilityLabel.setText("Archer: Ranged attacks up to 2 cells away. Higher accuracy gives higher chance to hit an enemy");
                }
                else if (sel.equals("Mage")){
                    abilityLabel.setText("Mage: Ranged Magic attacks, Can cast spells.Can have one of 4 elements: FIRE,ICE,ACID,LIGHTNING");
                }
                else if (sel.equals("Warrior")){
                    abilityLabel.setText("Warrior: Melee attacks up to 1 cell away. Higher defense gives better defence against enemy attacks ");
                }
            }
        };
        archerButton.addActionListener(updateAbility);
        warriorButton.addActionListener(updateAbility);
        mageButton.addActionListener(updateAbility);

        JButton startButton = new JButton("Start Game");
        startButton.setFont(startButton.getFont().deriveFont(Font.BOLD, 16f));
        startButton.addActionListener(e -> {
            try {
                int row = getRows(), col = getCols();
                if (row < 10 || col < 10) {
                    JOptionPane.showMessageDialog(this,
                            "Rows and Columns must each be at least 10.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String name = nameField.getText().trim();
                String type = getSelectedCharacter();
                if (type == null) {
                    JOptionPane.showMessageDialog(this,
                            "Please select a character class.", "No Character Selected", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                GameWorld world = new GameWorld(row, col, name, type);
                dispose();
                SwingUtilities.invokeLater(() -> new MainFrame(world));
            }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter valid integers for Rows and Columns.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(panelColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        content.add(title, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        content.add(new JLabel("Rows:"), gbc);
        gbc.gridx = 1;
        content.add(rowsField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        content.add(new JLabel("Cols:"), gbc);
        gbc.gridx = 1;
        content.add(colsField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        content.add(new JLabel("Enter Your Name:"), gbc);
        gbc.gridx = 1;
        content.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JLabel choose = new JLabel("Choose Your Class", SwingConstants.CENTER);
        choose.setFont(choose.getFont().deriveFont(Font.BOLD, 18f));
        content.add(choose, gbc);

        JPanel charsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        charsPanel.setBackground(panelColor);
        charsPanel.add(archerButton);
        charsPanel.add(warriorButton);
        charsPanel.add(mageButton);
        gbc.gridy = 5;
        content.add(charsPanel, gbc);

        gbc.gridy = 6;
        content.add(abilityLabel, gbc);

        gbc.gridy = 7;
        startButton.setPreferredSize(new Dimension(140, 40));
        gbc.fill=GridBagConstraints.NONE;
        content.add(startButton, gbc);

        setContentPane(content);
        pack();
        setSize(900, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

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
        return Integer.parseInt(rowsField.getText().trim()); }
    public int getCols(){
        return Integer.parseInt(colsField.getText().trim()); }
    public String getSelectedCharacter() {
        if (archerButton.isSelected())  return "Archer";
        if (warriorButton.isSelected()) return "Warrior";
        if (mageButton.isSelected())    return "Mage";
        return null;
    }
}
