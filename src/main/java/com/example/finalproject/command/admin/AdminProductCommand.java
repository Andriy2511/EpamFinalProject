package com.example.finalproject.command.admin;

import com.example.finalproject.command.ICommand;
import com.example.finalproject.dao.DAOFactory;
import com.example.finalproject.dao.IGoodsDAO;
import com.example.finalproject.dao.IRoleDAO;
import com.example.finalproject.dao.IUserDAO;
import com.example.finalproject.dao.mysql.GoodsDAO;
import com.example.finalproject.models.Goods;
import com.example.finalproject.pagination.Pagination;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AdminProductCommand implements ICommand {
    DAOFactory daoFactory;
    IUserDAO userDAO;
    IRoleDAO roleDAO;
    IGoodsDAO goodsDAO;
    private static final Logger logger = LogManager.getLogger(AddProductCommand.class);
    int startPage = 1;
    int recordsPerPage = 5;
    String listParam;
    String lastMenu;
    List<Goods> goodsList;

    public AdminProductCommand(){
        daoFactory = DAOFactory.getDaoFactory("MYSQL");
        userDAO = daoFactory.getUserDAO();
        roleDAO = daoFactory.getRoleDAO();
        goodsDAO = daoFactory.getGoodsDAO();
        listParam = "";
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        showList(request, response);
    }

    private void showList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("The showList changeOrderStatus is started");
        String action = request.getParameter("action");
        try {
            switch (action){
                case "showGoodsList":
                    showGoods(request, response);
                case "change":
                    changeGoods(request, response);
                    break;
                case "delete":
                    deleteGoods(request, response);
                    showGoods(request, response);
                    break;
                default:
                    logger.debug("Forward to admin/admin_goods_list.jsp");
                    RequestDispatcher dispatcher = request.getRequestDispatcher("admin/admin_goods_list.jsp");
                    dispatcher.forward(request, response);
                    break;
            }
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
    }

    private void changeGoods(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.info("The method changeGoods is started");
        int goodsId = Integer.parseInt(request.getParameter("goodsId"));
        Goods goods = goodsDAO.getGoodsById(goodsId).get(0);
        request.setAttribute("goods", goods);
        logger.debug("Forward to admin/admin_change_product.jsp");
        RequestDispatcher dispatcher = request.getRequestDispatcher("admin/admin_change_product.jsp");
        dispatcher.forward(request, response);
    }

    private void deleteGoods(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("The method deleteGoods is started");
        int goodsId = Integer.parseInt(request.getParameter("goodsId"));
        if(!goodsDAO.deleteGoods(goodsId)){
            request.setAttribute("NOTIFICATION", "This product cannot be deleted because the user has already placed an order for it");
        }
    }

    private void showGoods(HttpServletRequest request, HttpServletResponse response) throws ServletException, SQLException, IOException {
        logger.info("The method showGoods is started");
        int countOfGoods;
//        changeStartPageIfChangeMenu(listParam);
        countOfGoods = goodsDAO.showCountOfGoods();
        startPage = Pagination.pagination(request, countOfGoods, startPage, recordsPerPage);
        request.setAttribute("noOfPages", startPage);
        goodsList = goodsDAO.showLimitGoods((startPage-1)*recordsPerPage, recordsPerPage);
        sendGoodsList(request, response, goodsList);
    }

    private void sendGoodsList(HttpServletRequest request, HttpServletResponse response, List<Goods> goodsList)
            throws ServletException, IOException {
        logger.info("The method sendGoodsList is started");
        request.setAttribute("goodsList", goodsList);
        request.setAttribute("currentPage", startPage);
        logger.debug("Forward to admin/admin_goods_list.jsp");
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("admin/admin_goods_list.jsp");
        requestDispatcher.forward(request, response);
    }
}