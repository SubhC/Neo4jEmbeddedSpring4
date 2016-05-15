package org.eshark.neospringem.web.lstnr;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.eshark.neospringem.graph.GraphDBService;


public class ApplicationStartUpShutDownListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("---- initialize servlet context -----");
        GraphDBService.getInstance().startDataBase();
        // add initialization code here
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("---- destroying servlet context -----");
        GraphDBService.getInstance().stopDataBase();
    }
}