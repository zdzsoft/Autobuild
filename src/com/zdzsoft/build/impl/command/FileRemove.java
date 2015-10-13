package com.zdzsoft.build.impl.command;

import java.io.File;

import com.zdzsoft.build.AutoBuilder;
import com.zdzsoft.build.impl.Command;
import com.zdzsoft.build.impl.ConfigWriter;

public class FileRemove implements Command {

	/**
	 * 获取命令
	 * 
	 * @return
	 */
	public String getName() {
		return "remove";
	}

	/**
	 * 获取格式
	 * 
	 * @return
	 */
	public String getSynax() {
		return "[name/filePath]";
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
		String key = param[0];
		Object obj = builder.get(key);
		if (obj != null && obj instanceof ConfigWriter) {
			ConfigWriter writer = (ConfigWriter) obj;
			writer.close();
			writer.getConfig().delete();
			return true;
		}
		if (obj != null && obj instanceof File) {
			File file = (File) obj;
			if (file.exists()) {
				file.delete();
			}
			return true;
		}
		if (key != null) {
			File file = builder.getRelativeFile(key);
			if (file.exists()) {
				file.delete();
			}
			return true;
		}
		return true;
	}

}
