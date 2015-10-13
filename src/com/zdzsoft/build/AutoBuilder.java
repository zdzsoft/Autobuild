package com.zdzsoft.build;

import java.io.File;

import com.zdzsoft.build.impl.DefaultAutoBuilder;

public abstract class AutoBuilder {

	public static AutoBuilder build(String fileName) {
		return new DefaultAutoBuilder(fileName);
	}

	/**
	 * 获取编译主目录
	 * 
	 * @return
	 */
	public abstract File getHome();

	/**
	 * 获取相对路径下的文件
	 * 
	 * @param relativePath
	 * @return
	 */
	public abstract File getRelativeFile(String relativePath);

	/**
	 * 解析地址中的变量信息
	 * 
	 * @param path
	 * @return
	 */
	public abstract String resolve(String path);

	/**
	 * 执行编译过程
	 * 
	 * @return
	 */
	public abstract boolean process();

	/**
	 * 执行指定配置文件
	 * 
	 * @param config
	 * @return
	 */
	public abstract boolean process(File config);

	/**
	 * 获取对象
	 * 
	 * @param key
	 * @return
	 */
	public abstract Object get(String key);

	/**
	 * 设置对象
	 * 
	 * @param key
	 * @param value
	 */
	public abstract void set(String key, Object value);

	/**
	 * 设置全局对象
	 * 
	 * @param key
	 * @param value
	 */
	public abstract void gset(String key, Object value);

	/**
	 * 设置对象
	 * 
	 * @param key
	 */
	public abstract Object remove(String key);

	/**
	 * 关闭并清理资源
	 */
	public abstract void close();
}
