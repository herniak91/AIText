package ai.text.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Transaccion {
	private String id;
	private List<String> mensajes = new ArrayList<>();
	
	public Transaccion(String id, String... messages){
		this.id = id;
		for (String message : messages) {
			mensajes.add(message);
		}
	}
	
	public void addMensaje(String mensaje){
		if(!this.mensajes.contains(mensaje))
			this.mensajes.add(mensaje);
	}

	public String getId() {
		return id;
	}

	public List<String> getMensajes() {
		return mensajes;
	}
	
	public List<String> getTipoTransaccion(){
		List<String> tiposMensajes = new ArrayList<>();
		Pattern p = Pattern.compile(".*<.*:(\\p{Alpha}+) \\p{Graph}*>(?:(<return>)?+)<(?:request|response)Header>.*");
		for (String mensaje : mensajes) {
			Matcher m = p.matcher(mensaje);
			if(m.matches()){
				tiposMensajes.add(m.group(1));
			}else{
				tiposMensajes.add("TIPO_TX_DESCONOCIDO");
			}
		}
		return tiposMensajes;
	}
	
}
