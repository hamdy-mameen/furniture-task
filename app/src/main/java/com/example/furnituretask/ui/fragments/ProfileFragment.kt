package com.example.furnituretask.ui.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.furnituretask.Constants.Companion.AUTH_TOKEN
import com.example.furnituretask.R
import com.example.furnituretask.databinding.FragmentProfileBinding
import com.example.furnituretask.databinding.ListFragmentBinding
import com.example.furnituretask.enable
import com.example.furnituretask.remoteData.Resources
import com.example.furnituretask.showSnack
import com.example.furnituretask.ui.HomeActivity
import com.example.furnituretask.ui.LoginActivity
import com.example.furnituretask.ui.adapters.DiscountsAdapter
import com.example.furnituretask.ui.adapters.OffersAdapter
import com.example.furnituretask.ui.adapters.SavesAdapter
import com.example.furnituretask.viewModels.MainViewModel
import com.example.furnituretask.viewModels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment:Fragment(R.layout.list_fragment) {
    private var binding:FragmentProfileBinding?=null
    private val viewModel:ProfileViewModel by activityViewModels()
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.passWordLiveData.observe(viewLifecycleOwner, Observer { resources->
            when(resources){
                is Resources.Loading -> Toast.makeText(requireContext(), getString(R.string.loading), Toast.LENGTH_SHORT).show()
                is Resources.Success ->{
                    Toast.makeText(requireContext(), "${resources.data?.message}", Toast.LENGTH_SHORT).show()
                    binding?.let {
                        it.groupPassword.visibility =View.GONE
                        it.groupProfile.visibility =View.VISIBLE
                    }
                }
                is Resources.Failure -> {
                    resources.message?.let {message->
                        binding?.let {
                            it.groupPassword.visibility =View.GONE
                            it.groupProfile.visibility =View.VISIBLE
                            it.root.showSnack(message)
                        }
                    }
                }
            }

        })
        viewModel.logOutLiveData.observe(viewLifecycleOwner, Observer {resources->
            when(resources){
                is Resources.Loading -> Toast.makeText(requireContext(), getString(R.string.loading), Toast.LENGTH_SHORT).show()
                is Resources.Success ->{
                    sharedPreferences.edit {
                        putString(AUTH_TOKEN,"")
                        apply()
                    }
                    Toast.makeText(requireContext(), "${resources.data?.message}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(), LoginActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)

                }
                is Resources.Failure -> {
                    resources.message?.let {
                        binding?.root?.showSnack(it)
                    }
                }
            }

        })
        viewModel.profileLiveData.observe(viewLifecycleOwner, Observer {resources->
            when(resources){
                is Resources.Loading -> Toast.makeText(requireContext(), getString(R.string.loading), Toast.LENGTH_SHORT).show()
                is Resources.Success ->{
                  resources.data?.data?.let { data->
                      binding?.let {
                          it.registerAddress.text = data.address
                          it.registerName.text = data.name
                          it.registerPhone.text = data.phone
                          it.registerEmail.text = data.email
                          Glide.with(this).load(data.avatar).into(it.circleImg)

                      }

                  }
                }
                is Resources.Failure -> {
                    resources.message?.let {
                        binding?.root?.showSnack(it)
                    }
                }
            }

        })
        binding?.let {
            it.logOut.setOnClickListener { view->
                viewModel.logOut()
            }
            it.updatePassword.setOnClickListener { views->
                it.groupProfile.visibility = View.GONE
                it.groupPassword.visibility = View.VISIBLE
                it.registerBtn.enable(true)

            }
            it.registerBtn.setOnClickListener {view->
                if (it.registerPassword.text.toString().isNotEmpty() && it.registeroldPassword.text.toString().isNotEmpty() && it.registerPasswordAssert.text.toString().isNotEmpty()){
                    viewModel.updatePassword(it.registeroldPassword.text.toString(),it.registerPassword.text.toString(),it.registerPasswordAssert.text.toString())
                    it.registerBtn.enable(false)
                }
            }
        }

    }
}