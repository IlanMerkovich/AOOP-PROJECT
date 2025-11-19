package Game.GUI;

import Game.Builders.PlayerCharacterBuilder;
import Game.Characters.PlayerCharacter;
import Game.Decorators.AgilityDecorator;
import Game.Decorators.RageDecorator;
import Game.Decorators.ShieldDecorator;
import Game.Engine.GameController;
import Game.Engine.GameMap;
import Game.Engine.GameWorld;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StartFrame extends JFrame {
    private final JTextField nameField;
    private final JButton startButton;
    private final JRadioButton archerButton, warriorButton, mageButton;
    private final JTextArea descArea;
    private final JSpinner sizeSelector;
    private int size;
    private final AttributeCustomizationPanel customizationPanel;
    private final PlayerCharacterBuilder builder = new PlayerCharacterBuilder();

    private static final int ICON_SIZE = 48;

    public StartFrame() {
        super("Start Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Color backgroundColor = new Color(240, 240, 255);
        Color panelColor = new Color(250, 250, 255);
        Color textColor = new Color(30, 30, 30);
        Color accentColor = new Color(170, 130, 255);
        Color buttonColor = new Color(140, 200, 180);
        UIManager.put("Panel.background", panelColor);
        UIManager.put("Label.foreground", textColor);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", textColor);
        UIManager.put("Button.background", buttonColor);
        UIManager.put("Button.foreground", Color.WHITE);

        JLabel title = new JLabel("Welcome to Your D&D Adventure", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setForeground(textColor);
        title.setOpaque(true);
        title.setBackground(backgroundColor);
        title.setBorder(new CompoundBorder(new EmptyBorder(8, 8, 8, 8), new LineBorder(accentColor, 2)));

        String instructions = "How to play:\n" +
                "- Move by clicking a cell.\n" +
                "- Fight enemies: Goblins, Orcs, Dragons.\n" +
                "- Pick up potions for health or power.\n\n" +
                "Classes:\n" +
                "- Archer: high accuracy\n" +
                "- Warrior: high defense\n" +
                "- Mage: magic attacks\n";

        JTextArea infoArea = new JTextArea(instructions);
        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setBackground(panelColor);
        infoArea.setForeground(textColor);
        infoArea.setFont(infoArea.getFont().deriveFont(Font.PLAIN, 12f));
        JScrollPane infoScroll = new JScrollPane(infoArea);
        infoScroll.setPreferredSize(new Dimension(360, 80));
        infoScroll.setBorder(BorderFactory.createLineBorder(accentColor));

        nameField = new JTextField(15);

        archerButton = createCharacterButton("Archer", ImageLoader.load("archer.png", ICON_SIZE, ICON_SIZE));
        warriorButton = createCharacterButton("Warrior", ImageLoader.load("warrior.png", ICON_SIZE, ICON_SIZE));
        mageButton = createCharacterButton("Mage", ImageLoader.load("mage.png", ICON_SIZE, ICON_SIZE));
        ButtonGroup group = new ButtonGroup();
        group.add(archerButton);
        group.add(warriorButton);
        group.add(mageButton);

        descArea = new JTextArea("Choose your class to see its abilities.");
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBackground(panelColor);
        descArea.setForeground(textColor);
        descArea.setFont(descArea.getFont().deriveFont(Font.PLAIN, 14f));
        descArea.setPreferredSize(new Dimension(360, 60));

        customizationPanel = new AttributeCustomizationPanel("Archer");

        ActionListener updateDesc = e -> {
            String selected = getSelectedCharacter();
            if (selected == null) return;
            switch (selected) {
                case "Archer" -> descArea.setText("Archer: Ranged + high accuracy.");
                case "Warrior" -> descArea.setText("Warrior: Close-range + strong defense.");
                case "Mage" -> descArea.setText("Mage: Elemental magic at range.");
            }
            customizationPanel.updateForClass(selected);
        };
        archerButton.addActionListener(updateDesc);
        warriorButton.addActionListener(updateDesc);
        mageButton.addActionListener(updateDesc);

        size=10;
        SpinnerNumberModel sizeModel = new SpinnerNumberModel(size, 10, 100, 1);
        sizeSelector = new JSpinner(sizeModel);
        JComponent editor = sizeSelector.getEditor();
        JFormattedTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
        textField.setHorizontalAlignment(JTextField.CENTER);


        startButton = new JButton("Start Game");
        startButton.setFont(startButton.getFont().deriveFont(Font.PLAIN, 14f));
        startButton.setPreferredSize(new Dimension(120, 35));
        startButton.addActionListener(e -> onStartGame());

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(panelColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx  = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        content.add(title, gbc);

        gbc.gridy = 1;
        content.add(infoScroll, gbc);

        gbc.gridy = 2; gbc.gridwidth = 1;
        content.add(new JLabel("Your Name:"), gbc);
        gbc.gridx = 1;
        content.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        content.add(new JLabel("Map Size:"), gbc);
        gbc.gridx = 1;
        content.add(sizeSelector,gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        content.add(new JLabel("Choose Your Class", SwingConstants.CENTER), gbc);

        gbc.gridy = 5;
        JPanel charsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 2));
        charsPanel.setBackground(panelColor);
        charsPanel.add(archerButton);
        charsPanel.add(warriorButton);
        charsPanel.add(mageButton);
        content.add(charsPanel, gbc);

        gbc.gridy = 6;
        content.add(descArea, gbc);

        gbc.gridy = 7;
        content.add(customizationPanel, gbc);

        gbc.gridy = 8;
        content.add(startButton, gbc);

        setContentPane(content);
        pack();
        setSize(500, 580);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void onStartGame() {
        try {
            GameMap.resetInstance();
            System.out.println("Reset GameMap instance");
        } catch (Exception e) {
            System.out.println("No existing GameMap to reset");
        }

        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your name.", "Missing Name", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String type = getSelectedCharacter();
        if (type == null) {
            JOptionPane.showMessageDialog(this, "Please select a character class.", "No Class Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        builder.setName(name);
        builder.setDefaults(100, 10);
        customizationPanel.applyAttributesTo(builder);

        if (!builder.isValid()){
            JOptionPane.showMessageDialog(this,
                    "Invalid attribute distribution.\n" +
                            "Total delta must equal 0 and stay within range:\n" +
                            "- Health: ±2\n" +
                            "- Power: ±3",
                    "Invalid Attributes",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int mapSize = (int) sizeSelector.getValue();

        PlayerCharacter baseCharacter = switch (type) {
            case "Warrior" -> builder.buildWarrior();
            case "Archer" -> builder.buildArcher();
            case "Mage" -> builder.buildMage();
            default -> throw new IllegalArgumentException("Invalid class!");
        };

        ArrayList<String> boosts = customizationPanel.getSelectedBoosts();
        if (!boosts.isEmpty()) {
            System.out.println("Applying decorator: " + boosts.get(0));
            switch (boosts.get(0)) {
                case "attack" -> {
                    baseCharacter = new RageDecorator(baseCharacter);
                    System.out.println("Applied RageDecorator - +50% damage");
                }
                case "shield" -> {
                    baseCharacter = new ShieldDecorator(baseCharacter);
                    System.out.println("Applied ShieldDecorator - -5% damage taken");
                }
                case "agility" -> {
                    baseCharacter = new AgilityDecorator(baseCharacter);
                    System.out.println("Applied AgilityDecorator - +35% evasion");
                }
            }
        }

        System.out.println("Final player type: " + baseCharacter.getClass().getSimpleName());

        GameWorld world = new GameWorld(mapSize, mapSize, baseCharacter);
        GameController gameController = new GameController(world);
        dispose();
        SwingUtilities.invokeLater(() -> new MainFrame(world, gameController));
    }

    private String getSelectedCharacter() {
        if (archerButton.isSelected()) return "Archer";
        if (warriorButton.isSelected()) return "Warrior";
        if (mageButton.isSelected()) return "Mage";
        return null;
    }

    private JRadioButton createCharacterButton(String text, ImageIcon icon) {
        JRadioButton btn = new JRadioButton(text, icon, false);
        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.setFont(btn.getFont().deriveFont(Font.PLAIN, 12f));
        btn.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        btn.setPreferredSize(new Dimension(80, 100));
        return btn;
    }
}