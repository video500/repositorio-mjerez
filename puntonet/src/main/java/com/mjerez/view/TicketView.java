package com.mjerez.view;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.UserTransaction;

import com.mjerez.model.Parametro;
import com.mjerez.model.Ticket;
import com.mjerez.utils.ParametrosService;

@Named("ticketView")
@ViewScoped
public class TicketView implements Serializable {
	
	@PersistenceContext(unitName = "puntonet-persistence-unit")
	private EntityManager em;
	
    @Resource
    private UserTransaction trx;
    
    @Inject
    private ParametrosService ps;

	private static final long serialVersionUID = -372201332344963715L;

	private List<Ticket> tickets;
	private List<Ticket> ticketsFiltrados;
	
	private Ticket ticketEdicion;
	
	private Date fechaVenceTemporal;
	
	private List<Parametro> parametros;
	private List<Parametro> paramFiltros;
	private List<Parametro> paramStatus;
	private List<Parametro> paramPriority;
	private List<Parametro> paramOwner;
	private List<Parametro> paramDeal;
	
	private Parametro filtroActual;
	private Integer filtroActualId;
	
	@PostConstruct
    public void init() {
        tickets = listAll(0, 100);
        
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        String sid = request.getParameter("id");
        Long id= new Long(0);
        try {
			id=Long.parseLong(sid);
		} catch (Exception e) {
			// Sin parametros
		}
        
        if (id==null || id==0) {
        	ticketEdicion = new Ticket();
        }else {
        	ticketEdicion =  findTicket(id);
        }
        ParametrosService ps = new ParametrosService();
        
        parametros=ps.getParametros(); // Todos
        
        // Cargar parametros por categorias
        
    	paramFiltros 	= ps.getParametrosCategoria("filtros");
    	paramStatus 	= ps.getParametrosCategoria("status");
    	paramPriority 	= ps.getParametrosCategoria("priority");
    	paramOwner 		= ps.getParametrosCategoria("owner");
    	paramDeal 		= ps.getParametrosCategoria("deal");
    	
    	filtroActualId=13;
    	filtrar();
    }
	
	public void filtrar(){
		System.out.println("filtrando..."+filtroActualId);
		
		// Tomar el filtro seleccionado
//		
//		13L, "All tickets"
//		14L, "Open tickets"
//		15L, "Closed tickets"
//		16L, "Overdue tickets"
//		17L, "escalated tickets"
//		18L, "High priority tickets" 
		
		switch (filtroActualId) {
		case 13:
			ticketsFiltrados = tickets;
			break;
		case 14:
			ticketsFiltrados = filtrarTicketsPor("status",1L);		
			break;
		case 15:
			ticketsFiltrados = filtrarTicketsPor("status",2L);
			break;
		case 16:
			ticketsFiltrados = filtrarTicketsPor("status",19L);
			break;
		case 17:
			ticketsFiltrados = filtrarTicketsPor("status",3L);
			break;
		case 18:
			ticketsFiltrados = filtrarTicketsPor("priority",6L);
			break;

		default:
			break;
		}
		
	}
	
	
	 private List<Ticket> filtrarTicketsPor(String campo, Long id) {
		ArrayList<Ticket> ticketsFiltrados = new ArrayList<Ticket>();
		
		for (Ticket ticket : tickets) {
			if (campo.equals("status")){
				if (ticket.getStatusId().equals(id)) {
					ticketsFiltrados.add(ticket);
				}
			}else if(campo.equals("priority")) {
				if(ticket.getPriorityId().equals(id)) {
					ticketsFiltrados.add(ticket);
				}
			}
		}
		System.out.println("Elementos totales: "+tickets.size());
		System.out.println("Elementos filtrados: "+ticketsFiltrados.size());
		return ticketsFiltrados;
	}

	public void guardarAction() {
		ticketEdicion.setFechaIngreso(new java.sql.Date(new Date().getTime()));
		
		try {
			ticketEdicion.setFechaVence(new java.sql.Date(fechaVenceTemporal.getTime()));
		} catch (Exception e1) {
			ticketEdicion.setFechaVence(new java.sql.Date(new Date().getTime()));
			//e1.printStackTrace();
		}
		
		if (create(ticketEdicion)!=null) {
			addMessage("Registro guardado");
		}else {
			addMessage("No se pudo guardar el registro");
		}
		FacesContext context = FacesContext.getCurrentInstance();
	    try {
			context.getExternalContext().redirect("search.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	 
	public void actualizarAction() {
		ticketEdicion.setFechaIngreso(new java.sql.Date(new Date().getTime()));
		try {
			ticketEdicion.setFechaVence(new java.sql.Date(fechaVenceTemporal.getTime()));
		} catch (Exception e1) {
			ticketEdicion.setFechaVence(new java.sql.Date(new Date().getTime()));
			//e1.printStackTrace();
		}
		if (update(ticketEdicion.getId(), ticketEdicion)!=null) {
			addMessage("Registro guardado");
		}else {
			addMessage("No se pudo guardar el registro");
		}
		FacesContext context = FacesContext.getCurrentInstance();
	    try {
			context.getExternalContext().redirect("search.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	 
	public void editarAction() {
		System.out.println("Editar");
		FacesContext context = FacesContext.getCurrentInstance();
	    try {
			context.getExternalContext().redirect("edit.xhtml?id="+ticketEdicion.getId());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	 
	public void borrarAction() {
		 deleteById(ticketEdicion.getId());
		 tickets = listAll(0, 100);
		 filtrar();
	}

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	

	
	public List<Ticket> getTickets() {
		return tickets;
	}



	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public List<Ticket> getTicketsFiltrados() {
		return ticketsFiltrados;
	}

	public void setTicketsFiltrados(List<Ticket> ticketsFiltrados) {
		this.ticketsFiltrados = ticketsFiltrados;
	}

	public Ticket getTicketEdicion() {
		return ticketEdicion;
	}



	public void setTicketEdicion(Ticket ticketEdicion) {
		this.ticketEdicion = ticketEdicion;
	}

	public Date getFechaVenceTemporal() {
		return fechaVenceTemporal;
	}
	
	
	public void setFechaVenceTemporal(Date fechaVenceTemporal) {
		this.fechaVenceTemporal = fechaVenceTemporal;
	}
	
	public Parametro getFiltroActual() {
		return filtroActual;
	}


	public void setFiltroActual(Parametro filtroActual) {
		this.filtroActual = filtroActual;
	}

	public Integer getFiltroActualId() {
		return filtroActualId;
	}

	public void setFiltroActualId(Integer filtroActualId) {
		this.filtroActualId = filtroActualId;
	}

	public String getTexto(Long id) {
		try {
			return ps.getParametro(id).getTexto();
		} catch (Exception e) {
			return "";
		}
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Parametro> getParametros() {
		return parametros;
	}

	public List<Parametro> getParamFiltros() {
		return paramFiltros;
	}

	public List<Parametro> getParamStatus() {
		return paramStatus;
	}

	public List<Parametro> getParamPriority() {
		return paramPriority;
	}

	public List<Parametro> getParamOwner() {
		return paramOwner;
	}

	// Métodos de Base de datos

	public List<Parametro> getParamDeal() {
		return paramDeal;
	}


	public List<Ticket> listAll(Integer startPosition,
			Integer maxResult) {
		TypedQuery<Ticket> findAllQuery = em.createQuery(
				"SELECT DISTINCT c FROM Ticket c ORDER BY c.id",
				Ticket.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		final List<Ticket> results = findAllQuery.getResultList();
		return results;
	}
	
	public Ticket findTicket(Long id) {
		TypedQuery<Ticket> findQuery = em.createQuery(
				"SELECT DISTINCT c FROM Ticket c WHERE id=:id",
				Ticket.class);
		findQuery.setParameter("id", id);
		final Ticket result = findQuery.getSingleResult();
		return result;
	}
	
	public Ticket create(Ticket entity) {
		System.out.println("persistiendo");
		try {
			trx.begin();
			em.persist(entity);
			trx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}
	
	public Boolean deleteById(long id) {
		try {
			trx.begin();
			Ticket entity = em.find(Ticket.class, id);
			System.out.println("entity id: "+entity.getId());
			if (entity == null) {
				return false;
			}
			em.remove(entity);
			em.flush();
			trx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public Ticket update(long id, Ticket entity) {
		if (entity == null) {
			return null;
		}
		try {
			trx.begin();
		if (id != entity.getId()) {
			return null;
		}
		if (em.find(Ticket.class, id) == null) {
			return null;
		}
		
			entity = em.merge(entity);
			em.flush();
			trx.commit();
		} catch (Exception e) {
			return null;
		}

		return entity;
	}
	
}
