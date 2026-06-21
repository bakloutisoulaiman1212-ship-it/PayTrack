package com.example.paytrack.data.repository


import com.example.paytrack.data.localuser.User
import com.example.paytrack.data.localuser.UserDao

class UserRepository(private val userDao: UserDao) {

    suspend fun register(user: User) {
        userDao.insertUser(user)
    }

    suspend fun login(username: String, password: String): User? {
        return userDao.login(username, password)
    }

    suspend fun userExists(username: String): Boolean {
        return userDao.getUserByUsername(username) != null
    }
    suspend fun changePassword(username: String, old: String, new: String): Boolean {
        val user = userDao.checkUser(username, old)

        return if (user != null) {
            userDao.updatePassword(username, new)
            true
        } else {
            false
        }
    }
}
