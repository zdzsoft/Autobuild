package com.zdzsoft.build;

public class Main {

	public static void main(String[] args) {
		if (args == null || args.length != 1) {
			System.out.println("Synax error, usage: AutoBuild [configFile]");
			System.exit(-1);
		}
		AutoBuilder builder = AutoBuilder.build(args[0]);
		builder.process();
		builder.close();
		System.exit(0);
	}

}
