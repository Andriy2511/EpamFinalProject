package com.example.finalproject.command.admin;

import com.example.finalproject.Main;
import com.example.finalproject.command.ICommand;
import com.example.finalproject.dao.DAOFactory;
import com.example.finalproject.dao.ICategoryDAO;
import com.example.finalproject.dao.IGoodsDAO;
import com.example.finalproject.models.Goods;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.sql.SQLException;

public class AddProductCommand implements ICommand {
    DAOFactory daoFactory;
    IGoodsDAO goodsDAO;
    ICategoryDAO categoryDAO;
    String notification;
    private static final Logger logger = LogManager.getLogger(AddProductCommand.class);

    public AddProductCommand(){
        daoFactory = DAOFactory.getDaoFactory("MYSQL");
        goodsDAO = daoFactory.getGoodsDAO();
        categoryDAO = daoFactory.getCategoryDAO();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        addProduct(request, response);
    }

    private void addProduct(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.info("The showList addProduct is started");
        double price = 0;
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String photo = "without photo";
        String categoryName = request.getParameter("category");
        try {
            price = Double.parseDouble(request.getParameter("price"));
        } catch (NumberFormatException e) {
            logger.error(e);
            e.printStackTrace();
            response.sendRedirect("admin/admin_add_product.jsp");
        }

        if(price<0) {
            notification = "Price must be higher or equal than 0";
            request.setAttribute("NOTIFICATION", notification);
            logger.debug("Forward to admin_add_product.jsp, notification {}", notification);
            RequestDispatcher dispatcher = request.getRequestDispatcher("admin/admin_add_product.jsp");
            dispatcher.forward(request, response);
        }

        if(request.getPart("photo").getSize() > 0) {
            Part part = request.getPart("photo");
            photo = part.getSubmittedFileName();
            addPhoto(part, photo);
        }

        if(isCategoryExist(categoryName)){
            addGoods(name, description, photo, price, categoryName);
        } else {
            addCategory(categoryName);
            addGoods(name, description, photo, price, categoryName);
        }

        request.setAttribute("NOTIFICATION", notification);
        logger.debug("Forward to admin_add_product.jsp");
        RequestDispatcher dispatcher = request.getRequestDispatcher("admin/admin_add_product.jsp");
        dispatcher.forward(request, response);
    }


    //TODO Change path
    private void addPhoto(Part part, String photo) throws IOException {
        String tempPath = "C:\\Users\\andri\\finalProject\\src\\main\\webapp\\image";
//        String path = request.getServletContext().getRealPath("\\"+ "image"+ File.separator+photo);
        String path = tempPath + File.separator+ photo;
        part.write(path);
    }

    private void addGoods(String name, String description, String photo, double price, String categoryName) {
        Goods goods = new Goods(name, description, photo, price, getCategoryId(categoryName));
        if(goodsDAO.addGoods(goods)){
            notification = "Goods added successful";
        } else {
            notification = "Goods must contain unique name!";
        }
    }

    private boolean isCategoryExist(String name){
        return categoryDAO.showCategoryByName(name).size() == 1;
    }

    private void addCategory(String name){
        categoryDAO.addCategory(name);
    }

    private int getCategoryId(String name){
        return categoryDAO.showCategoryByName(name).get(0).getId();
    }
}