package com.wallpaper.allwallpapers.Fragment

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.wallpaper.allwallpapers.Adapter.DownloadAdapter
import com.wallpaper.allwallpapers.databinding.FragmentDownloadBinding
import java.io.File

class DownloadFragment : Fragment() {
    lateinit var binding: FragmentDownloadBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentDownloadBinding.inflate(layoutInflater,container,false)

        activity?.title  = "Downloaded Wallpapers"

        val allFiles: Array<File>
        val imagelist = arrayListOf<String>()

        try{
            val path: String = Environment.getExternalStorageDirectory().absolutePath+ "/Pictures/All Wallpapers".toString()
            val targetfile = File(path)
            allFiles=targetfile.listFiles()!!

            for (data in allFiles){
                imagelist.add(data.absolutePath)
            }

        }catch (e:Exception){
            println(e.toString())
        }



        binding.downloadRec.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.downloadRec.adapter= DownloadAdapter(requireContext(),imagelist,this@DownloadFragment)




        return binding.root
    }



}