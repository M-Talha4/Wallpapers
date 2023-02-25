package com.wallpaper.allwallpapers.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.wallpaper.allwallpapers.Adapter.WallpaperAdapter
import com.wallpaper.allwallpapers.ModalClass.wallpapermodal
import com.wallpaper.allwallpapers.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore


class HomeFragment : Fragment() {


    lateinit var binding: FragmentHomeBinding
    lateinit var firestore: FirebaseFirestore





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentHomeBinding.inflate(layoutInflater,container,false)



        activity?.title  = "All Wallpapers"







        firestore  = FirebaseFirestore.getInstance()
        firestore.collection("Haaland").addSnapshotListener{
            value , error ->
            val list =  arrayListOf<wallpapermodal>()

            val data = value?.toObjects(wallpapermodal::class.java)
            list.addAll(data!!)





            binding.mainRv.layoutManager =StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            binding.mainRv.adapter = WallpaperAdapter(requireContext(),list,this@HomeFragment)

        }








        return  binding.root
    }












}