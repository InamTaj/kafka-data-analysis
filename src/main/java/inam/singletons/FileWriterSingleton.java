package inam.singletons;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileWriterSingleton {
	private static BufferedWriter instance = null;

	private FileWriterSingleton() {}

	public static BufferedWriter getInstance(String fileName) throws IOException{
		instance = new BufferedWriter(new FileWriter(fileName, true));
		return instance;
	}
}
