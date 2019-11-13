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

package org.stephenbrewer.livedatabinding.persistence

import androidx.lifecycle.LiveData
import org.stephenbrewer.livedatabinding.persistence.model.SomeData
import org.stephenbrewer.livedatabinding.persistence.model.SomeDataDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.stephenbrewer.arch.livedata.distinct
import org.stephenbrewer.arch.livedata.nonNull

class SomeDataRepository(private val someDataDao: SomeDataDao) {

    fun getTheOrderedListOfSomeIds(): LiveData<List<Long>?> {
        return someDataDao.getTheOrderedListOfSomeIds().distinct()
    }

    fun getTheOrderedListOfSomeData(): LiveData<List<SomeData>?> {
        return someDataDao.getTheOrderedListOfSomeData().distinct()
    }

    fun getSomeData(id: Long): LiveData<SomeData> {
        return someDataDao.findById(id).distinct().nonNull()
    }

    suspend fun getSomeDataSync(id: Long): SomeData {
        return withContext(Dispatchers.IO) {
            someDataDao.findByIdSync(id)
        }
    }

    suspend fun insert(someData: SomeData): Long {
        return withContext(Dispatchers.IO) {
            val newId = someDataDao.insert(someData)
            // notify the registered adapters
            newId
        }
    }

    suspend fun update(someData: SomeData) {
        withContext(Dispatchers.IO) {
            someDataDao.update(someData)
        }
    }

    suspend fun deleteById(id: Long) {
        withContext(Dispatchers.IO) {
            someDataDao.deleteById(id)
        }
    }
}
