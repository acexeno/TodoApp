package com.example.todomanager.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todomanager.R
import com.example.todomanager.data.model.Todo
import com.example.todomanager.databinding.ItemTodoBinding

class TodoAdapter(
    private val onTodoClick: (Todo) -> Unit,
    private val onDeleteClick: (Todo) -> Unit
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
        val todo = getItem(position)
        holder.bind(todo)
    }

    inner class TodoViewHolder(
        private val binding: ItemTodoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onTodoClick(getItem(position))
                }
            }

            binding.buttonDelete.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDeleteClick(getItem(position))
                }
            }
        }

        fun bind(todo: Todo) {
            binding.apply {
                textViewTitle.text = todo.title
                textViewDescription.text = todo.description ?: ""
                checkBoxCompleted.isChecked = todo.completed
                
                // Update text appearance based on completion status
                val textAppearance = if (todo.completed) {
                    R.style.TextAppearance_AppCompat_Body1
                } else {
                    R.style.TextAppearance_AppCompat_Headline
                }
                textViewTitle.setTextAppearance(textAppearance)
                
                // Show/hide description based on completion status
                textViewDescription.visibility = if (todo.completed) View.GONE else View.VISIBLE
            }
        }
    }
}

class TodoDiffCallback : DiffUtil.ItemCallback<Todo>() {
    override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem == newItem
    }
}
