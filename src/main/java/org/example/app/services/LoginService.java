package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.LoginForm;
import org.springframework.stereotype.Service;

import static org.example.web.config.Settings.ROOT_PASSWORD;
import static org.example.web.config.Settings.ROOT_USER;

@Service
public class LoginService {

    private Logger logger = Logger.getLogger(LoginService.class);

    public boolean authenticate(LoginForm loginForm) {
        logger.info("try to auth with user-form " + loginForm);
        return loginForm.getUsername().equals(ROOT_USER) && loginForm.getPassword().equals(ROOT_PASSWORD);
    }
}
