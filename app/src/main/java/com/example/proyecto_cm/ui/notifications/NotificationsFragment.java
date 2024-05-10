package com.example.proyecto_cm.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyecto_cm.R;
import com.example.proyecto_cm.databinding.FragmentNotificationsBinding;
import com.google.android.gms.maps.MapView;

public class NotificationsFragment extends Fragment {

private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);


    binding = FragmentNotificationsBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        WebView myWebView = (WebView) root.findViewById(R.id.webview);
        myWebView.loadUrl("https://www.google.com/maps/d/u/0/edit?mid=12qwrCE-N27sAR4D75BZ09SaOkcQgcVQ&usp=sharing");
        myWebView.getSettings().setJavaScriptEnabled(true);

        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // Cargar la URL en la misma WebView
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        });

        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
