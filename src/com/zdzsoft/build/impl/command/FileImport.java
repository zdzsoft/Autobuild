package com.zdzsoft.build.impl.command;

import java.io.File;

import com.zdzsoft.build.AutoBuilder;
import com.zdzsoft.build.impl.Command;

public class FileImport implements Command {

	/**
	 * 获取命令
	 * 
	 * @return
	 */
	public String getName() {
		return "import";
	}

	/**
	 * 获取格式
	 * 
	 * @return
	 */
	public String getSynax() {
		return "[filePath]";
	}

	/**
	 * 执行命令
	 * 
	 * @param builder
	 * @param param
	 * @return
	 */
	public boolean run(AutoBuilder builder, String[] param) {
		if (param == null || param.length != 1) {
			return false;
		}
		String path = param[0];
		File file = builder.getRelativeFile(path);
		if (!file.exists()) {
			if (!path.endsWith(".properties")) {
				path = path + ".properties";
			}
			file = builder.getRelativeFile(path);
		}
		if (!file.exists()) {
			return false;
		}
		return builder.process(file);
	}

}
