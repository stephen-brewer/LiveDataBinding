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

package org.stephenbrewer.livedatabinding.ui.fragment4

import android.view.LayoutInflater
import android.view.ViewGroup
import org.stephenbrewer.arch.lifecycle.LifecycleAwareCommand
import org.stephenbrewer.arch.recyclerview.AsyncListDiffer
import org.stephenbrewer.arch.recyclerview.DiffUtil
import org.stephenbrewer.arch.recyclerview.RecyclerView
import org.stephenbrewer.livedatabinding.databinding.RecyclerView4Binding
import org.stephenbrewer.livedatabinding.ui.common.ItemModel

class Adapter4: RecyclerView.Adapter<Adapter4.ViewHolder4>() {

    val editCommand = LifecycleAwareCommand<Long>()
    val deleteCommand = LifecycleAwareCommand<Long>()

    class ViewHolder4(
        val binding: RecyclerView4Binding
    ): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder4 {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerView4Binding.inflate(layoutInflater, parent, false)
        val viewHolder = ViewHolder4(binding)
        binding.lifecycleOwner = viewHolder
        binding.itemModel = ItemModel(parent.context, "Adapter4")
        binding.deleteCommand = deleteCommand
        binding.editCommand = editCommand

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder4, position: Int) {
        val id = this.getKey(position)
        holder.binding.itemModel?.id = id
    }

    private val asyncListDiffer = AsyncListDiffer<Long>(this@Adapter4, LongDiffCallback())
    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }
    private fun getKey(position: Int): Long {
        return asyncListDiffer.currentList[position]
    }
    fun submitKeys(newKeys: List<Long>?) {
        asyncListDiffer.submitList(newKeys)
    }

    class LongDiffCallback: DiffUtil.ItemCallback<Long>() {
        override fun areItemsTheSame(oldItem: Long, newItem: Long): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Long, newItem: Long): Boolean {
            return false
        }
    }
}
