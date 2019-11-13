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

package org.stephenbrewer.livedatabinding.ui.activityMain

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.stephenbrewer.livedatabinding.persistence.ApplicationDatabase
import org.stephenbrewer.livedatabinding.persistence.SomeDataRepository
import org.stephenbrewer.livedatabinding.persistence.model.SomeData

class MainActivityViewModel(application: Application): AndroidViewModel(application) {
    private val database = ApplicationDatabase.getDatabase(application.applicationContext)
    private val dao = database.someDataDao()
    private val repository = SomeDataRepository(dao)

    private val someDataLD = MutableLiveData<SomeData>()

    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()

    fun setData(newData: SomeData?) {
        someDataLD.value = newData
        firstName.value = newData?.firstName?: ""
        lastName.value = newData?.lastName?: ""
    }

    @Suppress("UNUSED_PARAMETER")
    fun onFirstNameChanged(text: CharSequence, start: Int, before: Int, count: Int) {
        firstName.value = text.toString()
        someDataLD.value?.let {
            if (text != it.firstName) {
                updateDatabase(it.id)
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun onLastNameChanged(text: CharSequence, start: Int, before: Int, count: Int) {
        lastName.value = text.toString()
        someDataLD.value?.let {
            if (text != it.lastName) {
                updateDatabase(it.id)
            }
        }
    }

    private fun updateDatabase(id: Long) {
        val firstNameString = firstName.value ?: ""
        val lastNameString = lastName.value ?: ""
        val newData = SomeData(firstNameString, lastNameString).apply { this.id = id }
        viewModelScope.launch {
            repository.update(newData)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun createNewData(view: View) {
        viewModelScope.launch {
            for (counter in 0..9) {
                repository.insert(SomeData(randomString(), randomString()))
            }
        }
    }

    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z')
    private fun randomString() =
        (1..8)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
}