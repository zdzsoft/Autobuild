package com.zdzsoft.build.impl;

import com.zdzsoft.build.AutoBuilder;

public interface Command {

	/**
	 * 获取命令
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * 获取格式
	 * 
	 * @return
	 */
	public String getSynax();

	/**
	 * 执行命令
	 * 
	 * @param builder
	 * @param param
	 * @return
	 */
	public boolean run(AutoBuilder builder, String[] param);
}
