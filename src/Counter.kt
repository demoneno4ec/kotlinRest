package myFirstRestApi.test

import java.sql.*

class Counter() {
    private val table = "counter"
    private val connection: Connection
    private var value: Int? = null;

    init {
        val database: DataBase = DataBase()
        this.connection = database.connect()
    }


    fun reset() {
        this.truncate()

        try {
            this.execute("insert into " + this.table + "(value) values(0);")

            println("reset table " + this.table + "!")
        } catch (e: SQLException) {
            println(e.message)
        }
    }

    fun setValue(value: Int) {
        this.value = value
    }

    fun getValue(): Int {
        if (this.value != null) {
            return this.value!!;
        }

        val rs: ResultSet = this.query("select * from " + this.table)

        var currentValue = 0;
        if (rs.next()) {
            currentValue = rs.getInt("value")
        }

        this.setValue(currentValue);

        return 0
    }

    fun increment(value: Int = 0): String {
        val currentValue = getValue()

        val newValue: Int = currentValue + value
        this.update(newValue);

        return "New value = $newValue, Old value " + this.getValue()
    }

    fun decrement(value: Int = 0): String {
        val currentValue = getValue()

        val newValue: Int = currentValue - value
        this.update(newValue);

        return "New value = $newValue, Old value $currentValue"
    }

    private fun update(value: Int) {
        try {
            val sqlString = "UPDATE " + this.table + " SET value = $value WHERE value = " + this.getValue() + ";";
            this.execute(sqlString)

            println("Value update " + this.table + ", $value, " + this.getValue() + "!")
            this.setValue(value)
        } catch (e: SQLException) {
            println(e.message)
        }
    }

    private fun truncate() {
        try {
            this.execute("truncate " + this.table + " restart identity;");

            println("truncate table " + this.table + "!")
        } catch (e: SQLException) {
            println(e.message)
        }
    }

    private fun execute(sqlString: String) {
        val statement: Statement = this.connection.createStatement()
        statement.execute(sqlString)
    }

    private fun query(sqlString: String): ResultSet {
        val statement: Statement = this.connection.createStatement()
        return statement.executeQuery(sqlString)
    }
}