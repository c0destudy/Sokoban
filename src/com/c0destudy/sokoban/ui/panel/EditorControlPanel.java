package com.c0destudy.sokoban.ui.panel;

import com.c0destudy.sokoban.resource.Skin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import static com.c0destudy.sokoban.ui.helper.MakeComponent.*;

public class EditorControlPanel extends BasePanel
{
    private final ArrayList<JButton>  brushes = new ArrayList<>();
    private final JTextField          txtLevelName;
    private final JFormattedTextField txtLevelDifficulty;

    public EditorControlPanel(final ActionListener listener) {
        super(listener, 300, 530);

        // Brush
        final BrushActionListener brushListener = new BrushActionListener();
        brushes.add(makeButton("Eraser",  220, 30, true));
        brushes.add(makeButton("Wall",    220, 30, true));
        brushes.add(makeButton("Baggage", 220, 30, true));
        brushes.add(makeButton("Goal",    220, 30, true));
        brushes.add(makeButton("Trigger", 220, 30, true));
        brushes.add(makeButton("Player",  220, 30, true));
        brushes.forEach(e -> e.addActionListener(brushListener));

        // UI Components
        Arrays.asList(
            makeVSpace(20),
            makeLargeLabel("< LEVEL EDITOR >", true),
            makeVSpace(20),
            makeSmallLabel("== Level Name ==", true),
            makeVSpace(5),
            txtLevelName = makeTextBox(220, 30, true, "", SwingConstants.CENTER),
            makeVSpace(20),
            makeSmallLabel("== Difficulty ==", true),
            makeVSpace(5),
            txtLevelDifficulty = makeNumberTextBox(220, 30, true, 0, SwingConstants.CENTER),
            makeVSpace(15),
            makeSmallLabel("== Select Brush ==", true),
            makeVSpace(10)
        ).forEach(this::add);
        brushes.forEach(e -> {
            this.add(e);
            this.add(makeVSpace(10));
        });
        Arrays.asList(
            makeVSpace(10),
            makeHBox(250, 30, true, Arrays.asList(
                makeButton("Cancel", 120, 30, false),
                makeHSpace(10),
                makeButton("Save",   120, 30, false)))
        ).forEach(this::add);
    }

    public String getLevelName()       { return txtLevelName.getText(); }
    public int    getLevelDifficulty() { return Integer.parseInt(txtLevelDifficulty.getText().replaceAll(",", "")); }
    public void   setLevelName      (final String levelName) { txtLevelName.setText(levelName); }
    public void   setLevelDifficulty(final int difficulty)   { txtLevelDifficulty.setText(Integer.toString(difficulty)); }

    private class BrushActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event) {
            final JButton button = (JButton) event.getSource();
            final String  brush  = button.getText();
            brushes.forEach(e -> {
                if (brush.equals(e.getText())) {
                    e.setBackground(skin.getColor(Skin.COLORS.ButtonSelected));
                } else {
                    e.setBackground(skin.getColor(Skin.COLORS.Button));
                }
            });
        }
    }
}
