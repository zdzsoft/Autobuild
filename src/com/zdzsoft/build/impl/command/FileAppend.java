package com.zdzsoft.build.impl.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.zdzsoft.build.AutoBuilder;
import com.zdzsoft.build.impl.Command;
import com.zdzsoft.build.impl.ConfigWriter;

public class FileAppend implements Command {

	/**
	 * 获取命令
	 * 
	 * @return
	 */
	public String getName() {
		return "append";
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
		if (!file.exists()) {
			return false;
		}
		Object obj = (ConfigWriter) builder.get(key);
		if (obj == null || !(obj instanceof ConfigWriter)) {
			return false;
		}
		ConfigWriter writer = (ConfigWriter) obj;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			int len = fis.available();
			byte[] data = new byte[len < 512 ? 512 : len];
			len = fis.read(data);
			while (len > 0) {
				String text = new String(data, 0, len);
				writer.write(text);
				len = fis.read(data);
			}
			writer.writeLine("");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
		}
		return true;
	}

}
