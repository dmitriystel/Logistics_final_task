package com.stelmashok.logistics.controller;

import java.io.*;

import com.stelmashok.logistics.controller.command.Command;
import com.stelmashok.logistics.exception.CommandException;
import com.stelmashok.logistics.model.connection.ConnectionPool;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
// (name = "appController", value = "/controller")
@WebServlet(name = "helloServlet", urlPatterns = {"/controller", "*.do"}) // имена для доступа к сервлету
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
    public void init() {
        ConnectionPool.getInstance();
        logger.log(Level.INFO, "---------------> Servlet Init: " + this.getServletInfo());
    }
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
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");

        String commandStr = request.getParameter("command");
        Command command = CommandType.define(commandStr);
        String page;
        try {
            page = command.execute(request);
            //request.getRequestDispatcher(page).forward(request, response);
            response.sendRedirect("../" + page);
        } catch (CommandException e) {
            //response.sendError(500);    //  1
            //throw new ServletException(e);  //  2
            request.setAttribute("error_msg", e.getCause());    // 3
            request.getRequestDispatcher("pages/error/error_500.jsp").forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
/*
При выгрузке приложения из контейнера, то есть по окончании жизненного цикла сервлета, вызывается метод destroy(), в теле
которого следует помещать код освобождения занятых сервлетом ресурсов.
 */
    public void destroy() {
        ConnectionPool.getInstance().destroyPool();
        logger.log(Level.INFO, "---------------> Servlet Destroyed: " + this.getServletName());
    }
}