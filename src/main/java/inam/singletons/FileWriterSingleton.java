package inam.singletons;

import inam.utils.Utils;

import java.io.*;

public class FileWriterSingleton {
	private static BufferedWriter instance = null;

	private FileWriterSingleton() {}

	public static BufferedWriter getInstance(String fileName) throws IOException{
		instance = new BufferedWriter(new FileWriter(fileName, true));
		return instance;
	}
}
