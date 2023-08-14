package com.bitetype.bitetype

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.bitetype.bitetype.ui.theme.BiteTypeTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class MainActivity : ComponentActivity() {


    lateinit var textInput: EditText
    lateinit var goButton: Button
    lateinit var responseView: TextView

   private val openai = OpenAI(
        token = "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",
        timeout = Timeout(socket = 60.seconds),
        // additional configurations...
    )

    @OptIn(BetaOpenAI::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textInput = findViewById(R.id.aiTextField)
        goButton = findViewById(R.id.button)
        responseView = findViewById(R.id.myTextView)




        goButton.setOnClickListener {
            Toast.makeText(this, textInput.text, Toast.LENGTH_SHORT).show()
            val chatCompletionRequest = ChatCompletionRequest(
                model = ModelId("gpt-3.5-turbo"),
                messages = listOf(
                    ChatMessage(
                        role = ChatRole.System,
                        content = "Hello"
                    ),
                    ChatMessage(
                        role = ChatRole.User,
                        content = this.textInput.text.toString()
                    )
                )
            )
// or, as flow
            GlobalScope.launch {
                val fullResponse = openai.chatCompletion(chatCompletionRequest)

                println(fullResponse.choices[0].message!!?.content)
                println("--------------------------------------!!!!!!!!!!!!!!!!")
                println(fullResponse.choices[0].message!!?.content)
                responseView.text = fullResponse.choices[0].message!!?.content
            }
            //Toast.makeText(this, fullResponse.toString(), Toast.LENGTH_SHORT).show()



        }

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BiteTypeTheme {
        Greeting("Android")
    }
}