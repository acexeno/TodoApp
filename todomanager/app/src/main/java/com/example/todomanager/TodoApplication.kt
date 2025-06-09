package com.example.todomanager

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TodoApplication : Application()

// Add this to AndroidManifest.xml:
// <application
//     android:name=".TodoApplication"
//     ...
// >
