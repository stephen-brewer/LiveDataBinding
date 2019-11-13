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

package org.stephenbrewer.livedatabinding.ui.common

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import org.stephenbrewer.livedatabinding.persistence.ApplicationDatabase
import org.stephenbrewer.livedatabinding.persistence.SomeDataRepository

class ViewModel(application: Application): AndroidViewModel(application) {

    private val database = ApplicationDatabase.getDatabase(application.applicationContext)
    private val dao = database.someDataDao()
    private val repository = SomeDataRepository(dao)

    fun getTheOrderedListOfSomeIds(): LiveData<List<Long>?> {
        return repository.getTheOrderedListOfSomeIds()
    }
}
