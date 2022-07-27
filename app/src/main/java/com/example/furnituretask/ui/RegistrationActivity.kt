package com.example.furnituretask.ui

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.edit
import androidx.lifecycle.Observer
import com.example.furnituretask.Constants
import com.example.furnituretask.R
import com.example.furnituretask.Registration
import com.example.furnituretask.databinding.ActivityRegistrationBinding
import com.example.furnituretask.enable
import com.example.furnituretask.remoteData.Resources
import com.example.furnituretask.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {
    private val viewModel:AuthViewModel by viewModels()
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    lateinit var binding:ActivityRegistrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.authLiveData.observe(this, Observer { resources->
            when(resources){
              is Resources.Success -> {
                  binding.registerLoading.visibility = View.GONE
                  Toast.makeText(this, getString(R.string.success_reg), Toast.LENGTH_SHORT).show()
                  resources.data?.let {
                      sharedPreferences.edit {
                          putString(Constants.AUTH_TOKEN,it.data.token)
                      }
                  }

              }
                is Resources.Failure -> {
                    binding.registerLoading.visibility = View.GONE
                    binding.registerBtn.enable(true)
                    resources.message?.let {
                        Toast.makeText(this, "error : $it", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resources.Loading -> binding.registerLoading.visibility = View.VISIBLE
            }
        })

        binding.registerBtn.setOnClickListener {
           validateFields()
        }

    }
    private fun validateFields(){
        val name = binding.registerName.text.toString().trim()
        val email = binding.registerEmail.text.toString().trim()
        val phone = binding.registerPhone.text.toString().trim()
        val address = binding.registerAddress.text.toString().trim()
        val password = binding.registerPassword.text.toString().trim()
        val confirmPassword = binding.registerPasswordAssert.text.toString().trim()
        if (name.isNotEmpty()&& email.isNotEmpty() && phone.isNotEmpty() && address.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() ){
            if (password==confirmPassword){
                binding.registerBtn.enable(false)
                val registration = Registration(name,email,phone,address,password,confirmPassword)
                viewModel.registerUser(registration)
            }else{
                Toast.makeText(this, getString(R.string.not_same_pass), Toast.LENGTH_SHORT).show()
            }

        }else{
            Toast.makeText(this, getString(R.string.required_fields), Toast.LENGTH_SHORT).show()
        }

    }
}