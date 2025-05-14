package com.example.proyecto_nativas.fragments


import com.example.proyecto_nativas.R

import android.os.Bundle
import android.view.*

import androidx.fragment.app.Fragment



class SuperiorNavFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_superior_nav, container, false)




        return view

    }

    // Aquí puedes inicializar botones, eventos, etc.


}