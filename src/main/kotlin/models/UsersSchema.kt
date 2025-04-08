package com.miaoyongzheng.models

import com.miaoyongzheng.utils.Const
import com.miaoyongzheng.utils.UserUtil
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class User(
    val id: Int = -1,
    val username: String? = null,
    val password: String,
    val phone: String,
    val avatar: String? = null
)

class UserService : BaseSchema(Users) {

    object Users : Table(name = "users") {
        val id = integer("id").autoIncrement()
        val username = varchar("username", length = 32)
        val password = varchar("password", length = 32)
        val phone = varchar("phone", length = 32).uniqueIndex()
        val avatar = varchar("avatar", length = 4000).nullable()

        override val primaryKey = PrimaryKey(id)
    }


    fun create(user: User): Int = transaction {
        println("create user: $user")
        Users.insert {
            val u = user.copy(username = UserUtil.generateRandomUsername(16))
            it[username] = u.username!!
            it[password] = u.password
            it[phone] = u.phone
            it[avatar] = u.avatar?: Const.USER_DEFAULT_AVATAR
        }[Users.id]
    }

    fun getUserByPhone(phone: String): User? = transaction {
        Users.selectAll()
            .where { Users.phone eq phone }
            .map { User(it[Users.id],it[Users.username], it[Users.password], it[Users.phone], it[Users.avatar]) }
            .singleOrNull()
    }

    fun getUserByPhonePassword(phone: String, password: String): User? = transaction {
        Users.selectAll()
            .where { Users.phone eq phone and (Users.password eq password) }
            .map { User(it[Users.id],it[Users.username], it[Users.password], it[Users.phone], it[Users.avatar]) }
            .singleOrNull()
    }


//    suspend fun read(id: Int): User? {
//        return dbQuery {
//            Users.selectAll()
//                .where { Users.id eq id }
//                .map { User(it[Users.name], it[Users.age]) }
//                .singleOrNull()
//        }
//    }
//
//    suspend fun update(id: Int, user: User) {
//        dbQuery {
//            Users.update({ Users.id eq id }) {
//                it[name] = user.name
//                it[age] = user.age
//            }
//        }
//    }
//
//    suspend fun delete(id: Int) {
//        dbQuery {
//            Users.deleteWhere { Users.id.eq(id) }
//        }
//    }


}

