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

import androidx.lifecycle.*

class RecyclerViewLifecycleRegistry(owner: LifecycleOwner, private val parent: Lifecycle): LifecycleRegistry(owner) {

    private val parentLifecycleObserver = object: LifecycleObserver {
        @OnLifecycleEvent(Event.ON_ANY)
        fun onAny() {
            this@RecyclerViewLifecycleRegistry.currentState = parent.currentState
        }
    }

    var highestState = State.INITIALIZED
        set(value) {
            field = value
            if (parent.currentState >= value) {
                currentState = value
            }
        }

    init {
        observeParent()
    }

    private fun observeParent() {
        parent.addObserver(parentLifecycleObserver)
    }
    private fun ignoreParent() {
        parent.removeObserver(parentLifecycleObserver)
    }

    override fun setCurrentState(nextState: State) {
        val maxNextState = if (nextState > highestState) highestState else nextState
        if (nextState == State.DESTROYED) {
            ignoreParent()
        }
        super.setCurrentState(maxNextState)
    }
}
