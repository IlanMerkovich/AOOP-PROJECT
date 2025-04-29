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

        JLabel title = new JLabel("Start game", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setBorder(new CompoundBorder(
                new EmptyBorder(10, 10, 10, 10),
                new LineBorder(Color.BLACK, 2)
        ));

        rowsField = new JTextField(5);
        colsField = new JTextField(5);
        nameField = new JTextField(15);

        archerButton = createCharacterButton("archer",ImageLoader.load("archer.png",64,64));
        warriorButton = createCharacterButton("warrior",ImageLoader.load("figther.png",64,64));
        mageButton    = createCharacterButton("mage",ImageLoader.load("Mage.png",64,64));

        ButtonGroup group = new ButtonGroup();
        group.add(archerButton);
        group.add(warriorButton);
        group.add(mageButton);

        startButton = new JButton("start");

        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        content.add(title, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        content.add(new JLabel("rows"), gbc);
        gbc.gridx = 1;
        content.add(rowsField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        content.add(new JLabel("cols"), gbc);
        gbc.gridx = 1;
        content.add(colsField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        content.add(new JLabel("name"), gbc);
        gbc.gridx = 1;
        content.add(nameField, gbc);

        // choose your character label
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        JLabel choose = new JLabel("choose your character", SwingConstants.CENTER);
        choose.setFont(choose.getFont().deriveFont(Font.PLAIN, 16f));
        content.add(choose, gbc);

        JPanel charsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        charsPanel.add(archerButton);
        charsPanel.add(warriorButton);
        charsPanel.add(mageButton);

        gbc.gridy = 5;
        content.add(charsPanel, gbc);

        gbc.gridy = 6;
        startButton.setPreferredSize(new Dimension(120, 30));
        content.add(startButton, gbc);
        setVisible(true);
        setContentPane(content);
        pack();
        setSize(600,600);
        setLocationRelativeTo(null);
    }

    /** יוצר JRadioButton עם אייקון מעל וטקסט מתחת **/
    private JRadioButton createCharacterButton(String text,ImageIcon icon) {
        JRadioButton btn = new JRadioButton(text, icon,false);
        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.setBorder(new LineBorder(Color.BLACK, 1));
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

