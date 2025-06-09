package com.yourdomain.todomanager.ui.todos

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.yourdomain.todomanager.R
import com.yourdomain.todomanager.data.models.Todo
import com.yourdomain.todomanager.databinding.FragmentAddEditTodoBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddEditTodoFragment : Fragment() {
    private var _binding: FragmentAddEditTodoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TodoViewModel by viewModels()
    private val args: AddEditTodoFragmentArgs by navArgs()
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    private var selectedDate: Date? = null
    private var hasUnsavedChanges = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAnimations()
        setupPriorityDropdown()
        setupDueDatePicker()
        setupValidation()
        setupButtons()
        observeViewModel()
        setupUnsavedChangesTracking()

        if (args.todoId != null) {
            loadTodo(args.todoId!!)
        }
    }

    private fun setupAnimations() {
        val slideIn = AnimationUtils.loadAnimation(context, R.anim.slide_in)
        binding.root.startAnimation(slideIn)
    }

    private fun setupPriorityDropdown() {
        val priorities = arrayOf(
            getString(R.string.priority_low),
            getString(R.string.priority_medium),
            getString(R.string.priority_high)
        )
        val adapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, priorities)
        binding.priorityAutoComplete.setAdapter(adapter)
        binding.priorityAutoComplete.setText(priorities[1], false) // Default to medium
    }

    private fun setupDueDatePicker() {
        binding.dueDateEditText.setOnClickListener {
            showDatePicker()
        }

        binding.dueDateLayout.setEndIconOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        selectedDate?.let { calendar.time = it }

        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                calendar.set(year, month, day)
                selectedDate = calendar.time
                binding.dueDateEditText.setText(dateFormat.format(calendar.time))
                hasUnsavedChanges = true
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun setupValidation() {
        binding.titleEditText.doAfterTextChanged {
            hasUnsavedChanges = true
            binding.titleLayout.error = when {
                it.isNullOrBlank() -> getString(R.string.error_required)
                it.length > 100 -> getString(R.string.error_title_too_long)
                else -> null
            }
        }

        binding.descriptionEditText.doAfterTextChanged {
            hasUnsavedChanges = true
            binding.descriptionLayout.error = if (it?.length ?: 0 > 500) {
                getString(R.string.error_description_too_long)
            } else {
                null
            }
        }

        binding.priorityAutoComplete.doAfterTextChanged {
            hasUnsavedChanges = true
            binding.priorityLayout.error = if (it.isNullOrBlank()) {
                getString(R.string.error_required)
            } else {
                null
            }
        }

        binding.completedSwitch.setOnCheckedChangeListener { _, _ ->
            hasUnsavedChanges = true
        }
    }

    private fun setupButtons() {
        binding.saveButton.setOnClickListener {
            if (validateInput()) {
                saveTodo()
            }
        }

        binding.cancelButton.setOnClickListener {
            if (hasUnsavedChanges) {
                showUnsavedChangesDialog()
            } else {
                navigateBack()
            }
        }
    }

    private fun setupUnsavedChangesTracking() {
        // Track all input changes
        binding.titleEditText.doAfterTextChanged { hasUnsavedChanges = true }
        binding.descriptionEditText.doAfterTextChanged { hasUnsavedChanges = true }
        binding.priorityAutoComplete.doAfterTextChanged { hasUnsavedChanges = true }
        binding.completedSwitch.setOnCheckedChangeListener { _, _ -> hasUnsavedChanges = true }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collectLatest { error ->
                error?.let {
                    Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                    viewModel.clearError()
                }
            }
        }
    }

    private fun validateInput(): Boolean {
        var isValid = true

        if (binding.titleEditText.text.isNullOrBlank()) {
            binding.titleLayout.error = getString(R.string.error_required)
            isValid = false
        }

        if (binding.titleEditText.text?.length ?: 0 > 100) {
            binding.titleLayout.error = getString(R.string.error_title_too_long)
            isValid = false
        }

        if (binding.descriptionEditText.text?.length ?: 0 > 500) {
            binding.descriptionLayout.error = getString(R.string.error_description_too_long)
            isValid = false
        }

        if (binding.priorityAutoComplete.text.isNullOrBlank()) {
            binding.priorityLayout.error = getString(R.string.error_required)
            isValid = false
        }

        return isValid
    }

    private fun saveTodo() {
        val todo = Todo(
            id = args.todoId ?: "",
            title = binding.titleEditText.text.toString(),
            description = binding.descriptionEditText.text.toString().takeIf { it.isNotBlank() },
            priority = binding.priorityAutoComplete.text.toString().lowercase(),
            dueDate = selectedDate,
            completed = binding.completedSwitch.isChecked
        )

        viewLifecycleOwner.lifecycleScope.launch {
            val result = if (args.todoId != null) {
                viewModel.updateTodo(todo)
            } else {
                viewModel.createTodo(todo)
            }

            result.fold(
                onSuccess = {
                    navigateBack()
                },
                onFailure = { error ->
                    Snackbar.make(
                        binding.root,
                        error.message ?: getString(R.string.error_saving_todo),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            )
        }
    }

    private fun loadTodo(todoId: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getTodo(todoId)?.let { todo ->
                binding.apply {
                    titleEditText.setText(todo.title)
                    descriptionEditText.setText(todo.description)
                    priorityAutoComplete.setText(todo.priority.capitalize(), false)
                    completedSwitch.isChecked = todo.completed
                    todo.dueDate?.let {
                        selectedDate = it
                        dueDateEditText.setText(dateFormat.format(it))
                    }
                }
                hasUnsavedChanges = false
            }
        }
    }

    private fun showUnsavedChangesDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.unsaved_changes)
            .setMessage(R.string.unsaved_changes_message)
            .setPositiveButton(R.string.discard) { _, _ ->
                navigateBack()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun navigateBack() {
        val slideOut = AnimationUtils.loadAnimation(context, R.anim.slide_out)
        binding.root.startAnimation(slideOut)
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 