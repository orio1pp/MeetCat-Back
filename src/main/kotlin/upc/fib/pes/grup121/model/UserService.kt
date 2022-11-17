package upc.fib.pes.grup121.model

interface UserService {
    fun saveUser(user: User): User;
    fun saveRole(role:Role): Role;
    fun addRoleToUser(username: String, roleName: String);
    fun getUser(username: String): User;
    fun getUsers(): List<User>;
}