from django.shortcuts import render, redirect, get_object_or_404
from django.contrib import messages
from django.utils import timezone
from .models import Todo
from .forms import TodoForm, RegistrationForm
from django.contrib.auth import login, authenticate
from django.contrib.auth.forms import AuthenticationForm
from django.contrib.auth.decorators import login_required

# Create your views here.

@login_required
def todo_list(request):
    # Display all todos
    todos = Todo.objects.filter(user=request.user)
        
    form = TodoForm()
    
    if request.method == 'POST':
        form = TodoForm(request.POST)
        if form.is_valid():
            form.save()
            messages.success(request, 'Todo created successfully!')
            return redirect('todo:todo_list')

    context = {
        'todos': todos,
        'form': form,
    }
    return render(request, 'todo/todo_list.html', context)

@login_required
def todo_update(request, pk):
    todo = Todo.objects.get(pk=pk, user=request.user)
    
    if request.method == 'POST':
        form = TodoForm(request.POST, instance=todo)
        if form.is_valid():
            form.save()
            messages.success(request, 'Todo updated successfully!')
            return redirect('todo:todo_list')
    else:
        form = TodoForm(instance=todo)
    
    context = {
        'form': form,
        'todo': todo,
    }
    return render(request, 'todo/todo_update.html', context)

@login_required
def todo_delete(request, pk):
    todo = Todo.objects.get(pk=pk, user=request.user)
    if request.method == 'POST':
        todo.delete()
        messages.success(request, 'Todo deleted successfully!')
        return redirect('todo:todo_list')
    return render(request, 'todo/todo_confirm_delete.html', {'todo': todo})

def todo_toggle_complete(request, pk):
    todo = get_object_or_404(Todo, pk=pk)
    todo.completed = not todo.completed
    todo.save()
    status = 'completed' if todo.completed else 'marked as incomplete'
    messages.success(request, f'Todo {status}!')
    return redirect('todo:todo_list')

def register_view(request):
    if request.method == 'POST':
        form = RegistrationForm(request.POST)
        if form.is_valid():
            user = form.save()
            login(request, user)
            return redirect('todo:todo_list')
    else:
        form = RegistrationForm()
    return render(request, 'registration/register.html', {'form': form})
