package com.mjerez.model;
/**
 * Clase que simula tablas de parametros y dueños para efectos del examen.
 * 
 * @author Manuel
 *
 */
public class Parametro {

	private Long id;
	private String texto;
	private String categoria;
		
	
	public Parametro(Long id, String texto, String categoria) {
		super();
		this.id = id;
		this.texto = texto;
		this.categoria = categoria;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	
}
