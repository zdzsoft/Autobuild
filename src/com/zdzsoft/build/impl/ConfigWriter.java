package com.zdzsoft.build.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class ConfigWriter {
	private File config;
	private FileOutputStream fos;
	private PrintWriter writer;

	public ConfigWriter(File config) {
		this.config = config;
	}

	public void open() throws IOException {
		if (fos == null) {
			fos = new FileOutputStream(config);
			writer = new PrintWriter(fos);
		}
	}

	public void write(String text) throws IOException {
		open();
		writer.print(text);
	}

	public void writeLine(String line) throws IOException {
		open();
		writer.println(line);
	}

	public void close() {
		if (writer != null) {
			writer.close();
			writer = null;
		}
		if (fos != null) {
			try {
				fos.close();
			} catch (IOException e) {
			}
			fos = null;
		}
	}

	public File getConfig() {
		return config;
	}

	public FileOutputStream getFos() {
		return fos;
	}

	public PrintWriter getWriter() {
		return writer;
	}
}
