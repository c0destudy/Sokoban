package com.c0destudy.sokoban.ui.frame;

import com.c0destudy.sokoban.level.Level;
import com.c0destudy.sokoban.skin.Skin;

import java.awt.*;

public class FrameManager
{
    public static void showMainFrame() {
        Skin skin = new Skin();

        EventQueue.invokeLater(() -> {
            final MainFrame frame = new MainFrame(skin);
            frame.setVisible(true);
        });
    }

    public static void showGameFrame(final Level level) {
        Skin skin = new Skin();

        EventQueue.invokeLater(() -> {
            final GameFrame frame = new GameFrame(skin, level);
            frame.setVisible(true);
        });
    }
}
