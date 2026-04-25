package karo4a.contactbook

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import karo4a.contactbook.ui.theme.ContactBookTheme
import androidx.core.net.toUri

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContactBookTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainMenu(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

fun saveStartActivity(intent: Intent, context: Context) {
    try
    {
        context.startActivity(intent)
    }
    catch (ex: ActivityNotFoundException)
    {
        Toast.makeText(context, ex.message, Toast.LENGTH_SHORT).show()
    }
}

fun callPhoneNumber(phoneNumber: String,
                    context: Context,
) {
    val intent = Intent(Intent.ACTION_DIAL, "tel:$phoneNumber".toUri())
    saveStartActivity(intent, context)
}

fun sendEmail(email: String,
              theme: String,
              context: Context,
) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = "mailto:".toUri()
        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        putExtra(Intent.EXTRA_SUBJECT, theme)
    }
    saveStartActivity(intent, context)
}

fun showOnMap(latitude: Double,
              longitude: Double,
              label: String,
              context: Context,
) {
    val intent = Intent(Intent.ACTION_VIEW,
        "geo:0,0?q=$latitude,$longitude($label)".toUri())
    saveStartActivity(intent, context)
}

fun shareText(text: String,
              chooserTitle: String,
              context: Context
) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    val chooser = Intent.createChooser(intent, chooserTitle)
    saveStartActivity(chooser, context)
}

@Composable
fun MainMenu(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(Modifier.width(IntrinsicSize.Max)) {
            Button(
                onClick = { callPhoneNumber("+7 (495) 123-45-67", context) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.phone_button_text))
            }
            Button(
                onClick = { sendEmail("contact@example.com",
                    context.getString(R.string.email_theme_text), context) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.email_button_text))
            }
            Button(
                onClick = { showOnMap(60.0237,
                    30.2289,
                    "Наш офис",
                    context) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.map_button_text))
            }
            Button(
                onClick = { shareText("Контакт: +7 (495) 123-45-67, contact@example.com",
                    context.getString(R.string.map_chooser_title),
                    context) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.contact_button_text))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainMenuPreview() {
    ContactBookTheme {
        MainMenu()
    }
}