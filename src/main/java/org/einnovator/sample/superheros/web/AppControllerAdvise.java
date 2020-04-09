package org.einnovator.sample.superheros.web;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.einnovator.common.web.CommonControllerAdvise;
import org.einnovator.notifications.client.manager.NotificationManager;
import org.einnovator.notifications.client.manager.PreferencesManager;
import org.einnovator.sso.client.SsoClient;
import org.einnovator.sso.client.manager.RoleManager;
import org.einnovator.sso.client.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;


@ControllerAdvice(assignableTypes = {HomeController.class, SuperheroController.class})
@Component
public class AppControllerAdvise extends CommonControllerAdvise {

	@Autowired
	private SsoClient ssoClient;

	@Autowired
	private NotificationManager notificationManager;

	@Autowired
	private PreferencesManager preferencesManager;
	
	@Autowired
	private RoleManager roleManager;

	@Override
	protected Object makePrincipalInfo(Principal principal, boolean invalid) {
		return SsoClient.getPrincipalUser();
	}

	@ModelAttribute("notificationCount")
	public Long notificationCount(Principal principal) {
		if (principal == null) {
			return null;
		}
		ssoClient.setupToken();
		return notificationManager.countNotifications(null);
	}

	@ModelAttribute("admin")
	public Boolean admin(Principal principal, HttpSession session) {
		return roleManager.isAdmin(principal);
	}

	@ModelAttribute("pref")
	public Map<String, String> pref(HttpSession session) {
		return preferencesManager.getAll(session);
	}
}
