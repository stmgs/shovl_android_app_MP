package com.shovl.shovl_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.*
import com.shovl.shovl_android.databinding.ActivityLoginBinding
import com.shovl.shovl_android.utilities.PreferenceMangager
import com.shovl.shovl_android.utilities.ShovlConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.stripe.android.core.exception.APIException
import java.lang.Exception
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var email : String
    private lateinit var password:String
    private lateinit var preferenceMangager: PreferenceMangager
    private lateinit var auth : FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    //constatns
    private companion object{
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceMangager = PreferenceMangager(applicationContext)
        auth = FirebaseAuth.getInstance()

        //configure Google signing
       /* val googleSignInOptions =  GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions)
        //init firebase auth
        //checkUser()*/


        //if (Patterns.EMAIL_ADDRESS.matcher(username).matches() )
        binding.tvNewUser.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.tvForgotPasswordClick.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
            finish()
        }

        /*binding.glogin.setOnClickListener{
            //begin google signin
            Log.d(TAG,"Oncreate: begin google signin")
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }*/

        binding.btnLogin.setOnClickListener {
            email = binding.etEmail.text.toString()
            password = binding.etPassword.text.toString()

            if (!validateEmail(email) || !validatePassword(password)) {
                loading(false)

                //Toast.makeText(this,"Invalid email or password", Toast.LENGTH_LONG).show()
            } else {
                loading(true)

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val db = FirebaseFirestore.getInstance()
                        db.collection(ShovlConstants.KEY_COLLECTION_USERS)
                            .whereEqualTo(ShovlConstants.KEY_EMAIL, email)
                            .get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful
                                    && task.result != null
                                    && task.result.documents.size > 0
                                ) {
                                    val snapShot = task.result.documents[0]
                                    preferenceMangager.putBoolean(
                                        ShovlConstants.KEY_IS_SIGNED_IN,
                                        true
                                    )
                                    preferenceMangager.putString(
                                        ShovlConstants.KEY_USER_ID,
                                        snapShot.id
                                    )
                                    preferenceMangager.putString(
                                        ShovlConstants.KEY_EMAIL,
                                        snapShot.getString(ShovlConstants.KEY_EMAIL).toString()
                                    )
                                    preferenceMangager.putString(
                                        ShovlConstants.KEY_NAME,
                                        snapShot.getString(ShovlConstants.KEY_NAME).toString()
                                    )

                                    snapShot.getLong(ShovlConstants.KEY_AGE)?.let { it1 ->
                                        preferenceMangager.putInt(
                                            ShovlConstants.KEY_AGE,
                                            it1.toInt()
                                        )
                                    }

                                    preferenceMangager.putString(
                                        ShovlConstants.KEY_ADDRESS,
                                        snapShot.getString(ShovlConstants.KEY_ADDRESS).toString()
                                    )
                                    preferenceMangager.putString(
                                        ShovlConstants.KEY_GENDER,
                                        snapShot.getString(ShovlConstants.KEY_GENDER).toString()
                                    )
                                    preferenceMangager.putString(
                                        ShovlConstants.KEY_PHONE,
                                        snapShot.getString(ShovlConstants.KEY_PHONE).toString()
                                    )
                                    preferenceMangager.putString(
                                        ShovlConstants.KEY_DP_IMAGE,
                                        snapShot.getString(ShovlConstants.KEY_DP_IMAGE).toString()
                                    )
                                    val intent = Intent(this, HomeActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)

                                } else {
                                    loading(false)
                                    Toast.makeText(
                                        this,
                                        "Incorrect password for given email.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }.addOnFailureListener { exception ->
                                loading(false)

                                Toast.makeText(
                                    applicationContext,
                                    exception.localizedMessage,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    } else{
                        loading(false)
                        Toast.makeText(this, "Incorrect password for given email.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun checkUser(){
        //check if user is logged in or not
        val firebaseUser = auth.currentUser
        if(firebaseUser != null){
            //user is already logged in
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Result return
        if(requestCode == RC_SIGN_IN){
            Log.d(TAG,"onActivityResult: Google Sigin in intent result ")
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                println("inside try")
                //go0gle singin success, now auth with firebase
                val account = accountTask.getResult(APIException::class.java)
                firebaseAuthWithGoogleAccount(account)
            }
            catch (e: Exception){
                println("inside catch")
                ///failed google Signin
                Log.d("s", GoogleSignInStatusCodes.getStatusCodeString(8));
                Log.d(TAG,"onActivityResult: ${e.message}")
            }
        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        println("inside fb")
        Log.d(TAG,"firebaseAuthWithGoogleAccount: begin firebase auth with google account")
        val credential = GoogleAuthProvider.getCredential(account!!.idToken,null)
        auth.signInWithCredential(credential).addOnSuccessListener { authResult ->
            //login success
            Log.d(TAG,"Loogged in")

            //get loggedin user
            val firebaseUser = auth.currentUser
            //get user Info
            val uid = firebaseUser!!.uid
            val email = firebaseUser!!.email
            Log.d(TAG, "user id : $uid")
            Log.d(TAG, "email id : $email")


            ///new or exisiting
            if(authResult.additionalUserInfo!!.isNewUser){
                Log.d(TAG, "account created id : $email")
                Toast.makeText(this@LoginActivity, "account created \n$email", Toast.LENGTH_SHORT).show()
            }else{
                Log.d(TAG, "Exisiting user : $email")
                Toast.makeText(this@LoginActivity, "already user \n $email", Toast.LENGTH_SHORT).show()
            }

            //start profile activity
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish()
        }
            .addOnFailureListener { e ->
                //login failed
                Log.d(TAG, "failed  user auth  : ${e.message}")
                Toast.makeText(this@LoginActivity, "failed user \n ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun loading(isLoading : Boolean){
        if (isLoading){
            binding.pbLogIn.visibility= View.VISIBLE
            binding.btnLogin.visibility= View.INVISIBLE
        }else{
            binding.pbLogIn.visibility= View.GONE
            binding.btnLogin.visibility= View.VISIBLE
        }
    }

    private fun validateEmail(email: String): Boolean {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.textInputEmail.error = null
            binding.textInputEmail.isErrorEnabled = false
            return true

        } else {
            binding.textInputEmail.error = "Oops! Seems like you entered invalid email."
            return false
        }
    }

    private fun validatePassword(password: String): Boolean {
        val PASSWORD_PATTERN =
            Pattern.compile(
                "^" +
                        "(?=.*[0-9])" +         //at least 1 digit
                        //"(?=.*[a-z])" +         //at least 1 lower case letter
                        //"(?=.*[A-Z])" +         //at least 1 upper case letter
                        "(?=.*[a-zA-Z])" +      //any letter
                        "(?=.*[@#$%^&+=])" +    //at least 1 special character
                        //"(?=\\S+$)" +           //no white spaces
                        ".{8,}" +               //minimum 8 characters
                        "$"

            )

        // if password field is empty
        // it will display error message "Password can not be empty"
        if (password.isEmpty()){
            binding.textInputPassword.setError("Password cannot be empty")
            return false
        } else if(password.length <8){
            binding.textInputPassword.setError("Password must have at least 8 characters")
            return false
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            binding.textInputPassword.setError("Password must contain 1 special character and 1 digit")
            return false
        }  else {
            binding.textInputPassword.setError(null)
            binding.textInputPassword.isErrorEnabled = false
            return true
        }

    }

}