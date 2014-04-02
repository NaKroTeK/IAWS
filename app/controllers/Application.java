package controllers;

import models.Transport;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

	static Form<Transport> transportForm = Form.form(Transport.class);
	static Form<Transport> nextTransportForm = Form.form(Transport.class);


	public static Result index() {
		return redirect(routes.Application.transports());
	}

	public static Result transports() {
		return ok(views.html.index.render(Transport.all(), transportForm));
	}

	public static Result newTransport() {
		Form<Transport> filledForm = transportForm.bindFromRequest();
		if(filledForm.hasErrors()) {
			return badRequest(
					views.html.index.render(Transport.all(), filledForm)
					);
		} else {
			Transport.create(filledForm.get());
			return redirect(routes.Application.transports());  
		}
	}

	public static Result deleteTransport(Long id) {
		Transport.delete(id);
		return redirect(routes.Application.transports());
	}
	
	public static Result nextTransport() {
		return ok(views.html.nextTransport.render(Transport.all(), nextTransportForm));
	}

}
