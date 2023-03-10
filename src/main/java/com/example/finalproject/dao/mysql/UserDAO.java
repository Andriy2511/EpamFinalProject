package com.example.finalproject.dao.mysql;

import com.example.finalproject.dao.DAOFactory;
import com.example.finalproject.dao.GenericDAO;
import com.example.finalproject.dao.IRoleDAO;
import com.example.finalproject.dao.IUserDAO;
import com.example.finalproject.dao.query.DBQuery;
import com.example.finalproject.models.User;
import com.example.finalproject.utils.JDBCUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends GenericDAO<User> implements IUserDAO {
    private static final Logger logger = LogManager.getLogger(UserDAO.class);
    private UserDAO(){
    }
    DAOFactory daoFactory = DAOFactory.getDaoFactory("MYSQL");
    IRoleDAO roleDAO = daoFactory.getRoleDAO();

    private static UserDAO instance;
    public static IUserDAO getInstance() {
        if (instance == null)
            instance = new UserDAO();
        return instance;
    }

    @Override
    public boolean validate(User user) {
        boolean status = false;
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement(DBQuery.SELECT_USER_BY_LOGIN_AND_PASSWORD)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            ResultSet rs = preparedStatement.executeQuery();
            status = rs.next();
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        return status;
    }

    @Override
    public int getUserId(User user) {
        int userId = 0;
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement(DBQuery.SELECT_USER_ID_BY_LOGIN_AND_PASSWORD)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                userId = rs.getInt("id");
            }
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        return userId;
    }

    @Override
    public boolean createUser(User user) throws SQLException {
        Connection connection = JDBCUtils.getConnection();
        try{
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(DBQuery.INSERT_USER);
            mapFromEntity(statement, user);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.error(e);
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
        return true;
    }

    @Override
    public boolean updateName(int id, String name) throws SQLException {
        boolean result = false;
        Connection connection = JDBCUtils.getConnection();
        try{
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(DBQuery.UPDATE_USER_NAME);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, id);
            result = preparedStatement.executeUpdate() == 1;
            connection.commit();
        } catch (SQLException e) {
            logger.error(e);
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
        return result;
    }

    @Override
    public boolean updateSurname(int id, String surname) throws SQLException {
        boolean result = false;
        Connection connection = JDBCUtils.getConnection();
        try{
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(DBQuery.UPDATE_USER_SURNAME);
            preparedStatement.setString(1, surname);
            preparedStatement.setInt(2, id);
            result = preparedStatement.executeUpdate() == 1;
            connection.commit();
        } catch (SQLException e) {
            logger.error(e);
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
        return result;
    }

    @Override
    public boolean updateLogin(int id, String login) throws SQLException {
        boolean result = false;
        Connection connection = JDBCUtils.getConnection();
        try{
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(DBQuery.UPDATE_USER_LOGIN);
            preparedStatement.setString(1, login);
            preparedStatement.setInt(2, id);
            result = preparedStatement.executeUpdate() == 1;
            connection.commit();
        } catch (SQLException e) {
            logger.error(e);
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
        return result;
    }

    @Override
    public boolean updatePassword(int id, String password) throws SQLException {
        boolean result = false;
        Connection connection = JDBCUtils.getConnection();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(DBQuery.UPDATE_USER_PASSWORD);
            preparedStatement.setString(1, password);
            preparedStatement.setInt(2, id);
            result = preparedStatement.executeUpdate() == 1;
            connection.commit();
        } catch (SQLException e) {
            logger.error(e);
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
        return result;
    }

    @Override
    public boolean updateEmail(int id, String email) throws SQLException {
        boolean result = false;
        Connection connection = JDBCUtils.getConnection();
        try{
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(DBQuery.UPDATE_USER_EMAIL);
            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, id);
            result = preparedStatement.executeUpdate() == 1;
            connection.commit();
        } catch (SQLException e) {
            logger.error(e);
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
        return result;
    }

    @Override
    public int getRoleIdUser() {
        int id = 0;
        try(Connection connection = JDBCUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DBQuery.SELECT_ROLE_ID_BY_NAME)) {
            preparedStatement.setString(1, "user");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                id = rs.getInt("id");
            }
        } catch (SQLException e){
            logger.error(e);
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public List<User> getUserById(Integer id) {
        List<User> userList = new ArrayList<>();
        try(Connection connection = JDBCUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(DBQuery.SELECT_USER_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                userList.add(mapToEntity(rs));
            }
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated = false;
        Connection connection = JDBCUtils.getConnection();
        try{
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(DBQuery.UPDATE_USER);
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getLogin());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getEmail());
            statement.setInt(6, user.getId());
            rowUpdated = statement.executeUpdate() > 0;
            connection.commit();
        } catch (SQLException e) {
            logger.error(e);
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
        return rowUpdated;
    }

    @Override
    public boolean deleteUser(String login) throws SQLException {
        Connection connection = JDBCUtils.getConnection();
        boolean result = false;
        try{
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(DBQuery.DELETE_USER_BY_LOGIN);
            statement.setString(1, login);
            result = statement.executeUpdate() == 1;
            connection.commit();
        } catch (SQLException e) {
            logger.error(e);
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
        return result;
    }

    @Override
    public boolean deleteUserById(int id) throws SQLException {
        boolean rowDeleted = false;
        Connection connection = JDBCUtils.getConnection();
        try{
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(DBQuery.DELETE_USER_BY_ID);
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
        return rowDeleted;
    }

    @Override
    public List<User> readUserByLogin(String login) {
        List<User> userList = new ArrayList<>();
        try(Connection connection = JDBCUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(DBQuery.SELECT_USER_BY_LOGIN);
            preparedStatement.setString(1, login);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                userList.add(mapToEntity(rs));
            }
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public List<User> showAllUsers(){
        List<User> userList = new ArrayList<>();
        try(Connection connection = JDBCUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DBQuery.SELECT_ALL_FROM_USERS)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                userList.add(mapToEntity(rs));
            }
        } catch (SQLException e){
            logger.error(e);
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public List<User> showLimitUsers(int from, int numberOfRecords){
        List<User> userList = new ArrayList<>();
        try(Connection connection = JDBCUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DBQuery.SELECT_LIMIT_USERS)) {
            preparedStatement.setInt(1, roleDAO.getUserId());
            preparedStatement.setInt(2, from);
            preparedStatement.setInt(3, numberOfRecords);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                userList.add(mapToEntity(rs));
            }
        } catch (SQLException e){
            logger.error(e);
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public List<User> showLimitUsersByBlockedStatus(int from, int numberOfRecords, boolean isBlocked){
        List<User> userList = new ArrayList<>();
        try(Connection connection = JDBCUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DBQuery.SELECT_LIMIT_USERS_BY_BLOCKED_STATUS)) {
            preparedStatement.setInt(1, roleDAO.getUserId());
            preparedStatement.setBoolean(2, isBlocked);
            preparedStatement.setInt(3, from);
            preparedStatement.setInt(4, numberOfRecords);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                userList.add(mapToEntity(rs));
            }
        } catch (SQLException e){
            logger.error(e);
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public List<User> showBlockedUsers(){
        List<User> userList = new ArrayList<>();
        try(Connection connection = JDBCUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DBQuery.SELECT_BLOCKED_USERS)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                userList.add(mapToEntity(rs));
            }
        } catch (SQLException e){
            logger.error(e);
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public List<User> showUnblockedUsers(){
        List<User> userList = new ArrayList<>();
        try(Connection connection = JDBCUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DBQuery.SELECT_UNBLOCKED_USERS)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                userList.add(mapToEntity(rs));
            }
        } catch (SQLException e){
            logger.error(e);
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public int showCountOfUsers(){
        int count = 0;
        try(Connection connection = JDBCUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DBQuery.SELECT_NUMBER_OF_USERS)) {
            preparedStatement.setInt(1, roleDAO.getUserId());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                count = rs.getInt(1);
            }
        } catch (SQLException e){
            logger.error(e);
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public int showCountOfUsersByBlockedStatus(boolean statusBlocked){
        int count = 0;
        try(Connection connection = JDBCUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DBQuery.SELECT_NUMBER_OF_USERS_BY_BLOCKED_STATUS)) {
            preparedStatement.setInt(1, roleDAO.getUserId());
            preparedStatement.setBoolean(2, statusBlocked);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                count = rs.getInt(1);
            }
        } catch (SQLException e){
            logger.error(e);
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public boolean blockUser(int id) throws SQLException {
        boolean result = false;
        Connection connection = JDBCUtils.getConnection();
        try{
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(DBQuery.UPDATE_USER_STATUS_BLOCKED_TRUE);
            preparedStatement.setInt(1, id);
            result = preparedStatement.executeUpdate() == 1;
            connection.commit();
        } catch (SQLException e){
            logger.error(e);
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
        return result;
    }

    @Override
    public boolean unblockUser(int id) throws SQLException {
        boolean result = false;
        Connection connection = JDBCUtils.getConnection();
        try{
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(DBQuery.UPDATE_USER_STATUS_BLOCKED_FALSE);
            preparedStatement.setInt(1, id);
            result = preparedStatement.executeUpdate() == 1;
            connection.commit();
        } catch (SQLException e){
            logger.error(e);
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
        return result;
    }

    @Override
    protected User mapToEntity(ResultSet rs) throws SQLException {
        return new User(rs.getInt("id"), rs.getString("name"), rs.getString("surname"),
                rs.getString("login"), rs.getString("password"), rs.getString("email"),
                rs.getInt("roles_id"), rs.getBoolean("status_blocked"));
    }

    @Override
    protected void mapFromEntity(PreparedStatement preparedStatement, User user) throws SQLException {
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getSurname());
        preparedStatement.setString(3, user.getLogin());
        preparedStatement.setString(4, user.getPassword());
        preparedStatement.setString(5, user.getEmail());
        preparedStatement.setInt(6, getRoleIdUser());
    }
}
