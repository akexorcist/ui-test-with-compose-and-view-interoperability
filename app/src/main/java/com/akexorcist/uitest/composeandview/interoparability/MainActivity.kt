package com.akexorcist.uitest.composeandview.interoparability

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.akexorcist.uitest.composeandview.interoparability.ui.theme.UITestWithComposeAndViewInteroparabilityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UITestWithComposeAndViewInteroparabilityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        MainScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    Column {
        CustomComponent("Title 1", "Description 1")
        AnotherComponent("Title 2", "Description 2")
        AnotherComponent("Title 3", "Description 3")
    }
}

@Composable
fun CustomComponent(
    title: String,
    description: String,
) {
    AndroidView(
        modifier = Modifier.testTag("customComponent"),
        factory = { context -> CustomView(context).apply {
            id = R.id.custom_component
        } },
        update = { view ->
            view.setTitle(title)
            view.setDescription(description)
        },
    )
}

@Composable
fun AnotherComponent(
    title: String,
    description: String,
) {
    AndroidView(
        modifier = Modifier.testTag("anotherComponent"),
        factory = { context ->
            CustomView(context)
        },
        update = { view ->
            view.setTitle(title)
            view.setDescription(description)
        },
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UITestWithComposeAndViewInteroparabilityTheme {
        MainScreen()
    }
}

class CustomView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private var titleTextView: TextView
    private var descriptionTextView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_custom, this, true)

        titleTextView = findViewById(R.id.titleTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
    }

    fun setTitle(text: String) {
        titleTextView.text = text
    }

    fun setDescription(text: String) {
        descriptionTextView.text = text
    }
}
