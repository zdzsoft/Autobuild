package com.zdzsoft.build.impl.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;
import com.zdzsoft.build.AutoBuilder;
import com.zdzsoft.build.impl.Command;
import com.zdzsoft.build.impl.ConfigWriter;

public class YUI implements Command {

	/**
	 * 获取命令
	 * 
	 * @return
	 */
	public String getName() {
		return "yui";
	}

	/**
	 * 获取格式
	 * 
	 * @return
	 */
	public String getSynax() {
		return "[name]";
	}

	/**
	 * 执行命令
	 * 
	 * @param builder
	 * @param param
	 * @return
	 */
	public boolean run(AutoBuilder builder, String[] param) {
		if (param == null || param.length != 2) {
			return false;
		}
		String key = param[0];
		String dest = param[1];
		Object obj = (ConfigWriter) builder.remove(key);
		if (obj == null || !(obj instanceof ConfigWriter)) {
			return false;
		}
		File outFile = builder.getRelativeFile(dest);
		outFile.getParentFile().mkdirs();
		ConfigWriter configWriter = (ConfigWriter) obj;
		configWriter.close();
		File file = configWriter.getConfig();
		String ext = file.getName().toLowerCase();
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(file);
			fos = new FileOutputStream(outFile);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
			PrintWriter writer = new PrintWriter(fos);
			if (ext.endsWith(".css")) {
				CssCompressor compressor = new CssCompressor(reader);
				compressor.compress(writer, -1);
				writer.flush();
			} else if (ext.endsWith(".js")) {
				JavaScriptCompressor compressor = new JavaScriptCompressor(reader, new ErrorReporter() {
					public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
						if (line < 0) {
							System.err.println("/n[WARNING] " + message);
						} else {
							System.err.println("/n[WARNING] " + line + ':' + lineOffset + ':' + message);
						}
					}

					public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
						if (line < 0) {
							System.err.println("/n[ERROR] " + message);
						} else {
							System.err.println("/n[ERROR] " + line + ':' + lineOffset + ':' + message);
						}
					}

					public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
						error(message, sourceName, line, lineSource, lineOffset);
						return new EvaluatorException(message);
					}
				});
				compressor.compress(writer, -1, true, false, false, false);
				writer.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
			}
		}
		return true;
	}

}
