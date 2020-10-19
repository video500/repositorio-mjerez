package com.mjerez.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Named;

import com.mjerez.model.Parametro;

@Named
@ApplicationScoped
public class ParametrosService implements Serializable{
	
	private static final long serialVersionUID = 7258670627192836702L;
	private List<Parametro> parametros;
	
	
	
	public ParametrosService() {
		super();
		crearParametros();
	}

	public void crearParametros() {
		parametros =  new ArrayList<Parametro>();
		
		parametros.add(new Parametro(1L, "Open", "status"));
		parametros.add(new Parametro(2L, "Closed", "status"));
		parametros.add(new Parametro(3L, "Scalated", "status"));
		parametros.add(new Parametro(4L, "Normal", "priority"));
		parametros.add(new Parametro(5L, "Low", "priority"));
		parametros.add(new Parametro(6L, "High", "priority"));
		parametros.add(new Parametro(7L, "Irwin Steve", "owner"));
		parametros.add(new Parametro(8L, "Peter Jones", "owner"));
		parametros.add(new Parametro(9L, "Mary Shelley", "owner"));
		parametros.add(new Parametro(10L, "Ninguno", "deal"));
		parametros.add(new Parametro(11L, "Mobile deal", "deal"));
		parametros.add(new Parametro(12L, "Otro deal", "deal"));
		parametros.add(new Parametro(13L, "All tickets", "filtros"));
		parametros.add(new Parametro(14L, "Open tickets", "filtros"));
		parametros.add(new Parametro(15L, "Closed tickets", "filtros"));
		parametros.add(new Parametro(16L, "Overdue tickets", "filtros"));
		parametros.add(new Parametro(17L, "escalated tickets", "filtros"));
		parametros.add(new Parametro(18L, "High priority tickets", "filtros"));
		parametros.add(new Parametro(19L, "Overdue", "status"));
	}
	
	public Parametro getParametro(Long id) {
		for (Parametro parametro : parametros) {
			if (parametro.getId()==id) {
				return parametro;
			}
		}
		return null;
	}

	public List<Parametro> getParametros() {
		return parametros;
	}
	
	public List<Parametro> getParametrosCategoria(String categoria) {
		List<Parametro> paramCat = new ArrayList<Parametro>();
		for (Parametro parametro : parametros) {
			if (parametro.getCategoria().equals(categoria)) {
				paramCat.add(parametro);
			}
		}
		return paramCat;
	}

}
