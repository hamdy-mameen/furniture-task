package com.example.furnituretask.ui

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.Observer
import com.example.furnituretask.Constants
import com.example.furnituretask.Constants.Companion.AUTH_TOKEN
import com.example.furnituretask.R
import com.example.furnituretask.databinding.ActivityLoginBinding
import com.example.furnituretask.enable
import com.example.furnituretask.remoteData.Resources
import com.example.furnituretask.viewModels.AuthViewModel
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    @Inject
    lateinit var sharedPreferences:SharedPreferences
    @Inject
    lateinit var callbackManager: CallbackManager
    lateinit var binding: ActivityLoginBinding
    private val viewModel:AuthViewModel by viewModels()
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var task=GoogleSignIn.getSignedInAccountFromIntent(null)
    lateinit var token:AccessToken
    private var isSocial = false
    private var isFacebook =false
    private var isRegistered = true
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()
          mGoogleSignInClient =GoogleSignIn.getClient(this,gso)
          viewModel.authLiveData.observe(this, Observer { resources->
           when(resources){
               is Resources.Success ->{
                   binding.loginLoading.visibility = View.GONE
                   Toast.makeText(this, getString(R.string.success_login), Toast.LENGTH_SHORT).show()
                   resources.data?.let {
                      sharedPreferences.edit {
                          putString(Constants.AUTH_TOKEN,it.data.token)
                          apply()
                      }
                  }
                   val intent = Intent(this,HomeActivity::class.java).also {
                       it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                   }
                   startActivity(intent)
               }
             is Resources.Failure ->{
                 binding.loginLoading.visibility = View.GONE
                 handleButtons(true)

                 if (isSocial){
                     isRegistered =false
                     if (isFacebook){
                        handleFacebookAccessToken(token)
                     }else{
                         task?.let {
                             handleGoogleSignIn(it)
                         }
                     }
                 }
                 Toast.makeText(this, "error:${resources.message}", Toast.LENGTH_SHORT).show()
             }
          is Resources.Loading -> binding.loginLoading.visibility = View.VISIBLE
           }
       })

        //navigate to register
        binding.loginRegisterTxt.setOnClickListener {
            isSocial = false
            val intent = Intent(this,RegistrationActivity::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener {
            isSocial = false
            val email = binding.loginEmail.text.toString().trim()
            val password = binding.loginPassword.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()){
                handleButtons(false)
                viewModel.loginUser(email,password)
            }else{
                Toast.makeText(this, getString(R.string.enter_email), Toast.LENGTH_SHORT).show()
            }

        }
        binding.facebookBtn.setOnClickListener {
            handleButtons(false)
            LoginManager.getInstance().logInWithReadPermissions(this,listOf("public_profile","email"))
            LoginManager.getInstance().registerCallback(callbackManager, object :FacebookCallback<LoginResult> {
                override fun onCancel() {
                    Log.d("TAG", "facebook:onCancel")
                   handleButtons(true)
                }

                override fun onError(error: FacebookException) {
                    Log.d("TAG", "facebook:onError", error)
                   handleButtons(true)
                }

                override fun onSuccess(result: LoginResult) {
                    Log.d("TAG", "facebook:onSuccess:$result")
                    handleFacebookAccessToken(result.accessToken)
                }

            })
        }

        binding.googleBtn.setOnClickListener {
            handleButtons(false)
            val intent = mGoogleSignInClient.signInIntent
            startForResult.launch(intent)
        }

    }
    private fun handleButtons(isEnable:Boolean){
        binding.loginBtn.enable(isEnable)
        binding.googleBtn.enable(isEnable)
        binding.facebookBtn.enable(isEnable)

    }
    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {result->

        if(result.resultCode==Activity.RESULT_OK){
            isSocial = true
            isFacebook = false
             task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            Log.d("Tag","google result:${result.resultCode}")
            handleGoogleSignIn(task)
        }else{
            Log.d("Tag","google error:${result.resultCode}")
            handleButtons(true)
        }
    }

    private fun handleGoogleSignIn(task: Task<GoogleSignInAccount>) {
        val err = task.exception
        if (task.isSuccessful) {

            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null) {
                    val authCredential = GoogleAuthProvider.getCredential(
                        account.idToken, null
                    )
                    auth.signInWithCredential(authCredential).addOnCompleteListener {
                        if (task.isSuccessful) {
                            Log.d("TAG", "task.isSuccessful")
                            if (isRegistered) {
                                task.result.id?.let {
                                    Log.d("TAG", "isRegistered")
                                    viewModel.checkSocial("google", it)
                                }


                            } else {
                                Log.d("TAG", "noisRegistered")
                                Log.d("TAG", "${task.result.id}")
                                task.result.id?.let {
                                    viewModel.loginSocial(
                                        "google",
                                        it,
                                        task.result?.displayName,
                                        task.result?.email) }    }
                        }
                    }


                }


            } catch (e: ApiException) {
                Log.d("TAG", "google sign in failed:${e.message}")
                handleButtons(true)
            }
        } else {
            Log.d("TAG", "google sign in error:${err.toString()}")

    }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        isSocial = true
        isFacebook =true
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("TAG", "handleFacebookAccessToken:$token")
        val request = GraphRequest.newMeRequest(token
        ) { obj, response ->
            if (response?.error != null) {
                Log.d("TAG", "error:${response.error}")
            } else {
                obj?.let {
                    val userId = it.optString("id")
                    val userName = it.optString("name")
                    val userEmail = it.optString("email")
                    Log.d("TAG", "userId:$userId")
                    Log.d("TAG", "userName:$userName")
                    Log.d("TAG", "userEmail:$userEmail")
                    if (isRegistered){
                        viewModel.checkSocial("facebook",userId)
                    }else{
                        viewModel.loginSocial("facebook", userId, userName, userEmail)
                    }

                }

            }
        }
        val parameters = Bundle()
        parameters.putString("fields","id,name,email,gender,birthday")
         request.parameters = parameters
        request.executeAsync()
    }

    override fun onStart() {
        super.onStart()
        if(sharedPreferences.getString(AUTH_TOKEN,"") !=""){
            binding.loginLoading.visibility = View.VISIBLE
            val intent = Intent(this,HomeActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }


}