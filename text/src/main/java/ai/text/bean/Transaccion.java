package ai.text.bean;

import java.util.ArrayList;
import java.util.List;

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
		this.mensajes.add(mensaje);
	}

	public List<String> getMensajes() {
		return mensajes;
	}
	
	public String getTipoTransaccion(){
		return "";
	}
	
}
