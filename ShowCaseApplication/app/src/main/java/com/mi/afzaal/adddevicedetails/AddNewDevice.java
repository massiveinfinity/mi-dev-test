package com.mi.afzaal.adddevicedetails;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mi.afzaal.models.DeviceData;
import com.mi.afzaal.showcaseapplication.DevicesListActivity;
import com.mi.afzaal.showcaseapplication.R;

public class AddNewDevice extends Activity implements IAddDeviceView,OnClickListener{

	EditText name,version,codename,target,distribution;
	Button submit;
	ProgressBar progressBar;
	AddDevicePresenterImpl devicePresentor;
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.addnewdevice_view);
	        name = (EditText) findViewById(R.id.name);
	        version = (EditText) findViewById(R.id.version);
	        codename = (EditText) findViewById(R.id.codename);
	        target = (EditText) findViewById(R.id.target);
	        distribution = (EditText) findViewById(R.id.distribution);
	        progressBar = (ProgressBar) findViewById(R.id.progressBar);
	        submit = (Button) findViewById(R.id.submit);
	        submit.setOnClickListener(this);
	        devicePresentor = new AddDevicePresenterImpl(this);
	        
	 }
	@Override
	public void showProgress() {
		progressBar.setVisibility(View.VISIBLE);
	}
	@Override
	public void hideProgress() {
		progressBar.setVisibility(View.GONE);

	}
	@Override
	public void setonSuccess() {
		progressBar.setVisibility(View.GONE);
		Toast.makeText(getApplicationContext(), "Data Saved Successfully",Toast.LENGTH_SHORT).show();
		this.navigateToHome();
	}
	@Override
	public void setonError() {
		progressBar.setVisibility(View.GONE);
		Toast.makeText(getApplicationContext(), "Error in Saving Data",Toast.LENGTH_SHORT).show();
		this.navigateToHome();
	}
	@Override
	public void onClick(View v) {
		
		if(v == submit){
			if(Validation()){
				progressBar.setVisibility(View.VISIBLE);
				DeviceData data = new DeviceData();
				data.setName(name.getText().toString());
				data.setVersion(version.getText().toString());
				data.setCodename(codename.getText().toString());
				data.setTarget(target.getText().toString());
				
				data.setDistribution(distribution.getText().toString());
				devicePresentor.AddData(data);
			}
		}
	}
	private boolean Validation(){
        if(TextUtils.isEmpty(name.getText().toString().trim())) {
        	name.setError("Enter the Name");
            return false;
        } else if(TextUtils.isEmpty(version.getText().toString().trim())) {
        	version.setError("Enter the Version");
            return false;
        } else if(TextUtils.isEmpty(codename.getText().toString().trim())) {
        	codename.setError("Enter the Code name");
            return false;
        } else if(TextUtils.isEmpty(target.getText().toString().trim())) {
        	target.setError("Enter the Target");
            return false;
        } else if(TextUtils.isEmpty(distribution.getText().toString().trim())) {
        	distribution.setError("Enter the Distribution");
            return false;
        }
        return true;
    }
	@Override
	public void navigateToHome() {
		Intent i =new Intent(AddNewDevice.this,DevicesListActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
		finish();
		startActivity(i);
	}
	
}
