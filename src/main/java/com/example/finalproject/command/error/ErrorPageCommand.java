package com.example.finalproject.command.error;

import com.example.finalproject.command.ICommand;
import com.example.finalproject.command.admin.AddProductCommand;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

public class ErrorPageCommand implements ICommand {
    private static final Logger logger = LogManager.getLogger(ErrorPageCommand.class);
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        logger.info("Method execute is started");
        logger.debug("Forward to the error/error_page.jsp");
        RequestDispatcher dispatcher = request.getRequestDispatcher("error/error_page.jsp");
        dispatcher.forward(request, response);
    }
}