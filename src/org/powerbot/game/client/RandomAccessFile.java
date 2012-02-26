package org.powerbot.game.client;

import java.io.File;
import java.io.FileNotFoundException;

public class RandomAccessFile extends java.io.RandomAccessFile {
	public RandomAccessFile(File file, String mode) throws FileNotFoundException {
		super(file, mode);
	}

	public RandomAccessFile(String name, String mode) throws FileNotFoundException {
		super(name, mode);
	}
}
