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

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import org.stephenbrewer.arch.livedata.log
import org.stephenbrewer.livedatabinding.getSomeDataRepository

class ItemModel(context: Context, logTag: String) {
    private val repository = context.getSomeDataRepository()

    private val idLD = MutableLiveData<Long>()
    var id: Long
        get() { return idLD.value ?: 0L }
        set(value) { idLD.value = value }

    // Observe idLD, when it is updated, update the details od theData
    private val theData = Transformations.switchMap(idLD) {
        repository.getSomeData(it)
    }

    // Observe theData, when it is updated, update fullName.
    // fullName is being observed by databinding in the recycler view item xml
    val fullName: LiveData<String?> = Transformations.map(theData) { someData ->
        "${someData.id} ${someData.firstName} ${someData.lastName}"
    }.log(logTag)
}
