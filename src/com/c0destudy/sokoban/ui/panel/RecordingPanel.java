package com.c0destudy.sokoban.ui.panel;

import com.c0destudy.sokoban.misc.Resource;
import com.c0destudy.sokoban.skin.Skin;
import com.c0destudy.sokoban.ui.frame.FrameManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static com.c0destudy.sokoban.ui.helper.MakeComponent.*;
import static com.c0destudy.sokoban.ui.helper.MakeComponent.makeButton;

public class RecordingPanel extends BasePanel
{
    public RecordingPanel(final ActionListener listener) {
        super(listener);
        initUI();
    }

    private void initUI() {
        final File                 levelDir = new File(Resource.PATH_RECORDING_ROOT);
        final String[]             files    = levelDir.list();
        final ArrayList<Component> levels   = new ArrayList<>();
        if (files != null && files.length > 0) {
            for (final String fileName : files) {
                final String levelName = fileName.substring(0, fileName.lastIndexOf("."));
                levels.add(makeButton(levelName, 400, 30, true, fontSmallButton, listener, colorButton, colorButtonBack));
                levels.add(makeVSpace(10));
            }
        } else {
            levels.add(makeButton("NO RECORDINGS", 400, 30, true, fontSmallButton, listener, colorButton, colorButtonBack));
            levels.add(makeVSpace(10));
        }

        Arrays.asList(
            makeVSpace(50),
            makeLabel("R E C O R D I N G S", true, fontTitle),
            makeVSpace(40),
            makeScroll(450, 240, true, true, levels),
            makeVSpace(20),
            makeButton("Back", 450, 45, true, fontLargeButton, listener, colorButton, colorButtonBack)
        ).forEach(this::add);
    }
}
