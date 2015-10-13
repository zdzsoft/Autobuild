package com.zdzsoft.build.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;

import com.zdzsoft.build.AutoBuilder;
import com.zdzsoft.build.impl.command.EnvSet;
import com.zdzsoft.build.impl.command.FileAppend;
import com.zdzsoft.build.impl.command.FileImport;
import com.zdzsoft.build.impl.command.FileRemove;
import com.zdzsoft.build.impl.command.FileWriter;
import com.zdzsoft.build.impl.command.YUI;

public class DefaultAutoBuilder extends AutoBuilder {
	private static final String CLASS_TAG = "/com/qeegoo/build/AutoBuilder.class";
	private static final String BIN_TAG = "/classes";
	private static final String JAR_TAG = "file:/";
	private File home;
	private File config;
	private HashMap<String, Command> commands = new HashMap<String, Command>();
	private HashMap<String, Object> parentMap;
	private HashMap<String, Object> map = new HashMap<String, Object>();

	public DefaultAutoBuilder(String fileName) {
		init(fileName);
		regCommands();
	}

	public DefaultAutoBuilder(File configFile, DefaultAutoBuilder parent) {
		init(configFile);
		regCommands();
		if (parent != null) {
			parentMap = parent.map;
		}
	}

	public DefaultAutoBuilder init(String fileName) {
		String clsPath = AutoBuilder.class.getResource("AutoBuilder.class").getPath();
		if (clsPath.startsWith("/")) {
			clsPath = clsPath.substring(1);
		}
		if (clsPath.endsWith(CLASS_TAG)) {
			clsPath = clsPath.substring(0, clsPath.length() - CLASS_TAG.length());
		}
		if (clsPath.endsWith(BIN_TAG)) {
			clsPath = clsPath.substring(0, clsPath.length() - BIN_TAG.length());
		}
		if (clsPath.startsWith(JAR_TAG)) {
			clsPath = clsPath.substring(JAR_TAG.length());
		}
		if (clsPath.endsWith("!")) {
			clsPath = clsPath.substring(0, clsPath.length() - 1);
		}
		if (clsPath.endsWith(".jar")) {
			int index = clsPath.lastIndexOf('/');
			clsPath = clsPath.substring(0, index);
		}
		String runPath = new File(".").getAbsolutePath();
		System.out.println("Bin dir: " + clsPath + ", Run dir: " + runPath);

		File file;
		file = new File(clsPath, fileName);
		if (file.exists()) {
			home = new File(clsPath);
			config = file;
			return this;
		}
		file = new File(runPath, fileName);
		if (file.exists()) {
			home = new File(runPath);
			config = file;
			return this;
		}
		System.out.println("Cannot find build config file!");
		return null;
	}

	public DefaultAutoBuilder init(File configFile) {
		home = configFile.getParentFile();
		config = configFile;
		return this;
	}

	private void regCommands() {
		regCommand(new YUI());
		regCommand(new EnvSet());
		regCommand(new FileImport());
		regCommand(new FileAppend());
		regCommand(new FileWriter());
		regCommand(new FileRemove());
	}

	private void regCommand(Command command) {
		commands.put(command.getName(), command);
	}

	/**
	 * 获取编译主目录
	 * 
	 * @return
	 */
	public File getHome() {
		return home;
	}

	/**
	 * 获取相对路径下的文件
	 * 
	 * @param relativePath
	 * @return
	 */
	public File getRelativeFile(String relativePath) {
		File home = getHome();
		return home == null ? null : new File(home, resolve(relativePath));
	}

	/**
	 * 解析地址中的变量信息
	 * 
	 * @param path
	 * @return
	 */
	public String resolve(String path) {
		int index = path.indexOf('$');
		while (index >= 0) {
			String prefix = path.substring(0, index);
			String sufix = path.substring(index);
			int pos1 = sufix.indexOf('/');
			int pos2 = sufix.indexOf('\\');
			int pos = -1;
			if (pos1 >= 0 && pos < pos1) {
				pos = pos1;
			}
			if (pos2 >= 0 && pos < pos2) {
				pos = pos2;
			}
			String env = sufix.substring(1, pos);
			sufix = sufix.substring(pos);
			Object value = get(env);
			if (value == null) {
				value = "";
			}
			path = prefix + value + sufix;
			index = path.indexOf('$');
		}
		return path;
	}

	/**
	 * 执行编译过程
	 * 
	 * @return
	 */
	public boolean process() {
		if (config == null) {
			return false;
		}
		try {
			ConfigReader reader = new ConfigReader(config);
			String line = reader.readLine();
			while (line != null) {
				System.out.println("Run line: " + line);
				String command = reader.readCommand(line);
				Command c = commands.get(command);
				if (c == null) {
					System.out.println("Cannot parse line " + line);
					return false;
				}
				String[] param = reader.readParam(line);
				if (!c.run(this, param)) {
					System.out.println("Cannot run line " + line);
					System.out.println("Command[" + c.getName() + "] synax: " + c.getName() + " " + c.getSynax());
					return false;
				}
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 执行指定配置文件
	 * 
	 * @param config
	 * @return
	 */
	public boolean process(File config) {
		if (config == null) {
			return false;
		}
		DefaultAutoBuilder builder = new DefaultAutoBuilder(config, this);
		boolean result = builder.process();
		builder.close();
		return result;
	}

	/**
	 * 获取对象
	 * 
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		Object obj = parentMap != null ? parentMap.get(key) : null;
		obj = obj == null ? map.get(key) : obj;
		return obj;
	}

	/**
	 * 设置对象
	 * 
	 * @param key
	 * @param value
	 */
	public void set(String key, Object value) {
		Object obj = map.get(key);
		closeObject(obj);
		map.put(key, value);
	}

	/**
	 * 设置全局对象
	 * 
	 * @param key
	 * @param value
	 */
	public void gset(String key, Object value) {
		if (parentMap != null) {
			parentMap.put(key, value);
		} else {
			map.put(key, value);
		}
	}

	/**
	 * 设置对象
	 * 
	 * @param key
	 */
	public Object remove(String key) {
		Object obj = parentMap != null ? parentMap.remove(key) : null;
		obj = obj == null ? map.remove(key) : obj;
		return obj;
	}

	/**
	 * 关闭并清理资源
	 */
	public void close() {
		Iterator<Object> i = map.values().iterator();
		while (i.hasNext()) {
			Object value = i.next();
			closeObject(value);
		}
	}

	private void closeObject(Object value) {
		if (value == null) {
			return;
		}
		if (value instanceof OutputStream) {
			OutputStream os = (OutputStream) value;
			try {
				os.close();
			} catch (IOException e) {
			}
		}
		if (value instanceof InputStream) {
			InputStream is = (InputStream) value;
			try {
				is.close();
			} catch (IOException e) {
			}
		}
		if (value instanceof ConfigReader) {
			ConfigReader config = (ConfigReader) value;
			config.close();
		}
		if (value instanceof ConfigWriter) {
			ConfigWriter config = (ConfigWriter) value;
			config.close();
		}
	}
}
