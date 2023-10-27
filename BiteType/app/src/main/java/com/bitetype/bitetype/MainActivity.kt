package com.bitetype.bitetype

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
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
import com.aallam.openai.api.image.ImageCreation
import com.aallam.openai.api.image.ImageSize
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.bitetype.bitetype.ui.theme.BiteTypeTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.util.concurrent.Executors
import kotlin.time.Duration.Companion.seconds

class MainActivity : ComponentActivity() {


    lateinit var textInput: EditText
    lateinit var goButton: Button
    lateinit var responseView: TextView
    lateinit var imageResponseView: ImageView

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
        imageResponseView = findViewById(R.id.aiResponseImageView)

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
            GlobalScope.launch {
                val fullResponse = openai.chatCompletion(chatCompletionRequest)

                println(fullResponse.choices[0].message!!?.content)
                println("--------------------------------------!!!!!!!!!!!!!!!!")
                println(fullResponse.choices[0].message!!?.content)
                responseView.text = fullResponse.choices[0].message!!?.content

                val executor = Executors.newSingleThreadExecutor()
                val handler  = Handler(Looper.getMainLooper())
                var image: Bitmap? = null

                executor.execute {
                    GlobalScope.launch {
                        val images = openai.imageURL(
                            creation = ImageCreation(
                                prompt = textInput.text.toString(), //fullResponse.choices[0].message!!?.content!!,
                                n = 2,
                                size = ImageSize.is1024x1024
                            )
                        )
                        val image1 = images[0]
                       //tester  var newImage = "https://oaidalleapiprodscus.blob.core.windows.net/private/org-AJiV79nllE1S9OyTXz5XCpbQ/user-oPnS2YGdPYkSg4cQxmkEZsqp/img-hX8tCM1UqaeWZPzrBiBfWG3C.png?st=2023-08-14T19%3A05%3A59Z&se=2023-08-14T21%3A05%3A59Z&sp=r&sv=2021-08-06&sr=b&rscd=inline&rsct=image/png&skoid=6aaadede-4fb3-4698-a8f6-684d7786b067&sktid=a48cca56-e6da-484e-a814-9c849652bcb3&skt=2023-08-14T17%3A24%3A14Z&ske=2023-08-15T17%3A24%3A14Z&sks=b&skv=2021-08-06&sig=1RQoURxAP2UAeOb/ejmwTH7%2BNQ3RSu/0r6rvmaVNHdQ%3D"
                        println(image1.url)

                        try {
                            val `in` = java.net.URL(image1.url).openStream()
                            image = BitmapFactory.decodeStream(`in`)

                            handler.post {
                                imageResponseView.setImageBitmap(image)
                            }
                        }
                        catch (e: Exception) {
                            e.printStackTrace()
                        }
                }
            }
            //Toast.makeText(this, fullResponse.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    @OptIn(BetaOpenAI::class)
    fun createImage(fromPrompt: String)  {


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