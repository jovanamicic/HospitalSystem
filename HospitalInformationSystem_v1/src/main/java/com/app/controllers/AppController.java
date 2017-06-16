package com.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AppController {
	
	@RequestMapping(value = "/noResult", method = RequestMethod.GET)
	@ResponseBody
	 public String noResult(@RequestParam(value = "q") String q) {
        return "<html><head><title>Prikaz svih pacijenata</title><meta charset=\"utf-8\" /><meta name=\"viewport\"content=\"width=device-width, initial-scale=1, user-scalable=no\" /><!-- CSS files --><link rel=\"stylesheet\" href=\"static/css/main.css\" /><link href='static/css//fullcalendar.css' rel='stylesheet' /><link href='static/css/fullcalendar.print.css' rel='stylesheet'media='print' /><!-- JS files --><script src=\"static/js/jquery.min.js\"></script><script src='static/js/fullcalendar/moment.min.js'></script><script src='static/js/fullcalendar/fullcalendar.min.js'></script><script src='static/js/fullcalendar/sr.js'></script><script src='static/jsController/common/sessionUser.js'></script><script src='static/jsController/logout/logout.js'></script><script src='static/jsController/medicalStaff/patients.js'></script></head><body onload =\"getLoggedUser()\"><!-- Wrapper --><div id=\"wrapper\"><!-- Main --><div id=\"main\"><!-- Header image --><img class='headerImage' src=\"static/images/slika22.jpg\" /><div class=\"inner\"><!-- Content --><section><header class=\"major\"><h2>Pacijenti</h2></header><br><h3 style=\"text-align: center;\">Nema rezultata pretrage za  "+q+" .</h3></section></div></div><!-- Sidebar --><div id=\"sidebar\"><div class=\"inner\"><!-- Picture --><div class=\"4u\"style=\"border: None; margin: 0 auto; display: table; width: 50%\"><span class=\"image fit\"><img class='image' id=\"personPhoto\"src=\"\" alt=\"\" style=\"border-radius: 50%\" /></span></div><!-- Menu --><nav id=\"menu\"><header class=\"major\"><h2 id=\"personName\"></h2></header><ul><li><a href=\"doctorAllPatients.html\">Pacijenti</a></li><li><a href=\"doctorPatientRegistration.html\">Registrujpacijenta</a></li><li><a href=\"doctorSchedule.html\">Kalendar</a></li><li><a onclick=\"logout()\">Odjava</a></li></ul></nav></div></div></div><!-- Scripts --><script src=\"static/js/skel.min.js\"></script><script src=\"static/js/util.js\"></script><script src=\"static/js/main.js\"></script></body></html>";
    }

}
