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

import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.stephenbrewer.arch.lifecycle.LifecycleAwareCommand
import org.stephenbrewer.livedatabinding.databinding.RecyclerView1Binding
import org.stephenbrewer.livedatabinding.persistence.model.SomeData

class Adapter1: ListAdapter<SomeData, Adapter1.ViewHolder1>(SomeDataDiffCallback()) {

    val editCommand = LifecycleAwareCommand<Long>()
    val deleteCommand = LifecycleAwareCommand<Long>()

    class ViewHolder1(private val binding: RecyclerView1Binding): RecyclerView.ViewHolder(binding.root) {
        fun bind(itemModel: ItemModel1) {
            binding.obj = itemModel
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder1 {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerView1Binding.inflate(layoutInflater, parent, false)
        val viewHolder = ViewHolder1(binding)
        binding.deleteCommand = deleteCommand
        binding.editCommand = editCommand

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder1, position: Int) {
        val someData = getItem(position)
        val id = someData.id
        val fullName = "${someData.id} ${someData.firstName} ${someData.lastName}"
        val model = ItemModel1(
            id,
            fullName
        )

        holder.bind(model)
        Log.d("Adapter1", "onBind          : [$fullName].")
    }

    class SomeDataDiffCallback : DiffUtil.ItemCallback<SomeData>() {
        override fun areItemsTheSame(oldItem: SomeData, newItem: SomeData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SomeData, newItem: SomeData): Boolean {
            return oldItem == newItem
        }
    }
}