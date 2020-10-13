package com.c0destudy.sokoban.ui.frame;

import com.c0destudy.sokoban.level.Level;
import com.c0destudy.sokoban.level.LevelManager;
import com.c0destudy.sokoban.level.Record;
import com.c0destudy.sokoban.misc.Point;
import com.c0destudy.sokoban.misc.Resource;
import com.c0destudy.sokoban.skin.Skin;
import com.c0destudy.sokoban.ui.panel.GamePanel;

import javax.swing.*;
import java.awt.event.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class GameFrame extends JFrame
{
    private Skin            skin;
    private Level           level;
    private GamePanel       gamePanel = null;
    private boolean         isReplay;
    private final Timer     replayTimer = new Timer();
    private TimerTask       replayTask;
    private long            replayTime;
    private int             replayIndex;

    public GameFrame(final Skin skin, final Level level, final boolean isReplay) {
        super();
        this.skin     = skin;
        this.level    = level;
        this.isReplay = isReplay;
        setTitle("Sokoban - " + level.getName());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new TWindowAdapter());
        initUI();
        if (isReplay) {
            setTitle(getTitle() + " (replay mode)");
            level.setRecordEnabled(false);
            level.resetWithoutRecords();
            gamePanel.repaint();
            startReplay();
        }
    }

    private void initUI() {
        gamePanel = new GamePanel(skin, level);
        gamePanel.addKeyListener(new TKeyAdapter());
        getContentPane().add(gamePanel);
        setSize(gamePanel.getSize());
        pack();                      // 프레임 사이즈 맞추기
        setLocationRelativeTo(null); // 화면 중앙으로 이동
    }

    private void closeUI() {
        if (!isReplay) {
            if (!level.isCompleted()) {
                LevelManager.saveLevelToFile(level, Resource.PATH_LEVEL_PAUSE);
            } else {
                LevelManager.saveLevelToFile(level, String.format(Resource.PATH_RECORDING_FILE, level.getName(), level.getMoveCount()));
            }
        }
        FrameManager.showMainFrame();
        stopReplay();
        dispose();
    }

    private void startReplay() {
        if (replayTask != null) return;
        replayTime  = System.currentTimeMillis();
        replayIndex = 0;
        replayTask  = new TimerTask() {
            @Override
            public void run() {
                final Record record = level.getRecord(replayIndex);
                if (record == null) {
                    stopReplay();
                    return;
                }
                if (System.currentTimeMillis() - replayTime >= record.getTime()) {
                    level.movePlayerAndBaggage(record.getPlayerIndex(), record.getDirection());
                    gamePanel.repaint();
                    replayTime = System.currentTimeMillis();
                    replayIndex++;
                }
            }
        };
        replayTimer.scheduleAtFixedRate(replayTask, 0, 10);
    }

    private void stopReplay() {
        if (replayTask != null) {
            replayTask.cancel();
        }
    }

    private class TKeyAdapter extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e) {
            if (isReplay) return;

            final int keyCode = e.getKeyCode();
            switch (keyCode) {
                case KeyEvent.VK_R: // 재시작
                    level.reset();
                    gamePanel.repaint();
                    return;
                case KeyEvent.VK_ESCAPE:
                    closeUI();
                    return;
            }

            if (level.isCompleted()) { // 게임 클리어시 이동 불가
                return;
            }

            int playerIndex;
            switch (keyCode) {
                case KeyEvent.VK_U: // undo
                    level.undoMove();
                    gamePanel.repaint();
                    return;
                case KeyEvent.VK_LEFT: // Player1
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_UP:
                case KeyEvent.VK_DOWN:
                    playerIndex = 0;
                    break;
                case KeyEvent.VK_A: // Player 2
                case KeyEvent.VK_D:
                case KeyEvent.VK_W:
                case KeyEvent.VK_S:
                    playerIndex = 1;
                    break;
                default:
                    return;
            }

            Point delta = null;
            switch (keyCode) {
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    delta = new Point(-1, 0);
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    delta = new Point(1, 0);
                    break;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    delta = new Point(0, -1);
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    delta = new Point(0, 1);
                    break;
            }
            level.movePlayerAndBaggage(playerIndex, delta);
            gamePanel.repaint(); // 다시 그리기
        }
    }

    private class TWindowAdapter extends WindowAdapter
    {
        @Override
        public void windowClosing(final WindowEvent windowEvent) {
            closeUI();
        }
    }
}