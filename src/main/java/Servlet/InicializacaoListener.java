package Servlet;

import util.Banco;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class InicializacaoListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent evento) {
        Banco.inicializar();
    }
    @Override
public void contextDestroyed(ServletContextEvent evento) {
    
}
}
