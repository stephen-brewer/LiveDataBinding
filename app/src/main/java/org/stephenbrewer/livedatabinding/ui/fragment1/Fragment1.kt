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

package org.stephenbrewer.livedatabinding.ui.fragment1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_1.*
import kotlinx.coroutines.launch
import org.stephenbrewer.assure.assureNotNull
import org.stephenbrewer.livedatabinding.R
import org.stephenbrewer.livedatabinding.getSomeDataRepository
import org.stephenbrewer.livedatabinding.ui.activityMain.MainActivityViewModel

class Fragment1: Fragment() {

    private lateinit var viewModel: ViewModel1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ViewModel1::class.java)

        val sharedViewModel = ViewModelProviders.of(requireActivity()).get(MainActivityViewModel::class.java)
        val adapter = Adapter1()

        val theOrderedData = viewModel.getTheOrderedListOfSomeData()
        theOrderedData.observe(viewLifecycleOwner, Observer { list ->
            adapter.submitList(list)
        })

        adapter.deleteCommand.observe(viewLifecycleOwner) {
            it.assureNotNull { id ->
                viewModel.viewModelScope.launch {
                    val repository = requireContext().getSomeDataRepository()
                    repository.deleteById(id)
                }
            }
        }
        adapter.editCommand.observe(viewLifecycleOwner) {
            it.assureNotNull { id ->
                viewModel.viewModelScope.launch {
                    val repository = requireContext().getSomeDataRepository()
                    val someData = repository.getSomeDataSync(id)
                    sharedViewModel.setData(someData)
                }
            }
        }

        recycler_view_1.adapter = adapter
        recycler_view_1.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        recycler_view_1.adapter = null
        super.onDestroyView()
    }
}
