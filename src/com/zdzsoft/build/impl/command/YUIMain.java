package com.zdzsoft.build.impl.command;

import java.io.File;

import com.zdzsoft.build.AutoBuilder;
import com.zdzsoft.build.impl.Command;
import com.zdzsoft.build.impl.ConfigWriter;

public class YUIMain implements Command {

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
		String[] args = new String[3];
		args[0] = "-o";
		args[1] = outFile.getAbsolutePath();
		args[2] = file.getAbsolutePath();
		try {
			com.yahoo.platform.yui.compressor.Bootstrap.main(args);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
