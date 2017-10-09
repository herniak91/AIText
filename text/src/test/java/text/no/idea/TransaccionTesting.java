package text.no.idea;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import ai.text.bean.Transaccion;

public class TransaccionTesting {
	
	final String inBodyPattern = ".*[IO][BH]=<soapenv:[Body|Header].*";
	File wsMonitFile;
	
	@Before
	public void before(){
		wsMonitFile = new File(getClass().getClassLoader().getResource("h2h_ws_monit.log").getFile());
	}
	
	@Test
	public void agruparMensajesPorTransaccion() throws FileNotFoundException{
		Map<Integer, String> mensajes = obtenerMensajes(wsMonitFile);
		Map<String, Transaccion> transacciones = new HashMap<>();
		Pattern pattern = Pattern.compile(".*txId=([\\p{Lower}\\p{Digit}-]+),.*");
		for (Entry<Integer, String> entry : mensajes.entrySet()) {
			Integer number = entry.getKey();
			String line = entry.getValue();
			Matcher matcher = pattern.matcher(line);
			if(matcher.matches()){
				String txId = matcher.group(1);
				if(transacciones.get(txId) == null)
					transacciones.put(txId, new Transaccion(txId));
				if("8cef1f21-2127-4f90-9141-54070c71cab6".equalsIgnoreCase(txId)){
					System.out.println(line);
					Pattern p = Pattern.compile(".*<.*:(\\p{Alpha}+) \\p{Graph}*>(?:(<return>)?+)<(?:request|response)Header>.*");
					Matcher m = p.matcher(line);
					if(m.matches()){
						System.out.println("Tipo de transaccion: " + m.group(1));
					}
				}
				transacciones.get(txId).addMensaje(line);
			}else{
				throw new RuntimeException("No se encontro txId para el mensaje en linea [" + number + "]");
			}
		}
		System.out.println("Se obtuvieron [" + transacciones.size() + "] transacciones");
		
		// Se categorizan las transacciones segun la cant de mensajes que involcra cada una
		Map<Integer, List<Transaccion>> categoriaTransaccion = new HashMap<>();
		for (Entry<String, Transaccion> transaccion : transacciones.entrySet()) {
			Transaccion t = transaccion.getValue();
			Integer cantMensajes = t.getMensajes().size();
			if(categoriaTransaccion.get(cantMensajes) == null)
				categoriaTransaccion.put(cantMensajes, new ArrayList<Transaccion>());
			categoriaTransaccion.get(cantMensajes).add(t);
		}
		
		//Se lista por categoria
		for (Entry<Integer, List<Transaccion>> categoria : categoriaTransaccion.entrySet()) {
			System.out.println("Se encontraron [" + categoria.getValue().size() + "] transacciones con [" + categoria.getKey() + "] mensajes cada una");
		}
		
	}

	private Map<Integer, String> obtenerMensajes(File file) throws FileNotFoundException{
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
		System.out.println("Se guardaron [" + foundMessages.size() + "] mensajes entrantes/salientes de [" + lines + "] lineas - saveMatchesSubstringWithPattern");
		return foundMessages;
	}
}
