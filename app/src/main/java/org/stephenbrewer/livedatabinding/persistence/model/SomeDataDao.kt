/*
 * Copyright 2019 Stephen Brewer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.stephenbrewer.livedatabinding.persistence.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SomeDataDao {

    @Query("SELECT COUNT(*) FROM someData")
    fun getCount(): LiveData<Long?>

    @Query("SELECT id FROM someData ORDER BY first_name ASC")
    fun getTheOrderedListOfSomeIds(): LiveData<List<Long>?>

    @Query("SELECT * FROM someData ORDER BY first_name ASC")
    fun getTheOrderedListOfSomeData(): LiveData<List<SomeData>?>

    @Query("SELECT * FROM someData WHERE id = :id")
    fun findById(id: Long): LiveData<SomeData?>

    @Query("SELECT * FROM someData WHERE id = :id")
    fun findByIdSync(id: Long): SomeData

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(user: SomeData): Long

    @Update
    fun update(someData: SomeData)

    @Delete
    fun delete(user: SomeData)

    @Query("DELETE FROM someData WHERE id = :id")
    fun deleteById(id: Long)
}