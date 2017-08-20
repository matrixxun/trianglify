package com.sdsmdg.kd.trianglifyexample;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    ImageView githubLinkBtn, reviewLinkBtn, shareLink;
    TextView openSourceLicense, versiontTextView;
    PackageInfo pInfo;
    String versionName;
    int versionCode;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("About Us");

        SpannableString content = new SpannableString("view license");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

        // Gets version and build number from package manager
        try {
            pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            versionName = pInfo.versionName;
            versionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        versiontTextView = (TextView) this.findViewById(R.id.about_version_text);

        openSourceLicense = (TextView) this.findViewById(R.id.about_license_text);
        openSourceLicense.setText(content);

        githubLinkBtn = (ImageView) this.findViewById(R.id.about_github_link);
        reviewLinkBtn = (ImageView) this.findViewById(R.id.about_rate_link);
        shareLink = (ImageView) this.findViewById(R.id.about_share_link);
        versiontTextView.setText("Version " + versionName);
        openSourceLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayOpenSourceLicenses();
            }
        });

        githubLinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://github.com/sdsmdg/trianglify");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        reviewLinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + v.getContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + v.getContext().getPackageName())));
                }
            }
        });

        //TODO: Update link when app releases to marketplace

        shareLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_application_text) +
                        " " + getResources().getString(R.string.trianglify_store_short_link));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }
    public void displayOpenSourceLicenses() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(this.getApplicationInfo().name);
        alertDialog.setMessage(getResources().getString(R.string.license_text));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "view license", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getResources().getString(R.string.license_link))));
            }
        });
        alertDialog.show();
    }
}
