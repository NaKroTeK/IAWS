package controllers;

import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
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

	static final String JC_DECAUX_URL = "https://api.jcdecaux.com/vls/v1/stations?";
	static final String TISSEO_URL = "http://pt.data.tisseo.fr/" + NAME_API_TISSEO + "?";

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

	public static Result defaultTransports() {
//		return ok(views.html.listTransports.render(Transport.all(), transportForm));
		return TODO;
	}

	public static Promise<Result> listTransports() {
		WSRequestHolder request = WS.url(TISSEO_URL);
		request.setQueryParameter("format", "json");
		request.setQueryParameter("key", TISSEO_KEY);
		
		final Promise<Result> resultPromise = request.get().map(
				new Function<WS.Response, Result>() {
					public Result apply(WS.Response response) {
						
//						return ok(response.asJson());
						
						return ok(views.html.listTransports.render(response.asJson().findValuesAsText("shortName"), transportForm));
					}
				});
		
		return resultPromise;
	}
	
	public static Promise<Result> prochainPassage(String nom) {
		WSRequestHolder request = WS.url(TISSEO_URL);
		request.setQueryParameter("format", "json");
		request.setQueryParameter("key", TISSEO_KEY);
		
		final Promise<Result> resultPromise = request.get().map(
				new Function<WS.Response, Result>() {
					public Result apply(WS.Response response) {
						
//						return ok(response.asJson());
						
						return ok(views.html.listTransports.render(response.asJson().findValuesAsText("shortName"), transportForm));
					}
				});
		
		return resultPromise;
	}
	
	public static Promise<Result> listStationsVelos() {
		WSRequestHolder request = WS.url(JC_DECAUX_URL);
		request.setQueryParameter("contract", "toulouse");
		request.setQueryParameter("apiKey", JC_DECAUX_KEY);
		
		final Promise<Result> resultPromise = request.get().map(
				new Function<WS.Response, Result>() {
					public Result apply(WS.Response response) {
						List<String> listVelo = response.asJson().findValuesAsText("name");
						
						return ok(views.html.listStationsVelos.render(listVelo, transportForm));
					}
				});

		return resultPromise;
	}
	
	public static Promise<Result> veloDisponible(final String nom) {
		WSRequestHolder request = WS.url(JC_DECAUX_URL);
		request.setQueryParameter("contract", "toulouse");
		request.setQueryParameter("apiKey", JC_DECAUX_KEY);
		
		final Promise<Result> resultPromise = request.get().map(
				new Function<WS.Response, Result>() {
					public Result apply(WS.Response response) {
						Iterator<JsonNode> it = response.asJson().elements();
						JsonNode json;
						
						while (it.hasNext()) {
							json = it.next();
							if ((json.findValue("name").asText().compareTo(nom)) == 0) {
								String nbVelo = json.findValue("available_bikes").toString();
								return ok(views.html.veloDisponible.render(nbVelo, nom, transportForm));
							}
						}
						return ok("erreur");
					}
				});

		return resultPromise;
	}

}
