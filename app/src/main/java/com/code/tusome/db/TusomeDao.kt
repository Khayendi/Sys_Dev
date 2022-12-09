package com.code.tusome.db

import androidx.room.*
import com.code.tusome.models.AssignmentDB
import com.code.tusome.models.UserDB

@Dao
interface TusomeDao {
    /////////////////////////////////////////////////////////
    ///  /////  ////        ///////         /////         //
    @Insert
    suspend fun insert(user: UserDB)

    @Query("select * from user order by id desc")
    suspend fun getAll(): List<UserDB>

    @Update
    suspend fun updateUser(user: UserDB)

    @Delete
    suspend fun deleteUser(user: UserDB)

    ////////////////////////////////////////////////////
    @Insert
    suspend fun saveAssignment(assignmentDB: AssignmentDB)

    @Query("select * from assignment order by date_issued asc")
    suspend fun getAssignments(): List<AssignmentDB>

    @Update
    suspend fun updateAssignment(assignmentDB: AssignmentDB)

    @Query("update assignment set name =:name,unit_name=:unitName,date_issued=:issueDate,due_date=:dueDate where uid=:uid")
    suspend fun updateAssign(uid: String, name: String, unitName: String, issueDate: String, dueDate: String)

    @Delete
    suspend fun deleteAssignment(assignmentDB: AssignmentDB)
    @Query("delete from assignment where uid=:uid")
    suspend fun deleteAssign(uid: String)
    ///////////////////////////////////////////////////////

}
