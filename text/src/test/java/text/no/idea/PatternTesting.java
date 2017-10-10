package text.no.idea;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ai.text.parser.TextLineFinder;

public class PatternTesting {

	File wsMonitFile;
	
	@Before
	public void before(){
		wsMonitFile = new File(getClass().getClassLoader().getResource("h2h_ws_monit.log").getFile());
	}
	
	@Test
	public void countMatchesPattern() throws FileNotFoundException{
		Scanner scanner = new Scanner(wsMonitFile);
		String currentLine = "";
		String inBodyPattern = ".*[IO][BH]=<soapenv:[Body|Header].*";
		int counter = 0, lines = 0;
		while(scanner.hasNextLine()){
			currentLine = scanner.nextLine();
			if(Pattern.matches(inBodyPattern, currentLine))
				counter++;
			lines++;
		}
		System.out.println("Se encontraron [" + counter + "] mensajes(body) entrantes de [" + lines + "] lineas - countMatchesPattern");
		Assert.assertNotEquals(0, counter);
	}
	
	@Test
	public void countMatchesSubstring() throws FileNotFoundException{
		Scanner scanner = new Scanner(wsMonitFile);
		String currentLine = "";
		String inBodyPattern = "IB=<soapenv:Body";
		int counter = 0, lines = 0;
		while(scanner.hasNextLine()){
			currentLine = scanner.nextLine();
			lines++;
			if(currentLine.contains(inBodyPattern))
				counter++;
		}
		System.out.println("Se encontraron [" + counter + "] mensajes(body) entrantes de [" + lines + "] lineas - countMatchesSubstring");
		Assert.assertNotEquals(0, counter);
	}
	
	@Test
	public void countMatchesSubstringWithPattern() throws FileNotFoundException{
		Scanner scanner = new Scanner(wsMonitFile);
		String currentLine = "";
		String inBodyPattern = ".*[IO][BH]=<soapenv:[Body|Header].*";
		int counter = 0, lines = 0;
		while(scanner.hasNextLine()){
			currentLine = scanner.nextLine();
			lines++;
			if(currentLine.matches(inBodyPattern))
				counter++;
		}
		System.out.println("Se encontraron [" + counter + "] mensajes entrantes/salientes de [" + lines + "] lineas - countMatchesSubstringWithPattern");
		Assert.assertNotEquals(0, counter);
	}
	
	@Test
	public void saveMatchesSubstringWithPattern() throws FileNotFoundException{
		Map<Integer, String> foundMessages = new HashMap<>();
		Scanner scanner = new Scanner(wsMonitFile);
		String currentLine = "";
		String inBodyPattern = ".*[IO][BH]=<soapenv:[Body|Header].*";
		int lines = 0;
		while(scanner.hasNextLine()){
			currentLine = scanner.nextLine();
			lines++;
			if(currentLine.matches(inBodyPattern))
				foundMessages.put(lines, currentLine);
		}
		System.out.println("Se guardaron [" + foundMessages.keySet().size() + "] mensajes entrantes/salientes de [" + lines + "] lineas - saveMatchesSubstringWithPattern");
		Assert.assertNotEquals(0, foundMessages.keySet().size());
	}
	
	@Test
	public void findLinesInText() throws FileNotFoundException{
		Map<Integer, String> foundMessages = TextLineFinder.findLinesInText(wsMonitFile, ".*[IO][BH]=<soapenv:[Body|Header].*");
		System.out.println("Se guardaron [" + foundMessages.keySet().size() + "] mensajes entrantes/salientes - findLinesInText");
		Assert.assertNotEquals(0, foundMessages.keySet().size());
	}

}
