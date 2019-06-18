package org.einnovator.sample.superheros.web;

import java.net.URI;
import java.security.Principal;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.einnovator.sample.superheros.manager.SuperheroManager;
import org.einnovator.sample.superheros.model.Superhero;
import org.einnovator.sample.superheros.modelx.SuperheroFilter;
import org.einnovator.util.PageOptions;
import org.einnovator.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriTemplate;

@RestController
@RequestMapping("/api/superhero")
public class SuperheroRestController extends ControllerBase {

	@Autowired
	private SuperheroManager manager;


	@GetMapping
	public ResponseEntity<Page<Superhero>> listSuperheros(PageOptions options, SuperheroFilter filter, 
			Principal principal, HttpServletResponse response) {
		if (!isAllowed(principal, false)) {
			return forbidden("listSuperheros", response);
		}
		Page<Superhero> page = manager.findAll(filter, options.toPageRequest());
		return ok(page, "listSuperheros", response,  PageUtil.toString(page), filter, options);
	}

	@PostMapping
	public ResponseEntity<Void> createSuperhero(@RequestBody Superhero superhero, BindingResult errors,
			Principal principal, HttpServletRequest request, HttpServletResponse response) {
		if (!isAllowedCreate(principal, superhero)) {
			return forbidden("createSuperhero", response);
		}

		if (errors.hasErrors()) {
			String err = errors.getAllErrors().stream().map(o -> o.getDefaultMessage()).collect(Collectors.joining(","));
			return badrequest("createSuperhero", response, err);
		}

		Superhero superhero2 = manager.create(superhero, false);
		if (superhero2 == null) {
			return badrequest("createSuperhero", response);
		}
		URI location = new UriTemplate(request.getRequestURI() + "/{id}").expand(superhero2.getUuid());
		return created(location, "createSuperhero", response);
	}


	@GetMapping("/{id:.*}")
	public ResponseEntity<Superhero> getSuperhero(@PathVariable String id,
		Principal principal, HttpServletResponse response) {		
		Superhero superhero = manager.find(id);
		if (superhero == null) {
			return notfound("getSuperhero", response);
		}
		if (!isAllowedView(principal, superhero)) {
			return forbidden("getSuperhero", response);
		}
		return ok(superhero, "getSuperhero", response);
	}

	@PutMapping("/{_id:.*}")
	public ResponseEntity<Void> updateSuperhero(@PathVariable("_id") String id, @RequestBody Superhero superhero,
		Principal principal, HttpServletResponse response) {		
		Superhero superhero0 = manager.find(id);
		if (superhero0 == null) {
			return notfound("updateSuperhero", response);
		}
		superhero.setUuid(id);
		if (!isAllowedManage(principal, superhero)) {
			return forbidden("updateSuperhero", response);
		}
		Superhero superhero2 = manager.update(superhero, true, false);
		if (superhero2 == null) {
			return badrequest("updateSuperhero", response, id);
		}
		return nocontent("updateSuperhero", response);
	}


	@DeleteMapping("/{id:.*}")
	public ResponseEntity<Void> deleteSuperhero(@PathVariable String id,
		Principal principal, HttpServletResponse response) {		
		Superhero superhero = manager.find(id);
		if (superhero == null) {
			return notfound("deleteSuperhero", response);
		}
		if (!isAllowedManage(principal, superhero)) {
			return forbidden("deleteSuperhero", response);
		}
		Superhero superhero2 = manager.delete(superhero);
		if (superhero2 == null) {
			return badrequest("deleteSuperhero", response, id);
		}
		return nocontent("deleteSuperhero", response);
	}

}
