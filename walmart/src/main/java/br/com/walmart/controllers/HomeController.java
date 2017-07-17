package br.com.walmart.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.walmart.entity.Partners;
import br.com.walmart.service.PartnersService;

@Controller
@RequestMapping("/api")
public class HomeController {

	public static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	PartnersService partnersService;

	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	public String index(String name) {
		logger.info("Index url ok");
		return name;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/partners/", method = RequestMethod.GET)
	public ResponseEntity<List<Partners>> listAllPartners() {
		logger.info("Partners url ok");
		List<Partners> partners = partnersService.findAllPartners();
		if (partners.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Partners>>(partners, HttpStatus.OK);
	}
}
