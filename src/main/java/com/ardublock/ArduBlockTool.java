package com.ardublock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import uecide.app.*;
import uecide.app.debug.*;
import uecide.app.editors.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.util.zip.*;
import java.util.regex.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import say.swing.*;
import org.json.simple.*;
import java.beans.*;


import uecide.app.Editor;
import uecide.plugin.Plugin;

import com.ardublock.core.Context;
import com.ardublock.ui.ArduBlockToolFrame;
import com.ardublock.ui.listener.OpenblocksFrameListener;

public class ArduBlockTool extends Plugin implements OpenblocksFrameListener
{
	static ArduBlockToolFrame openblocksFrame;

    public static HashMap<String, String> pluginInfo = null;
    public static URLClassLoader loader = null;
    public static void setLoader(URLClassLoader l) { loader = l; }
    public static void setInfo(HashMap<String, String>info) { pluginInfo = info; }
    public static String getInfo(String item) { return pluginInfo.get(item); }
    public ArduBlockTool(Editor e) { System.err.println("Started ArduBlock"); editor = e; init(); }
    public ArduBlockTool(EditorBase e) { editorTab = e; }

    public ImageIcon getFileIconOverlay(File f) {
        return null;
    }

    public void populateMenu(JMenu menu, int flags) {
        if (flags == (Plugin.MENU_TOOLS | Plugin.MENU_TOP)) {
            JMenuItem item = new JMenuItem("ArduBlocks");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            ArduBlockTool.this.run();
                        }
                    });
                }
            });
            menu.add(item);
        }
    }

    public void populateContextMenu(JPopupMenu menu, int flags, DefaultMutableTreeNode node) {
    }

    public void addToolbarButtons(JToolBar toolbar, int flags) {
    }

    public static void populatePreferences(JPanel p) {
    }

    public static String getPreferencesTitle() {
        return "ArduBlocks";
    }

    public static void savePreferences() {
    }

	
	public void init() {
        ArduBlockTool.openblocksFrame = new ArduBlockToolFrame();
        ArduBlockTool.openblocksFrame.addListener(this);
        Context context = Context.getContext();
        String arduinoVersion = this.getArduinoVersion();
        context.setInArduino(true);
        context.setArduinoVersionString(arduinoVersion);
        context.setEditor(editor);
        System.out.println("Arduino Version: " + arduinoVersion);
	}

	public void run() {
		try {
			editor.toFront();
			ArduBlockTool.openblocksFrame.setVisible(true);
			ArduBlockTool.openblocksFrame.toFront();
		} catch (Exception e) {
			
		}
	}

	public String getMenuTitle() {
		return Context.APP_NAME;
	}

	public void didSave() {
		
	}
	
	public void didLoad() {
		
	}
	
	public void didSaveAs()
	{
		
	}
	
	public void didNew()
	{
		
	}
	
	public void didGenerate(String source) {
        int tab = editor.openOrSelectFile(editor.getSketch().getMainFile());
        EditorBase eb = editor.getTab(tab);
		eb.setText(source);
//		editor.handleExport(false);
	}
	
	private String getArduinoVersion()
	{
		Context context = Context.getContext();
		File versionFile = context.getArduinoFile("lib/version.txt");
		if (versionFile.exists())
		{
			try
			{
				InputStream is = new FileInputStream(versionFile);
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String line = reader.readLine();
				reader.close();
				if (line == null)
				{
					return Context.ARDUINO_VERSION_UNKNOWN;
				}
				line = line.trim();
				if (line.length() == 0)
				{
					return Context.ARDUINO_VERSION_UNKNOWN;
				}
				return line;
				
			}
			catch (FileNotFoundException e)
			{
				return Context.ARDUINO_VERSION_UNKNOWN;
			}
			catch (UnsupportedEncodingException e)
			{
				return Context.ARDUINO_VERSION_UNKNOWN;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return Context.ARDUINO_VERSION_UNKNOWN;
			}
		}
		else
		{
			return Context.ARDUINO_VERSION_UNKNOWN;
		}
		
	}
}
