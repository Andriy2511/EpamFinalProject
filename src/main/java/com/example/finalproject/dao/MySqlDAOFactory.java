package com.example.finalproject.dao;

import com.example.finalproject.dao.mysql.*;

/**
 * Factory for MySQL database
 */
public class MySqlDAOFactory extends DAOFactory{
    private static MySqlDAOFactory instance;

    public static DAOFactory getInstance() {
        if (instance == null)
            instance = new MySqlDAOFactory();
        return instance;
    }

    @Override
    public IUserDAO getUserDAO() {
        return UserDAO.getInstance();
    }

    @Override
    public ICategoryDAO getCategoryDAO() {
        return CategoryDAO.getInstance();
    }

    @Override
    public IGoodsDAO getGoodsDAO() {
        return GoodsDAO.getInstance();
    }

    @Override
    public IOrderDAO getOrderDAO() {
        return OrderDAO.getInstance();
    }

    @Override
    public IOrderStatusDAO getOrderStatusDAO() {
        return OrderStatusDAO.getInstance();
    }

    @Override
    public IRoleDAO getRoleDAO() {
        return RoleDAO.getInstance();
    }

}
