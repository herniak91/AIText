package ai.text.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TextLineFinder {
	
	/**
	 * Devuelve un mapa [Nro Linea, Linea] con todas aquellas lineas que califiquen segun la expresion regular pasada por parametro.
	 * @param file
	 * @param regExp
	 * @throws FileNotFoundException
	 */
	public static synchronized Map<Integer, String> findLinesInText(File file, String regExp) throws FileNotFoundException{
		Map<Integer, String> foundMessages = new HashMap<>();
		Scanner scanner = new Scanner(file);
		String currentLine = "";
		int lines = 0;
		while(scanner.hasNextLine()){
			currentLine = scanner.nextLine();
			lines++;
			if(currentLine.matches(regExp))
				foundMessages.put(lines, currentLine);
		}
		scanner.close();
		return foundMessages;
	}
}
