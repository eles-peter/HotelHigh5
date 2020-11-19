package com.progmasters.hotel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Temp {

    public static String url= "jdbc:mysql://localhost:3306/hotel?serverTimezone=UTC&characterEncoding=utf8";
    public static String user= "root";
    public static String password= "test1234";


    public static void main(String[] args) {


        System.out.println(getImageUrlWhereHotelIdMoreThan(50L));

    }

    public static List<String> getImageUrlWhereHotelIdMoreThan(Long hotelId) {
        List<String> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            try (Statement statement = connection.createStatement()) {
                String sqlSelectQuery = "SELECT hotel_image_urls FROM hotel_hotel_image_urls WHERE hotel_id = 1";
                ResultSet resultSet = statement.executeQuery(sqlSelectQuery);
                while (resultSet.next()) {
                    result.add(resultSet.getString("hotel_image_urls"));
                }

                sqlSelectQuery = "SELECT room_image_url FROM room WHERE hotel_id = 1";
                resultSet = statement.executeQuery(sqlSelectQuery);
                while (resultSet.next()) {
                    result.add(resultSet.getString("room_image_url"));
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }



}
