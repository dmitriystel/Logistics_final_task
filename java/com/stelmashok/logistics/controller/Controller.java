package com.stelmashok.logistics.controller;

import java.io.*;

import com.stelmashok.logistics.controller.command.*;
import com.stelmashok.logistics.exception.CommandException;
import com.stelmashok.logistics.model.connection.ConnectionPool;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
// (name = "appController", value = "/controller")
// @WebServlet(name = "helloServlet", urlPatterns = {"/controller", "*.do"}) // имена для доступа к сервлету - удалить?
@WebServlet(name = "Controller", urlPatterns = "/controller")
/*
Сервлеты реализуют общий интерфейс Servlet, в котором реализованы методы service(), init(), destroy()
При разработке сервлетов в качестве суперкласса в большинстве случаев используется абстрактный класс HttpServlet,
отвечающий за обработку запросов HTTP.
Приложение многопоточное, сервлет один
Клиентов много, все они обращаются к этому, единственному сервлету
 */
public class Controller extends HttpServlet {
    static Logger logger = (Logger) LogManager.getLogger();
/*
Жизненный цикл сервлета начинается с его инициализации, методом метод init(), и загрузки в память контейнером сервлетов
при старте контейнера либо в ответ на первый клиентский запрос. Сервлет готов к обслуживанию любого числа запросов.
Метод init() дает сервлету возможность инициализировать данные и подготовиться для обработки запросов. Чаще всего в этом
методе размещается код, кэширующий данные фазы инициализации.
init срабатывает один раз
 */
    /*
    public void init() {
        ConnectionPool.getInstance();
        logger.log(Level.INFO, "---------------> Servlet Init: " + this.getServletInfo());
    }

     */
/*
Для обработки запроса в классе HttpServlet определен ряд методов, которые мы можем переопределить в классе сервлета (Controller):
- doGet: обрабатывает запросы GET;
Запрос GET передает данные в URL в виде пар “имя-значение” (другими словами, через ссылку)
Методы передает на сервер запросы, а также параметры для их выполнения.
Метод GET (method="GET") используется для запроса содержимого указанного ресурса, изображения или гипертекстового документа.
Вместе с запросом могут передаваться дополнительные параметры как часть URI, значения могут выбираться из полей формы или
передаваться непосредственно через URL.
При этом запросы кэшируются и имеют ограничения на размер. Этот метод является основным методом взаимодействия браузера
клиента и веб-сервера.
 */
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    processCommand(req, resp);
}

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processCommand(req, resp);
    }
/*
При выгрузке приложения из контейнера, то есть по окончании жизненного цикла сервлета, вызывается метод destroy(), в теле
которого следует помещать код освобождения занятых сервлетом ресурсов.
 */
private void processCommand(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String commandName = req.getParameter(COMMAND);
    Command command = CommandFactory.getInstance().getCommand(commandName);
    Router router = command.execute(req);
    RoutingTypeHolder routingTypeHolder = router.getRoutingType();
    String resultPage = router.getResultPage();
    switch (routingTypeHolder) {
        case FORWARD ->
                req.getRequestDispatcher(resultPage).forward(req, resp);
        case REDIRECT ->
                resp.sendRedirect(req.getContextPath() + router.getResultPage());
        case ERROR ->
                resp.sendError(resultPage.equals(PagePathHolder.ERROR_PAGE_404) ? 404 : 500);
        default -> {
            logger.error("wrong routing type");
            resp.sendError(500);
        }
    }
}
}