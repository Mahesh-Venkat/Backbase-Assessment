package com.sunfinder.sunfinder.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.sunfinder.sunfinder.R;

public class HelpFragment extends Fragment {
    WebView helpWebview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.help, container, false);
        helpWebview = (WebView) view.findViewById(R.id.help_webview);
        helpWebview.getSettings().setJavaScriptEnabled(true);
        helpWebview.loadUrl("file:///android_asset/SunFinderHelp.html");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

}
