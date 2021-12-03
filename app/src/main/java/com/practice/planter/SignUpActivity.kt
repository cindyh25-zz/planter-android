package com.practice.planter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.practice.planter.model.User
import com.practice.planter.model.UserTokens
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class SignUpActivity : AppCompatActivity() {
    val BASE_URL : String = "https://leafy-app-backend.herokuapp.com/"

    private var isLoggingIn : Boolean = false
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val client = OkHttpClient()
    private val userJsonAdapter = moshi.adapter(User::class.java)
    private val userTokensJsonAdapter = moshi.adapter(UserTokens::class.java)

    private lateinit var signUpButton : Button
    private lateinit var usernameEditText : EditText
    private lateinit var passwordEditText : EditText
    private lateinit var welcomeText : TextView
    private lateinit var switchLabel : TextView
    private lateinit var switchCta : Button

    fun setText(isLoggingIn : Boolean) {
        welcomeText = findViewById(R.id.welcome_text)
        switchLabel = findViewById(R.id.changeAuthText)
        switchCta = findViewById(R.id.authSecondaryButton)
        if (isLoggingIn) {
            welcomeText.text = getString(R.string.welcome_back)
            switchLabel.text = getString(R.string.sign_up_label)
            switchCta.text = getString(R.string.sign_up_cta)
            signUpButton.text = getString(R.string.log_in_cta)
        } else {
            welcomeText.text = getString(R.string.welcome)
            switchLabel.text = getString(R.string.log_in_label)
            switchCta.text = getString(R.string.log_in_cta)
            signUpButton.text = getString(R.string.sign_up_cta)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        switchCta = findViewById(R.id.authSecondaryButton)
        signUpButton = findViewById(R.id.authSaveButton)
        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)

        val session_token = getSharedPreferences("authPreferences", Context.MODE_PRIVATE).getString("session_token", "")
        val refresh_token = getSharedPreferences("authPreferences", Context.MODE_PRIVATE).getString("refresh_token", "")
        val session_expiration_string = getSharedPreferences("authPreferences", Context.MODE_PRIVATE).getString("session_expiration", "")
        val session_expiration : Date?
        Log.d("tokens sign up", "session:$session_token, refresh: $refresh_token")
        if (session_expiration_string?.isNotEmpty() == true) {
            session_expiration = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").parse(session_expiration_string)
        } else {
            session_expiration = null
        }

        if (session_token?.isNotEmpty() == true && !Date().after(session_expiration)) {
            val intent = Intent(applicationContext, MainActivity::class.java).apply {
                putExtra("session_token", session_token)
            }
            startActivity(intent)
        }

        switchCta.setOnClickListener {
            setText(!isLoggingIn)
            isLoggingIn = !isLoggingIn
        }

        signUpButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isEmpty() or password.isEmpty()) {
                return@setOnClickListener
            }

            val route : String
            if (isLoggingIn) {
                route = "login/"
            } else {
                route = "register/"
            }

            val registerRequest = Request.Builder().url(BASE_URL + route)
                .post(userJsonAdapter.toJson(User(username, password))
                    .toRequestBody(("application/json; charset=utf-8").toMediaType())).build()
            client.newCall(registerRequest).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    throw IOException(e.message)
                }

                override fun onResponse(call: Call, response: Response) {
                    // TODO: need to handle invalid username or password!

                    response.use {
                        val responseData = it.body!!.string()
                        Log.d("response", responseData)

                        val responseJSON = JSONObject(responseData)
                        val session_token = responseJSON.getString("session_token")
                        val sharedPreferences = getSharedPreferences("authPreferences", Context.MODE_PRIVATE) ?: return
                        with (sharedPreferences.edit()) {
                            putString("session_token", responseJSON.getString("session_token"))
                            putString("session_expiration", responseJSON.getString("session_expiration"))
                            putString("refresh_token", responseJSON.getString("refresh_token"))
                            putString("refresh_expiration", responseJSON.getString("refresh_expiration"))
                            apply()
                        }
                        val intent = Intent(applicationContext, MainActivity::class.java).apply {
                            putExtra("session_token", session_token)
                        }
                        startActivity(intent)
                    }

                }

            })

            }
        }
}