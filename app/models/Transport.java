package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.*;
import play.db.ebean.Model;

@Entity
public class Transport extends Model {

	@Id
	public Long id;
	
	@Required
	public String label;
	
	public int nbLike;

	public static Finder<Long,Transport> find = new Finder(
			Long.class, Transport.class
			);

	public static List<Transport> all() {
		return find.all();
	}

	public static void create(Transport transport) {
		transport.save();
	}

	public static void delete(Long id) {
		find.ref(id).delete();
	}
}
