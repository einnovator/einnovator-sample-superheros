package org.einnovator.sample.superheros.web;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.einnovator.sample.superheros.manager.SuperheroManager;
import org.einnovator.sample.superheros.model.Superhero;
import org.einnovator.sample.superheros.modelx.SuperheroFilter;
import org.einnovator.util.MappingUtils;
import org.einnovator.util.PageOptions;
import org.einnovator.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/superhero")
public class SuperheroController extends ControllerBase {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private SuperheroManager manager;

	@GetMapping
	public String list(@ModelAttribute("filter") SuperheroFilter filter, PageOptions options,  @RequestParam(required=false) Boolean async,
			Model model, Principal principal, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		
		Page<Superhero> page = manager.findAll(filter, options.toPageRequest());
		model.addAttribute("superheros", page);
		model.addAttribute("page", page);
		model.addAttribute("pageJson", PageUtil.toJson(page, false));

		logger.info("list: " + PageUtil.toString(page) + " " + filter + " " + options);
		return Boolean.TRUE.equals(async) ? "superhero/superhero-table" : "superhero/list";

	}

	@GetMapping("/{id:.*}")
	public String show(@PathVariable("id") String id, String slug,
			Model model, Principal principal, HttpServletRequest request,  RedirectAttributes redirectAttributes) {
		
		Superhero superhero = manager.find(id);

		if (superhero == null) {
			notfound("show", request, redirectAttributes);
			return redirect("/superhero");
		}
		if (!isAllowedView(principal, superhero)) {
			forbidden("show", request, redirectAttributes);
			return redirect("/superhero");
		}
		model.addAttribute("superhero", superhero);
		model.addAttribute("superheroJson", MappingUtils.toJson(superhero));


		logger.info("show: " + superhero);
		return "superhero/show";
	}

	@GetMapping("/create")
	public String createGET(@ModelAttribute("superhero") Superhero superhero,
			Model model, Principal principal, HttpServletRequest request,  RedirectAttributes redirectAttributes) {
		
		logger.info("create: " + superhero);
		addCommonToModel(principal, model);
		return "superhero/edit";
	}

	@PostMapping
	public String createPOST(@ModelAttribute("superhero") Superhero superhero, BindingResult errors,
			Model model, Principal principal, RedirectAttributes redirectAttributes, HttpServletRequest request) {

		if (!isAllowedCreate(principal, superhero)) {
			forbidden("createPOST", request, redirectAttributes);
			return redirect("/superhero");
		}
		if (errors.hasErrors()) {
			addCommonToModel(principal, model);
			failure("createPOST", request, redirectAttributes, errors);
			return "superhero/edit";
		}
		Superhero superhero2 = manager.create(superhero, true);
		if (superhero2 == null) {
			logger.error("createPOST: " + superhero);
			failure("createPOST", request, redirectAttributes, superhero);
			return "";
		}
		logger.info("createPOST: " + superhero2);
		model.addAttribute("superhero", superhero2);
		return redirect("/superhero/" + superhero2.getUuid());
	}

	@GetMapping("/{id:.*}/edit")
	public String editGet(@PathVariable("id") String id,
			Model model, Principal principal, HttpServletRequest request,  RedirectAttributes redirectAttributes) {

		Superhero superhero = manager.find(id);
		if (superhero == null) {
			notfound("editGet", request, redirectAttributes);
			return redirect("/superhero");
		}
		if (!isAllowedManage(principal, superhero)) {
			forbidden("show", request, redirectAttributes);
			return redirect("/superhero");
		}
		
		logger.info("editGet: " + superhero);
		model.addAttribute("superhero", superhero);
		return "superhero/edit";
	}

	@PutMapping("/{id_:.*}")
	public String editPut(@PathVariable("id_") String id_, @ModelAttribute("superhero") Superhero superhero, BindingResult errors,
			Model model, Principal principal, RedirectAttributes redirectAttributes, HttpServletRequest request) {

		Superhero superhero0 = manager.find(id_);
		if (superhero0 == null) {
			notfound("editPut", request, redirectAttributes);
			return redirect("/superhero");
		}
		if (!isAllowedManage(principal, superhero0)) {
			forbidden("editPut", request, redirectAttributes);
			return redirect("/superhero");
		}
		superhero.setId(superhero0.getId());
		Superhero superhero2 = manager.update(superhero, true, true);
		if (superhero2 == null) {
			logger.error("editPut:  " + HttpStatus.BAD_REQUEST.getReasonPhrase());
			return redirect("/superhero");
		}
		success("editPut", request, redirectAttributes, superhero);
		model.addAttribute("superhero", superhero2);
		return redirect("/superhero/" + superhero.getUuid());
	}

	@DeleteMapping("/{id:.*}")
	public String delete(@PathVariable String id, 
			Model model, Principal principal, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		Superhero superhero = manager.find(id);
		if (superhero == null) {
			notfound("delete", request, redirectAttributes);
			return redirect("/");
		}
		if (!isAllowedManage(principal, superhero)) {
			forbidden("delete", request, redirectAttributes);
			return redirect("/");
		}

		Superhero superhero2 = manager.delete(superhero, false);
		if (superhero2 == null) {
			failure("delete", request, redirectAttributes, id);
			return redirect("/superhero/" + superhero.getUuid());
		}
		success("delete", request, redirectAttributes, superhero);
		return redirect("/superhero");
	}


	protected void addCommonToModel(Principal principal, Model model) {
	}

}
