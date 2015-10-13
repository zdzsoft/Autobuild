package com.zdzsoft.build.impl.command;

import java.io.File;

import com.zdzsoft.build.AutoBuilder;
import com.zdzsoft.build.impl.Command;
import com.zdzsoft.build.impl.ConfigWriter;

public class FileWriter implements Command {

	/**
	 * 获取命令
	 * 
	 * @return
	 */
	public String getName() {
		return "write";
	}

	/**
	 * 获取格式
	 * 
	 * @return
	 */
	public String getSynax() {
		return "[name] [filePath]";
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
		String path = param[1];
		File file = builder.getRelativeFile(path);
		file.getParentFile().mkdirs();
		try {
			ConfigWriter writer = new ConfigWriter(file);
			builder.set(key, writer);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
