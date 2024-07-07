package com.example.safenest

import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safenest.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    lateinit var inviteAdapter : InviteAdapter
    private val listContacts: ArrayList<ContactModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var binding:FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listMembers = listOf<MemberModel>(
            MemberModel("Dewanshi", "300- Silicon City, Indore","90%","220"),
            MemberModel("Kanupriya","10th building, 3rdFloor, maldiv road, manali 10, Rajasthan, India","80%","210"),
            MemberModel("Awadhesh","4th Floor, maaad road, GOA","70%", "200"),
            MemberModel("Maddy","205-  Mahaveer Colony,near TI mall, Indore","85%", "330"),
            MemberModel("Madhuri","6th floor, AMD Apartments and Villas, Dubai","50%", "150"),
        )

        val adapter = MemberAdapter(listMembers)
        //val recycler = requireView().requireViewById<RecyclerView>(R.id.recycler_member)
        binding.recyclerMember.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMember.adapter = adapter


        inviteAdapter = InviteAdapter(listContacts)
        fetchDatabaseContact()
        CoroutineScope(Dispatchers.IO).launch {
            insertDatabaseContacts(fetchContacts())
        }

        //val inviteRecycler = requireView().findViewById<RecyclerView>(R.id.recycler_invite)

        binding.recyclerInvite.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerInvite.adapter = inviteAdapter

        binding.dotImg.setOnClickListener{
            SharedPref.putBoolean(PrefContants.IS_USER_LOGGED_IN,false)
            FirebaseAuth.getInstance().signOut()
            //Toast.makeText(this,"Successfully Signed Out",Toast.LENGTH_SHORT).show()
        }
    }


    private fun fetchDatabaseContact(){
        val database = SafeNestDatabase.getDatabase(requireContext())
        database.contactDAO().getAllContacts().observe(viewLifecycleOwner){
            listContacts.clear()
            listContacts.addAll(it)

            inviteAdapter.notifyDataSetChanged()
        }
    }

    private suspend fun insertDatabaseContacts(listContacts: ArrayList<ContactModel>) {
        val database = SafeNestDatabase.getDatabase(requireContext())
        database.contactDAO().insertAll(listContacts)
    }

    private fun fetchContacts(): ArrayList<ContactModel> {
        val cr = requireActivity().contentResolver
        val cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null)

        val listContacts:ArrayList<ContactModel> = ArrayList()
        if(cursor!=null && cursor.count>0){

            while(cursor!=null && cursor.moveToNext()){
                val id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhoneNumber = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                if(hasPhoneNumber>0){


                    val pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                        arrayOf(id),""
                    )
                    if(pCur != null && pCur.count>0){
                        while(pCur != null && pCur.moveToNext()){
                            val phoneNum = pCur.getString(pCur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            listContacts.add(ContactModel(name,phoneNum))
                        }
                        pCur.close()
                    }
                }
            }
            if(cursor!=null){
                cursor.close()
            }
        }
        return listContacts
    }
    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}