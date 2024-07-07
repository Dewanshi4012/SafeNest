package com.example.safenest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.safenest.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

class LoginActivity :AppCompatActivity(){

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth:FirebaseAuth
    private val RC_SIGN_IN = 56
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)


        val signBtn = findViewById<Button>(R.id.signin_btn)

        val signInRequest = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this,signInRequest)

        auth = Firebase.auth

        signBtn.setOnClickListener{
            signIn()
        }

    }
    fun signIn(){
        val signInIntent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }
    private var resultLauncher:ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)!!
                Log.d("TAG", "fireBaseAuthWithGoogle: " + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w("TAG", "Google SignIn Failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential:AuthCredential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener(this){task->
            if (task.isSuccessful){
                Log.d("fire89","signInWithCredential: Success")
                SharedPref.init(this)
                SharedPref.putBoolean(PrefContants.IS_USER_LOGGED_IN, true)
                val user:FirebaseUser?=auth.currentUser
                //user?.displayName?.let { SharedPref.putString(PrefContants.IS_USER_LOGGED_IN, it) }


                startActivity(Intent(this,MainActivity::class.java))
            }else{
                Log.w("fire89", "signInWithCredential : Falied")
                //updateUI(null)
            }
        }
    }
}