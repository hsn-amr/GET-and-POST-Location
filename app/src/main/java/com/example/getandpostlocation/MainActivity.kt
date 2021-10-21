package com.example.getandpostlocation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var tv: TextView

    lateinit var inputName: EditText
    lateinit var showLocation: Button

    lateinit var name: EditText
    lateinit var location: EditText
    lateinit var save: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiInterface = APIClient.getClient()?.create(APIInterface::class.java)

        tv = findViewById(R.id.tv)

        inputName = findViewById(R.id.etInput)
        showLocation = findViewById(R.id.btnGet)

        showLocation.setOnClickListener {
            if(inputName.text.isNotEmpty()){
                val call: Call<List<User.UserInfo>?>? = apiInterface!!.getUsers()

                call?.enqueue(object : Callback<List<User.UserInfo>?>{
                    override fun onResponse(
                        call: Call<List<User.UserInfo>?>,
                        response: Response<List<User.UserInfo>?>
                    ) {
                        for(name in response.body()!!){
                            if(name.name.equals(inputName.text.toString(), true)){
                                tv.text = "${name.location}"
                            }
                        }
                        inputName.text.clear()
                    }

                    override fun onFailure(call: Call<List<User.UserInfo>?>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_LONG).show()
                    }

                })
            }else{
                Toast.makeText(this, "Please, Type Name", Toast.LENGTH_LONG).show()
            }
        }

        name = findViewById(R.id.etName)
        location = findViewById(R.id.etLocation)
        save = findViewById(R.id.btnSave)

        save.setOnClickListener {
            if(name.text.isNotEmpty() && location.text.isNotEmpty()){
                apiInterface!!.addUser(User(name.text.toString(), location.text.toString())).enqueue(
                    object : Callback<User>{
                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            Toast.makeText(this@MainActivity, "Saved", Toast.LENGTH_LONG).show()
                            name.text.clear()
                            location.text.clear()
                        }

                        override fun onFailure(call: Call<User>, t: Throwable) {
                            Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_LONG).show()
                        }

                    }
                )
            }else{
                Toast.makeText(this, "Please, Type Name & Location", Toast.LENGTH_LONG).show()
            }
        }
    }
}