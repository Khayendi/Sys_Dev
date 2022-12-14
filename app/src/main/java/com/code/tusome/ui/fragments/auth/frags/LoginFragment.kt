package com.code.tusome.ui.fragments.auth.frags

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.code.tusome.R
import com.code.tusome.databinding.FragmentLoginBinding
import com.code.tusome.databinding.PasswordResetBinding
import com.code.tusome.ui.viewmodels.MainViewModel
import com.code.tusome.utils.Utils
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: fragment started successfully")
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginBtn.setOnClickListener {
            binding.loginBtn.isEnabled = false
            binding.progressBar.visibility = VISIBLE
            val email = binding.emailEt.text.toString().trim()
            val password = binding.passwordEt.text.toString().trim()
            if (email.isBlank()) {
                binding.loginBtn.isEnabled = true
                binding.progressBar.visibility = GONE
                binding.emailEtl.error = "Email is required"
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.loginBtn.isEnabled = true
                binding.progressBar.visibility = GONE
                binding.emailEtl.error = "Invalid email address"
                return@setOnClickListener
            }
            if (password.isBlank()) {
                binding.loginBtn.isEnabled = true
                binding.progressBar.visibility = GONE
                binding.passwordEtl.error = "Password is required"
                return@setOnClickListener
            }
            viewModel.login(email, password).observe(viewLifecycleOwner) {
                if (it) {
                    binding.loginBtn.isEnabled = true
                    binding.progressBar.visibility = GONE
                    Utils.snackBar(binding.root, "Login successful")
                    findNavController().navigate(R.id.action_authFragment_to_homeActivity)
                }
                binding.loginBtn.isEnabled = true
                binding.progressBar.visibility = GONE
                Utils.snackBar(binding.root, "Error logging in")
            }
        }
        binding.forgotPassEt.setOnClickListener {
            val bind = PasswordResetBinding.bind(
                layoutInflater.inflate(
                    R.layout.password_reset,
                    binding.root,
                    false
                )
            )
            val alert = AlertDialog.Builder(requireContext())
                .setTitle("Password Reset")
                .setView(bind.root)
                .create()
            bind.submitBtn.setOnClickListener {
                it.isEnabled = false
                val email = bind.resetPassEt.text.toString().trim()
                if (email.isBlank()) {
                    bind.submitBtn.isEnabled = true
                    bind.resetPassEt.error = "Cannot be empty"
                    return@setOnClickListener
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    bind.submitBtn.isEnabled = true
                    bind.resetPassEt.error = "Invalid email address"
                    return@setOnClickListener
                }
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnSuccessListener {
                        bind.submitBtn.isEnabled = true
                        bind.resetPassEt.setText("")
                        Utils.snackBar(binding.root, "Password reset link sent to email")
                        alert.dismiss()
                    }.addOnFailureListener {
                        bind.submitBtn.isEnabled = true
                        Utils.snackBar(binding.root, "Something went wrong try again")
                    }
            }
            alert.show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        private val TAG = SignUpFragment::class.java.simpleName
    }
}