package com.yourdomain.todomanager.ui.todos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.yourdomain.todomanager.R
import com.yourdomain.todomanager.databinding.FragmentTodoListBinding
import com.yourdomain.todomanager.ui.adapters.TodoAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TodoListFragment : Fragment() {
    private var _binding: FragmentTodoListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TodoViewModel by viewModels()
    private lateinit var adapter: TodoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSwipeRefresh()
        setupFab()
        observeTodos()
        observeError()
    }

    private fun setupRecyclerView() {
        adapter = TodoAdapter(
            onItemClick = { todo ->
                val action = TodoListFragmentDirections.actionTodoListFragmentToAddEditTodoFragment(todo.id)
                findNavController().navigate(action)
            },
            onItemLongClick = { todo ->
                showDeleteConfirmationDialog(todo.id)
            }
        )
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshTodos()
        }
    }

    private fun setupFab() {
        binding.fabAddTodo.setOnClickListener {
            val action = TodoListFragmentDirections.actionTodoListFragmentToAddEditTodoFragment(null)
            findNavController().navigate(action)
        }
    }

    private fun observeTodos() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.todos.collectLatest { todos ->
                adapter.submitList(todos)
                binding.emptyView.visibility = if (todos.isEmpty()) View.VISIBLE else View.GONE
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun observeError() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collectLatest { error ->
                error?.let {
                    Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                    viewModel.clearError()
                }
            }
        }
    }

    private fun showDeleteConfirmationDialog(todoId: String) {
         MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_todo)
            .setMessage(R.string.delete_todo_confirmation)
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteTodo(todoId)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 