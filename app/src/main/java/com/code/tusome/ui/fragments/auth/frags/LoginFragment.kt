package com.code.tusome.ui.fragments.auth.frags

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.code.tusome.R
import com.code.tusome.databinding.FragmentLoginBinding
import com.code.tusome.ui.viewmodels.MainViewModel
import com.code.tusome.utils.Utils


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Logging in....")
        progressDialog.setCancelable(false)
        progressDialog.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginBtn.setOnClickListener {
            progressDialog.show()
            val email = binding.emailEt.text.toString().trim()
            val password = binding.passwordEt.text.toString().trim()
            if (email.isBlank()) {
                progressDialog.dismiss()
                binding.emailEtl.error = "Email is required"
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                progressDialog.dismiss()
                binding.emailEtl.error = "Invalid email address"
                return@setOnClickListener
            }
            if (password.isBlank()) {
                progressDialog.dismiss()
                binding.passwordEtl.error = "Password is required"
                return@setOnClickListener
            }
            viewModel.login(email, password).observe(viewLifecycleOwner) {
                if (it) {
                    progressDialog.dismiss()
                    Utils.snackbar(binding.root, "Login successful")
                    findNavController().navigate(R.id.action_authFragment_to_homeActivity)
                }
                Utils.snackbar(binding.root, "Error logging in")
            }
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