package Game.GUI;

import Game.Engine.GameController;
import Game.Engine.GameWorld;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class StartFrame extends JFrame {
    private final JTextField nameField;
    private final JButton startButton;
    private final JRadioButton archerButton;
    private final JRadioButton warriorButton;
    private final JRadioButton mageButton;
    private final JTextArea descArea;

    private static final int DEFAULT_ROWS = 10;
    private static final int DEFAULT_COLS = 10;
    private static final int ICON_SIZE = 64;

    public StartFrame() {
        super("Start Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Color backgroundColor = new Color(230, 240, 255);
        Color panelColor = new Color(245, 245, 245);
        Color textColor = new Color(30, 30, 30);
        Color accentColor = new Color(100, 180, 240);
        Color buttonColor = new Color(100, 200, 120);
        UIManager.put("Panel.background", panelColor);
        UIManager.put("Label.foreground", textColor);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", textColor);
        UIManager.put("Button.background", buttonColor);
        UIManager.put("Button.foreground", Color.WHITE);

        JLabel title = new JLabel("Welcome to Your D&D Adventure", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        title.setForeground(textColor);
        title.setOpaque(true);
        title.setBackground(backgroundColor);
        title.setBorder(new CompoundBorder(new EmptyBorder(12, 12, 12, 12), new LineBorder(accentColor, 3)));
        String instructions =
                "How to play:\n" +
                        "- Move your character by clicking a cell.\n" +
                        "- Fight enemies (Goblins, Orcs, Dragons) when you click on them.\n" +
                        "- Pick up potions to restore health or boost power.\n\n" +
                        "Characters:\n" +
                        "- You play as an Archer or Warrior or Mage.\n\n" +
                        "Enemies:\n" +
                        "- Goblin: weak but are very agile.\n" +
                        "- Orc: tougher and deals more damage.\n" +
                        "- Dragon: very strong enemy,can fight from a far.\n\n" +
                        "Potions:\n" +
                        "- Health Potion: restores HP.\n" +
                        "- Power Potion: increases your attack power once.";

        JTextArea infoArea = new JTextArea(instructions);
        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setBackground(panelColor);
        infoArea.setForeground(textColor);
        JScrollPane infoScroll = new JScrollPane(infoArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        infoScroll.setPreferredSize(new Dimension(400, 120));
        infoScroll.setBorder(BorderFactory.createLineBorder(accentColor));

        nameField = new JTextField(20);
        archerButton  = createCharacterButton("Archer",ImageLoader.load("archer.png", ICON_SIZE, ICON_SIZE));
        warriorButton = createCharacterButton("Warrior",ImageLoader.load("figther.png", ICON_SIZE, ICON_SIZE));
        mageButton = createCharacterButton("Mage",ImageLoader.load("mage.png",    ICON_SIZE, ICON_SIZE));
        ButtonGroup group = new ButtonGroup();
        group.add(archerButton);
        group.add(warriorButton);
        group.add(mageButton);

        descArea = new JTextArea();
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBackground(panelColor);
        descArea.setForeground(textColor);
        descArea.setFont(descArea.getFont().deriveFont(Font.BOLD,18f));
        descArea.setPreferredSize(new Dimension(400, 80));
        descArea.setText("Choose your class to see its abilities.");

        ActionListener updateDesc = e -> {
            if (archerButton.isSelected()) {
                descArea.setText("Archer: Ranged attacks up to 2 cells away. Higher accuracy gives higher chance to hit an enemy");
            }
            else if (warriorButton.isSelected()) {
                descArea.setText("Warrior: Melee attacks up to 1 cell away. Higher defense gives better defence against enemy attacks ");
            }
            else if (mageButton.isSelected()) {
                descArea.setText("Mage: Ranged Magic attacks, Can cast spells.Can have one of 4 elements: FIRE,ICE,ACID,LIGHTNING");
            }
        };
        archerButton.addActionListener(updateDesc);
        warriorButton.addActionListener(updateDesc);
        mageButton.addActionListener(updateDesc);

        startButton = new JButton("Start Game");
        startButton.setFont(startButton.getFont().deriveFont(Font.BOLD, 16f));
        startButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your name.", "Missing Name", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String type = getSelectedCharacter();
            if (type == null){
                JOptionPane.showMessageDialog(this, "Please select a character class.", "No Class Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }
            GameWorld world = new GameWorld(DEFAULT_ROWS, DEFAULT_COLS, name, type);
            GameController gameController=new GameController(world);
            dispose();
            SwingUtilities.invokeLater(() -> new MainFrame(world,gameController));
        });

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(panelColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx  = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        content.add(title, gbc);

        gbc.gridy = 1; gbc.gridwidth = 2;
        content.add(infoScroll, gbc);

        gbc.gridy = 2; gbc.gridwidth = 1;
        content.add(new JLabel("Your Name:"), gbc);
        gbc.gridx = 1;
        content.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JLabel chooseLabel = new JLabel("Choose Your Class", SwingConstants.CENTER);
        chooseLabel.setFont(chooseLabel.getFont().deriveFont(Font.BOLD, 18f));
        content.add(chooseLabel, gbc);

        gbc.gridy = 4;
        JPanel charsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        charsPanel.setBackground(panelColor);
        charsPanel.add(archerButton);
        charsPanel.add(warriorButton);
        charsPanel.add(mageButton);
        content.add(charsPanel, gbc);

        gbc.gridy = 5;
        content.add(descArea, gbc);

        gbc.gridy = 6; gbc.gridwidth = 2;
        startButton.setPreferredSize(new Dimension(140, 40));
        content.add(startButton, gbc);

        setContentPane(content);
        pack();
        setSize(1200, 800);
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
    private String getSelectedCharacter() {
        if (archerButton.isSelected())
            return "Archer";
        if (warriorButton.isSelected())
            return "Warrior";
        if (mageButton.isSelected())
            return "Mage";
        return null;
    }
}