package controllerUser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import model.Person;
import service.PersonService;
import service.impl.PersonServiceImpl;

@WebServlet(urlPatterns = "/admin/user/edit") 
public class EditUserController extends HttpServlet {
	PersonService personService = new PersonServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("uid");
		Person person = personService.get(Integer.parseInt(id));
		req.setAttribute("person", person);
		
		RequestDispatcher dispatcher = req.getRequestDispatcher("/view/admin/user/editUser.jsp");
		dispatcher.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			Person user = new Person();
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload servletFileUpload = new ServletFileUpload(factory);		
			List<FileItem> items = servletFileUpload.parseRequest(req);
			for (FileItem item : items) {
				if (item.getFieldName().equals("username")) {
					String uName = item.getString();
					user.setUsername(uName);
				} else if (item.getFieldName().equals("password")) {
					String uPass = item.getString();
					user.setPassword(uPass);
				} else if (item.getFieldName().equals("name")) {
					String name = item.getString();
					user.setName(name);
				} else if (item.getFieldName().equals("age")) {
					String age = item.getString();
					user.setAge(Integer.parseInt(age));
				} else if (item.getFieldName().equals("address")) {
					String address = item.getString();
					user.setAdd(address);
				} else if (item.getFieldName().equals("role")) {
					String role = item.getString();
					user.setRole(role);					
				} else if (item.getFieldName().equals("avatarFile")) {
					if(item.getSize()>0) {
						String location = "D:\\user\\";
						String fileName = System.currentTimeMillis() + ".jpg";
						File file = new File(location + fileName);
						item.write(file);				
						user.setAvatarFileName(fileName);
					}				
				} else if(item.getFieldName().equals("id")){
					String id = item.getString();
					user.setId(Integer.parseInt(id));
				}
			}
			personService.update(user);
			resp.sendRedirect(req.getContextPath() + "/admin/user/list");
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
}
