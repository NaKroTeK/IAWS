package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;

import models.Transport;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.libs.F.Function;
import play.libs.Json;
import play.libs.WS;
import play.mvc.*;
import play.libs.F.Promise;
import play.libs.WS.*;
import play.mvc.*;


public class Application extends Controller {

	static final String JC_DECAUX_KEY = "487c76791fcccf7073bc75e77643b3262759a068";
	static final String TISSEO_KEY = "a03561f2fd10641d96fb8188d209414d8";
	static final String NAME_API_TISSEO = "linesList";
	static final String PARAM_API_TISSEO = "format=json";
	static final String MASTER_KEY = System.getenv("MASTER_KEY");

	static final String JC_DECAUX_URL = "https://api.jcdecaux.com/vls/v1/stations?contract=toulouse&apiKey=" + JC_DECAUX_KEY;
	static final String TISSEO_URL = "http://pt.data.tisseo.fr/" + NAME_API_TISSEO + "?" + PARAM_API_TISSEO + "&key=" + TISSEO_KEY;

	static Form<Transport> transportForm = Form.form(Transport.class);
	static Form<Transport> listTransportsForm = Form.form(Transport.class);


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

	public static Result listTransports() {
		return ok(views.html.listTransports.render(Transport.all(), listTransportsForm));
	}

	public static Promise<Result> listTransports2() {
		WSRequestHolder request = WS.url(JC_DECAUX_URL);
		request.setAuth(JC_DECAUX_KEY, MASTER_KEY);
		
		final Promise<Result> resultPromise = request.get().map(
				new Function<WS.Response, Result>() {
					public Result apply(WS.Response response) {
						return ok("Hello " + response.asJson());
					}
				});

		return resultPromise;


		//return ok(views.html.listTransports.render(Transport.all(), listTransportsForm));
	}

}
