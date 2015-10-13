package com.zdzsoft.build.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ConfigReader {
	private File config;
	private FileInputStream fis;
	private BufferedReader reader;

	public ConfigReader(File config) throws IOException {
		this.config = config;
		fis = new FileInputStream(config);
		reader = new BufferedReader(new InputStreamReader(fis));
	}

	public String readCommand(String line) {
		int index = line.indexOf(' ');
		if (index <= 0) {
			return line;
		}
		String command = line.substring(0, index);
		return command;
	}

	public String[] readParam(String line) {
		int index = line.indexOf(' ');
		if (index <= 0) {
			return null;
		}
		String params = line.substring(index + 1).trim();
		if (params == null || params.length() == 0) {
			return null;
		}
		String[] param = split(params, " ");
		return param;
	}

	private String[] split(String string, String limit) {
		ArrayList<String> list = new ArrayList<String>();
		int index = string.indexOf(limit);
		while (index > 0) {
			String prefix = string.substring(0, index);
			list.add(prefix.trim());
			string = string.substring(index).trim();
			index = string.indexOf(limit);
		}
		if (string != null && string.length() > 0) {
			list.add(string);
		}
		return list.toArray(new String[list.size()]);
	}

	public String readLine() {
		try {
			String line = reader.readLine().trim();
			while (line.length() == 0 || line.startsWith("#") || line.startsWith("//")) {
				line = reader.readLine().trim();
			}
			return line;
		} catch (Exception e) {
			return null;
		}
	}

	public void close() {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
			}
			reader = null;
		}
		if (fis != null) {
			try {
				fis.close();
			} catch (IOException e) {
			}
			fis = null;
		}
	}

	public File getConfig() {
		return config;
	}

	public FileInputStream getFis() {
		return fis;
	}

	public BufferedReader getReader() {
		return reader;
	}
}
