package org.example;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class WebAppInitiakizer implements WebApplicationInitializer {

    @Override
    public void onStartup(javax.servlet.ServletContext servletContext) throws ServletException {

        XmlWebApplicationContext appContext = new XmlWebApplicationContext();
        appContext.setConfigLocation("classpath:app-config.xml");
        servletContext.addListener(new ContextLoaderListener(appContext));

        XmlWebApplicationContext webContext = new XmlWebApplicationContext();
        webContext.setConfigLocation("classpath:web-config.xml");

        DispatcherServlet dispatcherServlet = new DispatcherServlet(webContext);

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");


    }
}
