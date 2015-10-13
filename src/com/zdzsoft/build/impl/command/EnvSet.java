package com.zdzsoft.build.impl.command;

import com.zdzsoft.build.AutoBuilder;
import com.zdzsoft.build.impl.Command;

public class EnvSet implements Command {

	/**
	 * 获取命令
	 * 
	 * @return
	 */
	public String getName() {
		return "set";
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
		String path = builder.resolve(param[1]);
		builder.gset(key, path);
		return true;
	}

}
