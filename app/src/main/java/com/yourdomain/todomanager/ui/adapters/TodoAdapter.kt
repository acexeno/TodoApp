package com.yourdomain.todomanager.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yourdomain.todomanager.data.models.Todo
import com.yourdomain.todomanager.databinding.ItemTodoBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TodoAdapter(
    private val onItemClick: (Todo) -> Unit,
    private val onItemLongClick: (Todo) -> Unit
) : ListAdapter<Todo, TodoAdapter.TodoViewHolder>(TodoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TodoViewHolder(
        private val binding: ItemTodoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
            binding.root.setOnLongClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemLongClick(getItem(position))
                    true
                } else {
                    false
                }
            }
        }

        fun bind(todo: Todo) {
            binding.apply {
                titleTextView.text = todo.title
                descriptionTextView.text = todo.description
                priorityChip.text = todo.priority.capitalize()
                dueDateTextView.text = todo.dueDate?.let { dateFormat.format(it) }
                completedCheckBox.isChecked = todo.completed

                // Set priority chip color
                val priorityColor = when (todo.priority.lowercase()) {
                    "high" -> android.graphics.Color.RED
                    "medium" -> android.graphics.Color.rgb(255, 165, 0) // Orange
                    else -> android.graphics.Color.GREEN
                }
                priorityChip.setChipBackgroundColorResource(priorityColor)
            }
        }
    }

    private class TodoDiffCallback : DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem == newItem
        }
    }
} 