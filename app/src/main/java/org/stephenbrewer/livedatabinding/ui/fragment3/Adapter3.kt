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

package org.stephenbrewer.livedatabinding.ui.fragment3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.*
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.stephenbrewer.arch.lifecycle.LifecycleAwareCommand
import org.stephenbrewer.livedatabinding.databinding.RecyclerView3Binding
import org.stephenbrewer.livedatabinding.ui.common.ItemModel

class Adapter3(
    private val lifecycleOwner: LifecycleOwner
): RecyclerView.Adapter<Adapter3.ViewHolder3>() {

    val editCommand = LifecycleAwareCommand<Long>()
    val deleteCommand = LifecycleAwareCommand<Long>()

    class ViewHolder3(
        val binding: RecyclerView3Binding,
        parent: Lifecycle
    ): RecyclerView.ViewHolder(binding.root), LifecycleOwner {
        private val lifecycle = RecyclerViewLifecycleRegistry(this@ViewHolder3, parent)

        override fun getLifecycle(): RecyclerViewLifecycleRegistry {
            return lifecycle
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder3 {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerView3Binding.inflate(layoutInflater, parent, false)
        val viewHolder = ViewHolder3(binding, lifecycleOwner.lifecycle)
        binding.lifecycleOwner = viewHolder
        binding.itemModel = ItemModel(parent.context, "Adapter3")
        binding.deleteCommand = deleteCommand
        binding.editCommand = editCommand

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder3, position: Int) {
        val id = this.getKey(position)
        holder.binding.itemModel?.id = id
    }

    override fun onViewAttachedToWindow(holder: ViewHolder3) {
        super.onViewAttachedToWindow(holder)
        holder.lifecycle.highestState = Lifecycle.State.RESUMED
    }
    override fun onViewDetachedFromWindow(holder: ViewHolder3) {
        holder.lifecycle.highestState = Lifecycle.State.CREATED
        super.onViewDetachedFromWindow(holder)
    }

    private val asyncListDiffer = AsyncListDiffer<Long>(this@Adapter3, LongDiffCallback())
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
