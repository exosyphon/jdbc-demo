package com.example.jdbcdemo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.core.JdbcTemplate

@SpringBootApplication
class JdbcDemoApplication {
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Bean
    fun boot() = CommandLineRunner {
        println("truncating")
        println("beginning")
        val time = System.currentTimeMillis()
//        val stringBuilder = StringBuilder()
//        stringBuilder.append("INSERT INTO data (name, second_name) VALUES ")
        val sql = "INSERT INTO data (name, second_name) VALUES (?, ?)"
        val conn = jdbcTemplate.dataSource!!.connection
        conn.autoCommit = false
        val preparedStatement = conn.prepareStatement(sql)
        for (i in 1..5_000_000) {
//            if (i != 1) {
//                stringBuilder.append(", ")
//            }
            preparedStatement.setString(1, "$i")
            preparedStatement.setString(2, "name $i")
            preparedStatement.addBatch()
//            stringBuilder.append("($i, 'name $i') ")
        }
//        stringBuilder.append(";")

        println("about to save")
//        jdbcTemplate.batchUpdate(stringBuilder.toString())
//        conn.autoCommit = true

        try {
            preparedStatement.executeBatch()
            conn.commit()
        } catch (e: Exception) {
            println(e.message)
        } finally {
            conn.autoCommit = true
        }
        val time2 = System.currentTimeMillis()
        println("finished : ${time2 - time} MS")
    }
}

fun main(args: Array<String>) {
    runApplication<JdbcDemoApplication>(*args)
}
