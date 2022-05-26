package br.com.fiap.bean;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.file.UploadedFile;

import br.com.fiap.dao.UserDAO;
import br.com.fiap.model.User;
import br.com.fiap.service.UploadService;

@Named
@RequestScoped
public class UserBean {
	
	private User user = new User();
	
	private UploadedFile image;
	
	@Inject
	private UserDAO dao;
	
	public String save() {
		System.out.println(user);
		
		String imagePath = UploadService.write(image, "users");
		
		user.setImagePath(imagePath);
		
		dao.create(user);
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuário cadastrado com sucesso"));
		
		return "login";
	}
	
	public List<User> getUsers() {
		return dao.listAll();
	}
	
	public String login() {
		
		User userLogged = dao.exist(user);
		if (userLogged != null) {
			FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.getSessionMap()
				.put("user", userLogged);
			
			return "my_setups";
		}
		
		mostrarMensagem();
		System.err.println("login invalido");
		return "login?faces-redirect=true";
			
	}
	
	public String logout() {
		FacesContext
			.getCurrentInstance()
			.getExternalContext()
			.getSessionMap()
			.remove("user");
		
		return "login?faces-redirect=true";
	}
	
	

	private void mostrarMensagem() {
		FacesContext
			.getCurrentInstance()
			.getExternalContext()
			.getFlash()
			.setKeepMessages(true);
		
		FacesContext
			.getCurrentInstance()
			.addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Login inválido", ""));
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UploadedFile getImage() {
		return image;
	}

	public void setImage(UploadedFile image) {
		this.image = image;
	}
}
