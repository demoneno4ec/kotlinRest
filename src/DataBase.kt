package myFirstRestApi.test

import java.sql.*

class DataBase {
    private val dbUrl = "jdbc:postgresql://localhost:5432/firstrestapi"
    private val dbUser = "ladmin"
    private val dbPassword = "password"

    fun connect(): Connection {
        return DriverManager.getConnection(this.dbUrl, this.dbUser, this.dbPassword);
    }
}