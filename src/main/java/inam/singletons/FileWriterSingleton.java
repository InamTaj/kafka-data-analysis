package inam.singletons;

import inam.utils.Utils;

import java.io.*;

public class FileWriterSingleton {
	private static BufferedWriter instance = null;

	private FileWriterSingleton() {}

	public static BufferedWriter getInstance() throws IOException{
		instance = new BufferedWriter(new FileWriter(Utils.TRANSFORMED_DATA_FILE, true));
		return instance;
	}
}
